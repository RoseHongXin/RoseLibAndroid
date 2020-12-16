/**
 *
 */
package rose.android.jlib.kit.async;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

public class Toaster extends Handler {

	private static Context mCtx;
	private static Toaster _sInstance;

	private Toaster(){ }

	public static void init(Context ctx) {
		mCtx = ctx;
		_sInstance = new Toaster();
	}

	public static Toaster instance(){
		return _sInstance;
	}

	public static void show(int resId) {
		if (mCtx == null || 0 == resId) return;
		show(mCtx.getString(resId));
	}

	public static void show(String str) {
		if(mCtx == null || TextUtils.isEmpty(str)) return;
		Toast.makeText(mCtx, str, Toast.LENGTH_SHORT).show();
	}
}
