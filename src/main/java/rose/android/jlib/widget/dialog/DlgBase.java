package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import rose.android.jlib.R;
import rose.android.jlib.kit.log.Log4Android;


public abstract class DlgBase extends DialogFragment {

    protected int mStyle = STYLE_NO_TITLE;
    protected  @StyleRes int mTheme = 0;
    protected int mPadding = 24;
    protected int mGravity = Gravity.BOTTOM;
    protected @ColorRes int mBgColor = android.R.color.transparent;
    protected @StyleRes int mAnimStyle = R.style.dialog_bottom_up;

    protected FragmentManager mFraMgr;
    protected Activity mAct;
    protected View _v_layout;

    protected abstract @LayoutRes int sGetLayout();
    protected boolean onCustomDlgWindow(Window window) { return false; }
    protected void onDlgWindowAppend(Window window) { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(mStyle, mTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        DialogHelper.Padding(dialog, mGravity, mPadding, mPadding);
        Window window = dialog != null ? dialog.getWindow() : null;
        if (window != null && !onCustomDlgWindow(window)) {
            window.setWindowAnimations(mAnimStyle);
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(mBgColor)));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            onDlgWindowAppend(window);
        }
        _v_layout = inflater.inflate(sGetLayout(), container, true);
        return _v_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //    @Override
//    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
//        //super.show(manager, tag);
//        try {
//            Class c = Class.forName("android.support.v4.app.DialogFragment");
//            Constructor con = c.getConstructor();
//            Object obj = con.newInstance();
//            Field dismissed = c.getDeclaredField(" mDismissed");
//            dismissed.setAccessible(true);
//            dismissed.set(obj, false);
//            Field shownByMe = c.getDeclaredField("mShownByMe");
//            shownByMe.setAccessible(true);
//            shownByMe.set(obj, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        ft.commitAllowingStateLoss();
//    }

    public DlgBase selfMgr(FragmentManager mgr){
        this.mFraMgr = mgr;
        return this;
    }
    public DlgBase selfHost(Activity act){
        this.mAct = act;
        this.mFraMgr = ((AppCompatActivity)act).getSupportFragmentManager();
        return this;
    }

    public DlgBase selfShow(Object ... args){
        if(mFraMgr == null) { return this; }
        if(mAct == null || (!mAct.isDestroyed() && !mAct.isFinishing())) {
            try {
                String tag = this.getClass().getSimpleName();
                if(args.length > 0 && args[0] instanceof String){ tag = (String)args[0]; }
                show(mFraMgr, tag);
            }catch (Exception e){
                Log4Android.w(this, "show exception: " + e.getMessage());
            }
        }
        return this;
    }

    @Override
    public void dismiss() {
        try {
            if (getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()){
                super.dismiss();
            }
        }
        catch (Exception e){ Log4Android.w(this, "dismiss exception: " + e.getMessage()); }
    }

}
