package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import rose.android.jlib.R;

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
    private Object mPositiveBtText;
    private Object mTitle;

    public static DConfirmCancel builder(){
        return new DConfirmCancel();
    }
    public DConfirmCancel layout(@LayoutRes int layoutRes){
        this.mLayoutRes = layoutRes;
        return this;
    }

    public DConfirmCancel host(Activity act){
        this.mAct = act;
        return this;
    }
    public DConfirmCancel bt(Object positiveBtText){
        this.mPositiveBtText = positiveBtText;
        return this;
    }
    public DConfirmCancel title(Object title){
        this.mTitle = title;
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
        String btTxt = mAct.getString(R.string.HX_confirm);
        if(mPositiveBtText instanceof String) {
            btTxt = (String) mPositiveBtText;
        }else if (mPositiveBtText instanceof Integer){
            int resId = (int) mPositiveBtText;
            if(resId != -1) btTxt = mAct.getString(resId);
        }
        String title = "";
        if(mTitle instanceof String){ title = (String) mTitle; }
        else if(mTitle instanceof Integer){
            int resId = (int) mTitle;
            if(resId != -1) title = mAct.getString(resId);
        }
        AlertDialog dialog =  new AlertDialog.Builder(mAct)
                .setCancelable(false)
                .setTitle(title)
                .setView(mLayout)
                .setPositiveButton(btTxt, null)
                .setNegativeButton(R.string.HX_cancel, null)
                .create();
        Window window = dialog.getWindow();
        if(window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        DialogHelper.BtnFlat(dialog, dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                mOnClickListener.onClick(DConfirmCancel.this, dialog);
            });
        });
        return dialog;
    }

    public View layout(){
        return mLayout;
    }
    public <V extends View> V view(@IdRes int idRes){
        return (V)mLayout.findViewById(idRes);
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
