package hx.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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
    private String mText, mHint, mBtTxt, mTitle;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_input, container, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _l_title = view.findViewById(R.id._l_title);
        _tv_title = (TextView) view.findViewById(R.id._tv_title);
        _til_edit = (TextInputLayout)view.findViewById(R.id._til_edit);
        _et_edit = (TextInputEditText)view.findViewById(R.id._et_edit);
        _bt_editConfirm = (Button) view.findViewById(R.id._bt_editConfirm);
        _bt_editConfirm.setOnClickListener(v -> _bt_editConfirm());
        Dialog dialog = getDialog();
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        dialog.setOnDismissListener(dialog1 -> ViewKit.hideInputManager(_et_edit));
    }

    @Override
    public void onResume() {
        super.onResume();
        _l_title.setVisibility(TextUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
        _tv_title.setText(mTitle);
        _bt_editConfirm.setText(TextUtils.isEmpty(mBtTxt) ? getString(R.string.HX_confirm) : mBtTxt);
        if(mInputType != -1) _et_edit.setInputType(mInputType);
        if(!TextUtils.isEmpty(mText)) _et_edit.setText(mText);
        _et_edit.setHint(mHint);
        _et_edit.setSelection(_et_edit.getText().length());
        _et_edit.requestFocus();
    }

    public void _bt_editConfirm(){
        String text = _et_edit.getText().toString();
        if(!TextUtils.isEmpty(text)) {
            if(mFillAfterInput) _tv_anchor.setText(text);
            if(mCb != null) mCb.onConfirm(text, this);
            if(mAutoDismiss) dismiss();
        }
    }

    public DInput show(){
        show(mFraManager, TAG);
        return this;
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

        public Builder title(String title){
            mDialog.mTitle = title;
            return this;
        }
        public Builder text(String text){
            mDialog.mText = text;
            return this;
        }
        public Builder hint(String hint){
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
