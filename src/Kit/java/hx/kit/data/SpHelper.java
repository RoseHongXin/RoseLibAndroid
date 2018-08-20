package hx.kit.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

public class SpHelper {

    static private SharedPreferences mSp;

    public SpHelper(Context ctx){
        init(ctx);
    }

    public static SharedPreferences get(){
        return mSp;
    }

    public static void init(Context ctx){
        mSp = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String get(String key){
        if(mSp == null) return "";
        return mSp.getString(key, "");
    }
    public static int getInt(String key){
        if(mSp == null) return 0;
        return mSp.getInt(key, 0);
    }
    public static boolean getBoolean(String key, boolean def){
        if(mSp == null) return def;
        return mSp.getBoolean(key, def);
    }

    public static void set(String key, int data){
        if(mSp != null) mSp.edit().putInt(key, data).apply();
    }
    public static void set(String key, String data){
        if(mSp != null) mSp.edit().putString(key, data).apply();
    }
    public static <T> void set(String key, T t){
        String data = JSON.toJSONString(t);
        if(mSp != null) mSp.edit().putString(key, data).apply();
    }
    public static void set(String key, boolean b){
        if(mSp != null) mSp.edit().putBoolean(key, b).apply();
    }

    public static void clear(String key) {
        if (mSp != null && mSp.contains(key)) {
            mSp.edit().putString(key, "").apply();
        }
    }

}
