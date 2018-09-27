package hx.kit.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import hx.lib.R;


public class ClickHelper {

    private static long mLastClickTime = 0;
    private static int mClickCount = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }
    public static void fastDoubleClick(final View view) {
        view.setClickable(false);
        view.postDelayed(() -> view.setClickable(true), 500);
    }

    public static void finishPageWithConfirm(Activity act, DialogInterface.OnClickListener cb) {
        new AlertDialog.Builder(act)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    cb.onClick(dialogInterface, i);
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(R.string.HX_confirm_finish_current_page)
                .create()
                .show();
    }

    public static void editExitConfirm(Activity act) {
        new AlertDialog.Builder(act)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    act.finish();
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(R.string.HX_finish_while_editing_confirm)
                .create()
                .show();
    }

    public static void doubleClick(Activity act, String msg, View.OnClickListener cb){
        long cur = System.currentTimeMillis();
        if(mLastClickTime == 0 || cur - mLastClickTime < 3000) ++mClickCount;
        if(mClickCount == 2){
            cb.onClick(null);
            mClickCount = 0;
            mLastClickTime = 0;
            return;
        }
        mLastClickTime = cur;
        Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
    }

    public static void doubleClick2Home(Activity act) {
        doubleClick(act, act.getString(R.string.HX_click_again_to_home), v -> act.moveTaskToBack(true));
    }


}