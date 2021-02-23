/**
 * 
 */
package hx.kit.async;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import hx.lib.R;

/**
 * @author Guo Bo
 * 
 */
public class Noter extends Handler {

	private static Context mCtx;

	public static void init(Context ctx) {
		mCtx = ctx;
	}

	public static void toast(int resId) {
		if (mCtx == null || 0 == resId) return;
		toast(mCtx.getString(resId));
	}

	public static void toast(String str) {
		if(mCtx == null || TextUtils.isEmpty(str)) return;
		Toast.makeText(mCtx, str, Toast.LENGTH_SHORT).show();
	}
}
