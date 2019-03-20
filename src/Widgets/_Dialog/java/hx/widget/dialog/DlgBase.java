package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import hx.kit.log.Log4Android;
import hx.lib.R;

/**
 * Created by RoseHongXin on 2017/8/1 0001.
 */

public abstract class DlgBase extends DialogFragment {

    protected  @StyleRes int mTheme = -1;
    protected int mPadding = 24;
    protected int mGravity = Gravity.BOTTOM;
    protected @ColorRes int mBgColor = android.R.color.transparent;
    protected @StyleRes int mAnimStyle = R.style.dialog_bottom_up;

    protected FragmentManager mFraManager;
    protected Activity mAct;

    protected abstract @LayoutRes int sGetLayout();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getContext() != null && mTheme != -1) getContext().setTheme(mTheme);
        Dialog dialog = getDialog();
        DialogHelper.padding(dialog, mGravity, mPadding, mPadding);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(mAnimStyle);
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(mBgColor)));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return inflater.inflate(sGetLayout(), container, true);
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


    public DlgBase host(Activity act){
        this.mAct = act;
        this.mFraManager = ((AppCompatActivity)act).getSupportFragmentManager();
        return this;
    }

    public DlgBase show(){
        if(mFraManager != null) {
            show(mFraManager, this.getClass().getSimpleName());
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
        catch (Exception e){ Log4Android.w(this, "show exception: " + e.getMessage()); }
    }

}
