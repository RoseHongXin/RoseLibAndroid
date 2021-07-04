package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import rose.android.jlib.R;
import rose.android.jlib.kit.view.ViewKit;

/**
 * Created by RoseHongXin on 2017/8/1 0001.
 */

public class DInput extends DlgBase {

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
    private boolean mFillAfterInput;
    private boolean mAutoDismiss = true;

    public static Builder builder(){
        return new Builder();
    }
    public static Builder builder(Activity act){
        return new Builder(act);
    }

    @Override
    protected int sGetLayout() {
        return R.layout.d_input;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPadding = 0;
        mTheme = R.style.Dialog_Input;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDlgWindowAppend(Window window) {
        super.onDlgWindowAppend(window);
        Dialog dialog = getDialog();
        if(dialog == null) { return; }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(dlg -> dismiss());
        dialog.setOnDismissListener(dlg -> ViewKit.hideInputMgr(_et_edit));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _l_title = view.findViewById(R.id._l_title);
        _tv_title = (TextView) view.findViewById(R.id._tv_title);
        _til_edit = (TextInputLayout)view.findViewById(R.id._til_edit);
        _et_edit = view.findViewById(R.id._et_edit);
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
            _bt_editConfirm.setText(android.R.string.ok);
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
        super.selfShow(TAG);
        return this;
    }

    public static class Builder{

        private final DInput mDialog;

        public Builder(){
            mDialog = new DInput();
        }
        public Builder(Activity act){
            mDialog = new DInput();
            mDialog.selfHost(act);
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
        public Builder bt(Object btTxt){
            mDialog.mBtTxt = btTxt;
            return this;
        }
        public Builder autoDismiss(boolean auto){
            mDialog.mAutoDismiss = auto;
            return this;
        }
        public Builder fraMgr(FragmentManager mgr){
            mDialog.selfMgr(mgr);
            return this;
        }
        public Builder anchor(TextView _tv_anchor){
            mDialog._tv_anchor = _tv_anchor;
            return this;
        }
        public Builder theme(@StyleRes int theme){
            mDialog.mTheme = theme;
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
