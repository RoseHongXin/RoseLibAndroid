package hx.components;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * Android6.0 以后,权限检查与获取.
 *
 */

public class PermissionImpl {

    public static final int PERMISSION_REQ_CODE = 0xFFEE;

    public static void require(Activity act, String ... permissions) {
        List<String> needDoRequire = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(act, permission) != PackageManager.PERMISSION_GRANTED) {needDoRequire.add(permission);}
        }
        String[] permissionArray;
        if(needDoRequire.isEmpty()) return;
        permissionArray = new String[needDoRequire.size()];
        needDoRequire.toArray(permissionArray);
        ActivityCompat.requestPermissions(act, permissionArray, PERMISSION_REQ_CODE);
    }

    public static boolean checkIfDenied(Activity act, String permission){
        return ActivityCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_DENIED;
    }
    public static boolean checkIfGranted(Activity act, String permission){
        return ContextCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean checkIfGranted(Activity act, String ... permissions){
        for(String permission : permissions){
            boolean granted = ContextCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_GRANTED;
            if(!granted) return false;
        }
        return true;
    }

    public void requirePermissions(Fragment fra, String ... permissions){
        fra.requestPermissions(permissions, PERMISSION_REQ_CODE);
    }
}
