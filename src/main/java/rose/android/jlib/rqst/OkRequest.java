package rose.android.jlib.rqst;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.fasterxml.jackson.core.type.TypeReference;
import rose.android.jlib.kit.log.Log4Android;
import hx.lib.R;
import rose.android.jlib.widget.dialog.DLoading;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by RoseHongXin on 2018/8/1 0001.
 */

public class OkRequest extends Request {

    private final Handler mMainHandler;

    public OkRequest(Context ctx) {
        super(ctx, "");
        mMainHandler = new Handler();

    }
    @Override
    protected Interceptor getInterceptor() {
        return null;
    }

    @Override
    protected OkHttpClient onInit(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor bodyLogger = new HttpLoggingInterceptor(message -> Log4Android.v(TAG, message));
        bodyLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        return builder.addInterceptor(bodyLogger).build();
    }

    public <D> void request(Activity act, okhttp3.Request request, Callback<D> callback) {
        request(act, request, null, callback);
    }
    public <D> void request(Activity act, okhttp3.Request request, String hint, Callback<D> callback) {
        if(act != null) {
            sDialog = TextUtils.isEmpty(hint) ? DLoading.show(act) : DLoading.show(act, hint);
        }
        client().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mMainHandler.post(() -> {
                    callback.onFailed(e.getMessage());
                    callback.onFinish();
                    if(sDialog != null) sDialog.dismiss();
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)  {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if(body == null){
                        mMainHandler.post(() -> {callback.onFailed("");});
                    }else{
                        try {
                            D resp = mObjMapper.readValue(body.string(), new TypeReference<D>(){
                                @Override
                                public Type getType() {
                                    return ((ParameterizedType)callback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//                                    return ((ParameterizedType)callback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
                                }
                            });
                            mMainHandler.post(() -> {callback.onResp(resp);});
                        } catch (IOException e) {
                            e.printStackTrace();
                            mMainHandler.post(() -> {
                                callback.onFailed(e.getMessage());
                            });

                        }
                        body.close();
                    }
                }else{
                    mMainHandler.post(() -> {
                        String message = response.message();
                        if(response.code() == 404) { message = sCtx.getString(R.string.HX_invalidHttpRequest); }
                        else if(response.code() >= 500 && response.code() <= 505){ message = sCtx.getString(R.string.HX_serverInternalError); }
                        callback.onFailed(message);
                    });
                }
                mMainHandler.post(() -> {
                    callback.onFinish();
                    if(sDialog != null) sDialog.dismiss();
                });

            }
        });
    }

    public static class Callback<D>{
        public void onResp(D resp){}
        public void onFailed(String exception){}
        public void onFinish(){}
    }

}
