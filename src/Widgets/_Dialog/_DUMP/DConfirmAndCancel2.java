package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DConfirmAndCancel2 {

    private View mLayout;
    @LayoutRes private int mLayoutRes;
    private Activity mAct;

    private OnInitCallback mInitCb;
    private OnClickCallback mOnClickListener;
    private String mPositiveBtText;

    public static DConfirmAndCancel2 builder(){
        return new DConfirmAndCancel2();
    }
    public DConfirmAndCancel2 layout(@LayoutRes int layoutRes){
        this.mLayoutRes = layoutRes;
        return this;
    }

    public DConfirmAndCancel2 act(Activity act){
        this.mAct = act;
        return this;
    }
    public DConfirmAndCancel2 bt(String positiveBtText){
        this.mPositiveBtText = positiveBtText;
        return this;
    }
    public DConfirmAndCancel2 click(OnClickCallback listener){
        this.mOnClickListener = listener;
        return this;
    }
    public DConfirmAndCancel2 init(OnInitCallback cb){
        this.mInitCb = cb;
        return this;
    }

    public Dialog create(){
        mLayout = mAct.getLayoutInflater().inflate(mLayoutRes, null);
        if(mInitCb != null) mInitCb.onInit(DConfirmAndCancel2.this);
        AlertDialog dialog =  new AlertDialog.Builder(mAct)
                .setCancelable(false)
                .setView(mLayout)
                .setPositiveButton(TextUtils.isEmpty(mPositiveBtText) ? mAct.getString(R.string.HX_confirm) : mPositiveBtText, null)
                .setNegativeButton(R.string.HX_cancel, null)
                .create();
        Window window = dialog.getWindow();
        if(window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            mOnClickListener.onClick(DConfirmAndCancel2.this, dialog);
        }));
        return dialog;
    }

    public View view(@IdRes int idRes){
        return mLayout.findViewById(idRes);
    }

    public interface OnInitCallback {
        void onInit(DConfirmAndCancel2 handler);
    }
    public interface OnClickCallback{
        void onClick(DConfirmAndCancel2 handler, Dialog dialog);
    }

}
