package hx.kit.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {

    static private SharedPreferenceStub mSpStub;

    public SpHelper(Context ctx){
        init(ctx);
    }

    public static SharedPreferences get(){
        return mSpStub.sharedPref();
    }

    public static void init(Context ctx){
        mSpStub = new SharedPreferenceStub(ctx, ctx.getPackageName());
    }

    public static String get(String key){
        return mSpStub.get(key);
    }
    public static int getInt(String key){
        return getInt(key, 0);
    }
    public static int getInt(String key, int def){
        return mSpStub.sharedPref() == null ? def : mSpStub.sharedPref().getInt(key, def);
    }
    public static boolean getBoolean(String key, boolean def){
        return mSpStub.getBoolean(key, def);
    }

    public static void set(String key, int data){
        mSpStub.set(key, data);
    }
    public static void set(String key, String data){
        mSpStub.set(key, data);
    }
    public static <T> void set(String key, T t){
        mSpStub.set(key, t);
    }
    public static void set(String key, boolean b){
        mSpStub.set(key, b);
    }

    public static void clear(String key) {
        mSpStub.clear(key);
    }

}
