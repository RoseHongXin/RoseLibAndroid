package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import hx.lib.R;

/**
 * 弹出提示经度框
 */
public class MProgress extends Dialog {

    public MProgress(Context ctx){
        super(ctx);
    }
    /*public MProgress(Activity mAct) {
        super(mAct);
    }*/

    public MProgress(Context ctx, int theme) {
        super(ctx, theme);
    }
    public MProgress(Activity act, int theme) {
        super(act, theme);
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView iv = (ImageView) findViewById(R.id._iv);
        AnimationDrawable spinner = (AnimationDrawable) iv.getBackground();
        spinner.start();
    }

    public static MProgress show(Activity act, String msg) {
        return show(act, msg, false, null);
    }

    public static MProgress show(Activity act, String msg, boolean cancelable){
        return show(act, msg, cancelable, null);
    }

    private static MProgress getProgress(Context act){
        MProgress mprogress = new MProgress(act, R.style.MProgress);
        //mprogress.setTitle("");
        mprogress.setContentView(R.layout.l_mprogress);

        mprogress.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = mprogress.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        mprogress.getWindow().setAttributes(lp);
        mprogress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return mprogress;
    }
    public static Dialog getDialog(Context ctx){
        return getProgress(ctx);
    }
    

    public static MProgress show(Activity act, String msg, boolean cancelable, OnCancelListener cancelListener) {
        MProgress mprogress = getProgress(act);
        if (TextUtils.isEmpty(msg)) {
            mprogress.findViewById(R.id._tv).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) mprogress.findViewById(R.id._tv);
            txt.setVisibility(View.VISIBLE);
            txt.setText(msg);
        }
        mprogress.setCancelable(cancelable);
        mprogress.setOnCancelListener(cancelListener);
        mprogress.show();
        return mprogress;
    }
}
