package hx.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import hx.kit.log.Log4Android;
import hx.kit.view.ViewKit;
import hx.lib.R;
import hx.widget.dialog.DialogHelper;

/**
 * Created by RoseHongXin on 2017/8/1 0001.
 */

public class DInput extends DialogFragment{

    private final static String TAG = "DInput";

    private View _l_title;
    private TextView _tv_title;
    private TextInputLayout _til_edit;
    private TextInputEditText _et_edit;
    private Button _bt_editConfirm;

    private TextView _tv_anchor;
    private Callback mCb;
    private String mText;
    private Object mBtTxt;
    private Object mTitle;
    private Object mHint;
    private int mInputType = -1;
    private FragmentManager mFraManager;
    private boolean mFillAfterInput;
    private boolean mAutoDismiss = true;

    public static Builder builder(){
        return new Builder();
    }
    public static Builder builder(Activity act){
        return new Builder(act);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setWindowAnimations(R.style.dialog_bottom_up);
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.windowBackground)));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        dialog.setOnDismissListener(dialog1 -> ViewKit.hideInputMgr(_et_edit));
        return inflater.inflate(R.layout.d_input, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _l_title = view.findViewById(R.id._l_title);
        _tv_title = (TextView) view.findViewById(R.id._tv_title);
        _til_edit = (TextInputLayout)view.findViewById(R.id._til_edit);
        _et_edit = (TextInputEditText)view.findViewById(R.id._et_edit);
        _bt_editConfirm = (Button) view.findViewById(R.id._bt_editConfirm);
        _bt_editConfirm.setOnClickListener(v -> _bt_editConfirm());
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasTitle = mTitle != null && ((mTitle instanceof Integer && (int)mTitle != 0) || (mTitle instanceof String && !TextUtils.isEmpty((String)mTitle)));
        _l_title.setVisibility(hasTitle ? View.VISIBLE : View.GONE);
        if(hasTitle){
            if(mTitle instanceof Integer) _tv_title.setText((int)mTitle);
            if(mTitle instanceof String) _tv_title.setText((String)mTitle);
        }
        if(mBtTxt == null){
            _bt_editConfirm.setText(R.string.HX_confirm);
        }else {
            if(mBtTxt instanceof Integer) _bt_editConfirm.setText((int)mBtTxt);
            if(mBtTxt instanceof String) _bt_editConfirm.setText((String)mBtTxt);
        }
        if(mInputType != -1) _et_edit.setInputType(mInputType);
        if(!TextUtils.isEmpty(mText)) _et_edit.setText(mText);
        if(mHint != null) {
            if(mHint instanceof Integer) _et_edit.setHint((int)mHint);
            if(mHint instanceof String) _et_edit.setHint((String)mHint);
        }
        _et_edit.setSelection(_et_edit.getText() == null ? 0 : _et_edit.getText().length());
        _et_edit.requestFocus();
    }

    public void _bt_editConfirm(){
        String text = _et_edit.getText() == null ? "" : _et_edit.getText().toString();
        if(!TextUtils.isEmpty(text)) {
            if(mFillAfterInput && _tv_anchor != null) _tv_anchor.setText(text);
            if(mCb != null) mCb.onConfirm(text, this);
            if(mAutoDismiss) dismiss();
        }
    }

    public DInput show(){
        show(mFraManager, TAG);
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

    public static class Builder{

        private DInput mDialog;

        public Builder(){
            mDialog = new DInput();
        }
        public Builder(Activity act){
            mDialog = new DInput();
            mDialog.mFraManager = ((AppCompatActivity)act).getSupportFragmentManager();
        }

        public Builder title(Object title){
            mDialog.mTitle = title;
            return this;
        }
        public Builder text(String text){
            mDialog.mText = text;
            return this;
        }
        public Builder hint(Object hint){
            mDialog.mHint = hint;
            return this;
        }
        public Builder type(int type){
            mDialog.mInputType = type;
            return this;
        }
        public Builder callback(Callback cb){
            mDialog.mCb = cb;
            return this;
        }
        public Builder bt(String btTxt){
            mDialog.mBtTxt = btTxt;
            return this;
        }
        public Builder autoDismiss(boolean auto){
            mDialog.mAutoDismiss = auto;
            return this;
        }
        public Builder fraManager(FragmentManager fragmentManager){
            mDialog.mFraManager = fragmentManager;
            return this;
        }
        public Builder anchor(TextView _tv_anchor){
            mDialog._tv_anchor = _tv_anchor;
            return this;
        }
        public Builder fillAfterInput(boolean yes){
            mDialog.mFillAfterInput = yes;
            return this;
        }
        public DInput build(){
            return mDialog;
        }
    }

    public interface Callback {
        void onConfirm(String str, DInput dialog);
    }

}
