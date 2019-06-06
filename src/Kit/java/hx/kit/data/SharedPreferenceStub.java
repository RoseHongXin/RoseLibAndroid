package hx.kit.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceStub {

    private SharedPreferences mSp;

    public SharedPreferenceStub(Context ctx, String name){
        mSp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences sharedPref(){
        return mSp;
    }

    public String get(String key){
        if(mSp == null) return "";
        return mSp.getString(key, "");
    }
    public int getInt(String key){
        if(mSp == null) return 0;
        return mSp.getInt(key, 0);
    }
    public boolean getBoolean(String key, boolean def){
        if(mSp == null) return def;
        return mSp.getBoolean(key, def);
    }

    public void set(String key, int data){
        if(mSp != null) mSp.edit().putInt(key, data).apply();
    }
    public void set(String key, String data){
        if(mSp != null) mSp.edit().putString(key, data).apply();
    }
    public <T> void set(String key, T t){
        String data = JSONHelper.toJSONStr(t);
        if(mSp != null) mSp.edit().putString(key, data).apply();
    }
    public void set(String key, boolean b){
        if(mSp != null) mSp.edit().putBoolean(key, b).apply();
    }

    public void clear(String key) {
        if (mSp != null && mSp.contains(key)) {
            mSp.edit().putString(key, "").apply();
        }
    }

}
