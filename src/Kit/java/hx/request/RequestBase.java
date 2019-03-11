package hx.request;

import android.content.Context;
import androidx.fragment.app.DialogFragment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import hx.kit.log.Log4Android;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class RequestBase {

    protected final String TAG = "Api.Request";
    protected static int TIME = 60;

    protected DialogFragment sDialog;
    protected Context sCtx;
    protected OkHttpClient mOkHttpClient;
    protected OkHttpClient.Builder mOkHttpBuilder;
    protected ObjectMapper mObjMapper;

    protected RequestBase(Context ctx){
        this.sCtx = ctx;
        mOkHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(time(), TimeUnit.SECONDS)
                .writeTimeout(time(), TimeUnit.SECONDS)
                .connectTimeout(time(), TimeUnit.SECONDS)
//                .addInterceptor(Interceptor)  //可以处理connectionFailure的retry次数
                .retryOnConnectionFailure(false);
        mObjMapper = new ObjectMapper();
        mObjMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mObjMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mObjMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
    public int time(){
        return TIME;
    }

}
