package hx.request;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import hx.widget.dialog.DWaiting;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by RoseHongXin on 2018/8/1 0001.
 */

public class OkRequest extends RequestBase {

    private Handler mMainHandler;

    public OkRequest(Context ctx) {
        super(ctx);
        mMainHandler = new Handler();
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
            public void onFailure(Call call, IOException e) {
                mMainHandler.post(() -> {
                    callback.onFailed(e.getMessage());
                    callback.onFinish();
                    if(sDialog != null) sDialog.dismiss();
                });
            }
            @Override
            public void onResponse(Call call, Response response)  {
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
                            mMainHandler.post(() -> {callback.onSuccess(resp);});
                        } catch (IOException e) {
                            e.printStackTrace();
                            mMainHandler.post(() -> {});
                            callback.onFailed(e.getMessage());
                        }
                        body.close();
                    }
                }else{
                }
                mMainHandler.post(() -> {
                    callback.onFinish();
                    if(sDialog != null) sDialog.dismiss();
                });

            }
        });
    }


    public static class Callback<D>{
        public void onSuccess(D resp){}
        public void onFailed(String exception){}
        public void onFinish(){}
    }

}
