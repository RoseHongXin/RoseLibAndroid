package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import hx.kit.log.Log4Android;
import hx.lib.R;


/**
 * Created by Rose on 3/2/2017.
 *
 * process waiting handle.
 * like request waiting, data process waiting.
 *
 * Dialog width & height must be set at onStart() life circle.
 *
 */

public class DLoading extends DialogFragment{

    private String TAG = "DlgLoading";

    private TextView _tv_hint;
    private ProgressBar _pb_loading;
    private boolean mCancelable;
    private boolean mToast = false;
    private String mHint;
    private Activity mAct;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, R.style.Dialog_Waiting);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = String.valueOf(this.getClass().hashCode());
        return inflater.inflate(R.layout.d_loading, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window;
//        if(mCancelable && (window = dialog.getWindow()) != null){
        if((window = dialog.getWindow()) != null){
            window.setDimAmount(0f);
            view.setBackgroundResource(R.drawable.sh_dialog_toast);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = (int)(displayMetrics.widthPixels * 0.618f + 0.5f);
            int height = (int)(width * 0.618f + 0.5f);
            window.setLayout(width, height);
        }
        dialog.setOnKeyListener((d, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                d.dismiss();
                DLoading.this.dismiss();
            }
            return true;
        });
        _tv_hint = (TextView) view.findViewById(R.id._tv_hint);
        _pb_loading = (ProgressBar) view.findViewById(R.id._pb_loading);

        if(!TextUtils.isEmpty(mHint)) {
            _tv_hint.setVisibility(View.VISIBLE);
            _tv_hint.setText(mHint);
        }
        setCancelable(mCancelable);
        if(mToast){
            _pb_loading.setVisibility(View.GONE);
            view.postDelayed(this::dismiss, 2000);
        }
    }

    public void hint(String hint){
        mHint = hint;
        if(getDialog() != null && getDialog().isShowing()){
            if(_tv_hint != null) _tv_hint.setText(mHint);
        }
    }
    public void hint(@StringRes int hint){
        mHint = mAct == null ? "" : mAct.getString(hint);
        if(_tv_hint == null) return;
        if(getDialog() != null && getDialog().isShowing()){
            if(_tv_hint != null) _tv_hint.setText(mHint);
        }
    }
    public String hint(){
        return _tv_hint != null && _tv_hint.getText() != null ? _tv_hint.getText().toString() : "";
    }

    public static DLoading create(@NonNull Activity act){
        return create(act, null);
    }
    public static DLoading create(@NonNull Activity act, Object hint){
        return create(act, hint, true);
    }

    public static DLoading force(@NonNull Activity act, Object hint){
        return create(act, hint, false);
    }
    public static DLoading force(@NonNull Activity act){
        return create(act, null, false);
    }
    public static DLoading create(@NonNull Activity act, Object hint, boolean cancelable){
        DLoading dlg = new DLoading();
        if (hint != null) {
            String text = "";
            if (hint instanceof Integer){
                text = act.getString((Integer) hint);
            }else if(hint instanceof String){
                text = (String) hint;
            }
            dlg.mHint = text;

        }
        dlg.mCancelable = cancelable;
        dlg.mAct = act;
        dlg.TAG = act.getClass().getSimpleName();
        return dlg;
    }

    public static DLoading show(@NonNull Activity act){
        return _show(act, null, true);
    }
    public static DLoading show(@NonNull Activity act, Object hint){
        return _show(act, hint, true);
    }
    public static DLoading showForce(@NonNull Activity act){
        return _show(act, null, false);
    }
    public static DLoading showForce(@NonNull Activity act, Object hint){
        return _show(act, hint, false);
    }
    public static DLoading showToast(@NonNull Activity act, Object hint){
        DLoading dlg = create(act, hint, true);
        dlg.mToast = true;
        dlg.show();
        return dlg;
    }

    private static DLoading _show(@NonNull Activity act, Object hint, boolean cancelable){
        DLoading dlg = create(act, hint, cancelable);
        dlg.show();
        return dlg;
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    /* Call this method after builder, carefully. */
    //预防Activity异常退出,或者切换太快,引起的getSupportFragmentManager() 为null.
    public DialogFragment show(){
        if(isShowing()) dismiss();
        try {
            if (mAct != null && !mAct.isFinishing() && !mAct.isDestroyed()){
                show(((AppCompatActivity) mAct).getSupportFragmentManager(), TAG);
            }
        }
        catch (Exception e){ Log4Android.w(this, "show exception: " + e.getMessage()); }
        return this;
    }

    @Override
    public void dismiss() {
        if(!isShowing()) return;
        try {
            Activity act = getActivity();
            if (act != null && !act.isFinishing() && !act.isDestroyed()){
//                    ((AppCompatActivity) mAct).getSupportFragmentManager().beginTransaction().hide(this).commit();
                super.dismissAllowingStateLoss();
            }
        }
        catch (Exception e){ Log4Android.w(this, "show exception: " + e.getMessage()); }
    }
}
