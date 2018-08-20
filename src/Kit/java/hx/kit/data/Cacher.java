package hx.kit.data;

/**
 * Created by rose on 16-8-9.
 *
 *
 * use framework disklrucache and fastjson
 *
 * for sort of caching stuff.
 *
 */

@Deprecated
public class Cacher {

//    private final int VALUE_COUNT = 1;
//    private final int MAX_SIZE = VALUE_COUNT * 1024 * 1024;
//
//    private static final int IDX = 0;
//
//    private static DiskLruCache cache;
//
//    private Cacher(Context ctx){
//        int appVersion = 0;
//        try {
//            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
//            appVersion = pi.versionCode;
//            //appVersion = Build.VERSION.SDK_INT;
//            //appVersion = android.os.Build.VERSION_CODES.
//            cache = DiskLruCache.open(ctx.getCacheDir(), appVersion, VALUE_COUNT, MAX_SIZE);
//            //KEY = ctx.getPackageName();
//        } catch (PackageManager.NameNotFoundException | IOException e) {
//            e.printStackTrace();
//            cache = null;
//        }
//    }
//
//    /**
//     *
//     * Call this method at the app entry.
//     * get(...) return generic java bean class
//     * getList(...) return List mData.
//     *
//     *
//     * */
//    public static Cacher init(Context ctx){
//        return new Cacher(ctx);
//    }
//
//    public static <T> boolean write(String key, T data){
//        if(cache == null) return false;
//        try {
//            String str = JSON.toJSONString(data);
//            //OutputStream os = cache.edit(key).newOutputStream(IDX);
//            DiskLruCache.Editor editor = cache.edit(key);
//            editor.set(IDX, str);
//            editor.commit();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static <T> T get(String key, Class<T> clz){
//        T data = null;
//        try {
//            DiskLruCache.Snapshot snap = cache.get(key);
//            if(snap == null) return null;
//            String str = snap.getString(IDX);
//            if(TextUtils.isEmpty(str)) return null;
//            data = JSON.parseObject(str, clz);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//    public static <T> List<T> getList(String key, Class<T> clz){
//        List<T> data = new ArrayList<T>();
//        try {
//            DiskLruCache.Snapshot snap = cache.get(key);
//            if(snap == null) return data;       //empty array
//            String str = snap.getString(IDX);
//            if(TextUtils.isEmpty(str)) return data; //empty array
//            data = JSON.parseArray(str, clz);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(data == null) data = new ArrayList<T>();
//        return data;
//    }
}
