package hx.request;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import hx.kit.log.Log4Android;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class RequestBase {

    private final String TAG = "Api.Request";
    private static final int TIME = 12;

    protected DialogFragment sDialog;
    protected Context sCtx;
    protected OkHttpClient mOkHttpClient;
    protected OkHttpClient.Builder mOkHttpBuilder;
    protected ObjectMapper mObjMapper;

    protected RequestBase(Context ctx){
        this.sCtx = ctx;
        HttpLoggingInterceptor bodyLogger = new HttpLoggingInterceptor(message -> Log4Android.v(TAG, message));
        bodyLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(TIME, TimeUnit.SECONDS)
                .writeTimeout(TIME, TimeUnit.SECONDS)
                .connectTimeout(TIME, TimeUnit.SECONDS)
                .addInterceptor(bodyLogger);
        mObjMapper = new ObjectMapper();
        mObjMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mObjMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mObjMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

}
