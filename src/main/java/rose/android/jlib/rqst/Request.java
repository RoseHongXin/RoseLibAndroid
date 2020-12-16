package rose.android.jlib.rqst;

import android.content.Context;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import rose.android.jlib.widget.dialog.DLoading;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


public abstract class Request {

    protected final String TAG = "Api.Request";
    protected static int TIME = 60;

    protected DLoading sDialog;
    protected Context sCtx;
    protected String sUrl;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mOkHttpBuilder;
    protected ObjectMapper mObjMapper;

    protected abstract Interceptor getInterceptor();
    protected abstract OkHttpClient onInit(OkHttpClient.Builder builder);
    public int getTime(){
        return TIME;
    }
    protected OkHttpClient client() { return mOkHttpClient; }

    protected Request(Context ctx, String url){
        this.sCtx = ctx;
        this.sUrl = url;
        mOkHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(getTime(), TimeUnit.SECONDS)
                .writeTimeout(getTime(), TimeUnit.SECONDS)
                .connectTimeout(getTime(), TimeUnit.SECONDS)
//                .addInterceptor(Interceptor)  //可以处理connectionFailure的retry次数
                .retryOnConnectionFailure(true);
        mObjMapper = new ObjectMapper();
        mObjMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mObjMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mObjMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mOkHttpBuilder
                .addInterceptor(getInterceptor());
        mOkHttpClient = onInit(mOkHttpBuilder);
    }


}
