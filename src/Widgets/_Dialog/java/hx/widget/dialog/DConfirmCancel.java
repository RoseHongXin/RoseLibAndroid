package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DConfirmCancel {

    private View mLayout;
    @LayoutRes
    private int mLayoutRes;
    private Activity mAct;

    private OnInitCallback mInitCb;
    private OnClickCallback mOnClickListener;
    private String mPositiveBtText;

    public static DConfirmCancel builder(){
        return new DConfirmCancel();
    }
    public DConfirmCancel layout(@LayoutRes int layoutRes){
        this.mLayoutRes = layoutRes;
        return this;
    }

    public DConfirmCancel act(Activity act){
        this.mAct = act;
        return this;
    }
    public DConfirmCancel bt(String positiveBtText){
        this.mPositiveBtText = positiveBtText;
        return this;
    }
    public DConfirmCancel click(OnClickCallback listener){
        this.mOnClickListener = listener;
        return this;
    }
    public DConfirmCancel init(OnInitCallback cb){
        this.mInitCb = cb;
        return this;
    }

    public Dialog create(){
        mLayout = mAct.getLayoutInflater().inflate(mLayoutRes, null);
        if(mInitCb != null) mInitCb.onInit(DConfirmCancel.this);
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
            mOnClickListener.onClick(DConfirmCancel.this, dialog);
        }));
        return dialog;
    }

    public View view(@IdRes int idRes){
        return mLayout.findViewById(idRes);
    }
    public String text(@IdRes int idRes){
        View _et_ = mLayout.findViewById(idRes);
        if(_et_ instanceof EditText && ((EditText) _et_).getText() != null){
            return ((EditText)_et_).getText().toString();
        }
        return "";
    }

    public interface OnInitCallback {
        void onInit(DConfirmCancel handler);
    }
    public interface OnClickCallback{
        void onClick(DConfirmCancel handler, Dialog dialog);
    }

}
