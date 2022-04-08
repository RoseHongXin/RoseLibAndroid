package rose.android.jlib.components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Android6.0 以后,权限检查与获取.
 *
 */

public class PermImpl {

    public static final int RC_CODE = 0xFFEE;

    public static void require(Activity act, int rqCode, String ... permissions) {
        List<String> needDoRequire = new ArrayList<>();
        boolean requireStoragePerm = false;
        for (String permission : permissions) {
            if(Manifest.permission.READ_EXTERNAL_STORAGE.equalsIgnoreCase(permission) ||
                    Manifest.permission.WRITE_EXTERNAL_STORAGE.equalsIgnoreCase(permission) ||
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE.equalsIgnoreCase(permission)) {
                if(!ifStorageGranted(act)) { requireStoragePerm = true; }
                continue;
            }
            if (ContextCompat.checkSelfPermission(act, permission) != PackageManager.PERMISSION_GRANTED) {
                needDoRequire.add(permission);
            }
        }
        if(needDoRequire.isEmpty() && !requireStoragePerm) return;
        if(!needDoRequire.isEmpty()){
            String[] permissionArray;
            permissionArray = new String[needDoRequire.size()];
            needDoRequire.toArray(permissionArray);
            ActivityCompat.requestPermissions(act, permissionArray, rqCode);
        }
        if(requireStoragePerm){ requireStorage(act, rqCode); }
    }
    public static void require(Activity act, String ... permissions) {
        require(act, RC_CODE, permissions);
    }

    public static boolean ifDenied(Activity act, String permission){
        return ActivityCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_DENIED;
    }
    public static boolean ifGranted(Context ctx, String permission){
        if(ctx == null) return false;
        return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean ifGranted(Context ctx, String ... permissions){
        for(String permission : permissions){
            if(Manifest.permission.READ_EXTERNAL_STORAGE.equalsIgnoreCase(permission) ||
                    Manifest.permission.WRITE_EXTERNAL_STORAGE.equalsIgnoreCase(permission) ||
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE.equalsIgnoreCase(permission)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return Environment.isExternalStorageManager();
                }
            }
            boolean granted = ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
            if(!granted) return false;
        }
        return true;
    }
    public static boolean ifNoToastAnymore(Activity act, String ...permissions){
        for(String perm : permissions){
            boolean show = ActivityCompat.shouldShowRequestPermissionRationale(act, perm);
            if(!show) { return true; }
        }
        return false;
    }
    public static boolean ifStorageGranted(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }else{
            return ifGranted(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    public static void requireStorage(Activity act, int rqCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + act.getPackageName()));
            act.startActivityForResult(intent, rqCode);
        }else{
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, rqCode);
        }
    }
}
