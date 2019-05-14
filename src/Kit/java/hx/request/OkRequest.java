package hx.request;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import hx.kit.log.Log4Android;
import hx.lib.R;
import hx.widget.dialog.DWaiting;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by RoseHongXin on 2018/8/1 0001.
 */

public class OkRequest extends RequestBase {

    private Handler mMainHandler;

    public OkRequest(Context ctx) {
        super(ctx);
        mMainHandler = new Handler();
        HttpLoggingInterceptor bodyLogger = new HttpLoggingInterceptor(message -> Log4Android.v(TAG, message));
        bodyLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = mOkHttpBuilder.addInterceptor(bodyLogger).build();
    }

    public <D> void request(Activity act, Request request, Callback<D> callback) {
        request(act, request, null, callback);
    }
    public <D> void request(Activity act, Request request, String hint, Callback<D> callback) {
        if(act != null) {
            sDialog = TextUtils.isEmpty(hint) ? DWaiting.show(act) : DWaiting.show(act, hint);
        }
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
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
                            mMainHandler.post(() -> {});
                            callback.onFailed(e.getMessage());
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
