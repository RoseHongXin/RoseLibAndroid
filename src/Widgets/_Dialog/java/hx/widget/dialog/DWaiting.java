package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class DWaiting extends DialogFragment{

    private String TAG = "DWaiting";

    private TextView _tv_hint;
    private ProgressBar _pb_roomInfo;
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
        return inflater.inflate(R.layout.d_waiting, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
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
                DWaiting.this.dismiss();
            }
            return true;
        });
        _tv_hint = (TextView) view.findViewById(R.id._tv_hint);
        _pb_roomInfo = (ProgressBar) view.findViewById(R.id._pb_roomInfo);

        if(!TextUtils.isEmpty(mHint)) {
            _tv_hint.setVisibility(View.VISIBLE);
            _tv_hint.setText(mHint);
        }
        setCancelable(mCancelable);
        if(mToast){
            _pb_roomInfo.setVisibility(View.GONE);
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

    public static DWaiting create(@NonNull Activity act){
        return create(act, null);
    }
    public static DWaiting create(@NonNull Activity act, String hint){
        return create(act, hint, true);
    }

    public static DWaiting force(@NonNull Activity act, String hint){
        return create(act, hint, false);
    }
    public static DWaiting force(@NonNull Activity act){
        return create(act, null, false);
    }
    public static DWaiting create(@NonNull Activity act, @StringRes int hint, boolean cancelable){
        return create(act, act.getString(hint), cancelable);
    }
    public static DWaiting create(@NonNull Activity act, String hint, boolean cancelable){
        DWaiting dWaiting = new DWaiting();
        dWaiting.mHint = hint;
        dWaiting.mCancelable = cancelable;
        dWaiting.mAct = act;
        return dWaiting;
    }

    public static DWaiting show(@NonNull Activity act){
        return _show(act, null, true);
    }
    public static DWaiting show(@NonNull Activity act, @StringRes int hint){
        return show(act, act.getString(hint));
    }
    public static DWaiting show(@NonNull Activity act, String hint){
        return _show(act, hint, true);
    }
    public static DWaiting showForce(@NonNull Activity act){
        return _show(act, null, false);
    }
    public static DWaiting showForce(@NonNull Activity act, @StringRes int hint){
        return show(act, act.getString(hint));
    }
    public static DWaiting showForce(@NonNull Activity act, String hint){
        return _show(act, hint, false);
    }
    public static DWaiting showToast(@NonNull Activity act, @StringRes int hint){
        return showToast(act, act.getString(hint));
    }
    public static DWaiting showToast(@NonNull Activity act, String hint){
        DWaiting dWaiting = create(act, hint, true);
        dWaiting.mToast = true;
        dWaiting.TAG = act.getClass().getSimpleName();
        dWaiting.show();
        return dWaiting;

//        if(host.isFinishing() || host.isDestroyed()) return;
//        DialogHelper.dlgButtonCenter(
//                new AlertDialog.Builder(host)
//                        .setTitle(hint)
//                        .setCancelable(false)
//                        .setNeutralButton(R.string.HX_confirm, null))
//                .show();
    }

    private static DWaiting _show(@NonNull Activity act, String hint, boolean cancelable){
        DWaiting dWaiting = new DWaiting();
        dWaiting.mAct = act;
        dWaiting.mHint = hint;
        dWaiting.mCancelable = cancelable;
        dWaiting.TAG = act.getClass().getSimpleName();
        dWaiting.show();
        return dWaiting;
    }

    public boolean isShowing() {
         return getDialog() != null && getDialog().isShowing();
    }

    /* Call this method after builder, carefully. */
    //预防Activity异常退出,或者切换太快,引起的getSupportFragmentManager() 为null.
    public DialogFragment show(){
        if(isShowing()) dismiss();
        try {
            Activity act = getActivity();
            if (act != null && !act.isFinishing() && !act.isDestroyed()){
                show(((AppCompatActivity) act).getSupportFragmentManager(), TAG);
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
                super.dismiss();
            }
        }
        catch (Exception e){ Log4Android.w(this, "show exception: " + e.getMessage()); }
    }
}
