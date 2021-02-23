package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.cncoderx.wheelview.Wheel3DView;
import rose.android.jlib.R;

import java.util.ArrayList;
import java.util.List;

public class DItemBUConfirm extends DlgBase {

    private TextView _tv_title;
    private Wheel3DView _whv_items;
    private Button _bt_confirm;
    private Object mTitle = null;
    private Callback mCb;
    private CharSequence[] mTexts;
    private Drawable[] mIcons;
    private int[] mValues;
    private String mDefValue;
    private TextView _tv_anchor;
    private TextFormatCallback mTextFormatCallback;
    private Object mBtnText;
    private boolean mShow4Select = false;
    private int mSelectedIdx = -1;

    public static DItemBUConfirm obtain(Activity act) {
        DItemBUConfirm dlg = new DItemBUConfirm();
        dlg.selfHost(act);
        return dlg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(mShow4Select){
            String positiveText = DialogHelper.getText(mAct, mBtnText);
            if(TextUtils.isEmpty(positiveText)) positiveText = getString(R.string.HX_confirm);
            CharSequence[] texts = getTextItems();
            int defIdx = getDefItemIndex(texts);
            AlertDialog.Builder builder = new AlertDialog.Builder(mAct)
                    .setNegativeButton(R.string.HX_cancel, null)
                    .setPositiveButton(positiveText, (dialog, which) -> {
                        if(mSelectedIdx != -1){
                            String text = mTexts.length > mSelectedIdx && mTexts[mSelectedIdx] != null ? mTexts[mSelectedIdx].toString() : "";
                            onConfirmClick(mSelectedIdx, text);
                        }
                        dialog.dismiss();
                    })
                    .setSingleChoiceItems(texts, defIdx, (dialog, which) -> {
                        mSelectedIdx = which;
                    });
            String title = DialogHelper.getText(mAct, mTitle);
            if(!TextUtils.isEmpty(title)) builder.setTitle(title);
            AlertDialog dialog = builder.create();
            DialogHelper.BtnFlat(dialog, null);
            return dialog;
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected int sGetLayout() {
        return R.layout.d_item_bu_confirm;
    }

    @Override
    protected void onDlgWindowAppend(Window window) {
        super.onDlgWindowAppend(window);
        if(mShow4Select){
            if(window != null) window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.windowBackground)));
            return;
        }else{
            if(window != null) window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mShow4Select){

        }else {
            _show(view);
        }
    }

    private void _show(View view){
        _tv_title = view.findViewById(R.id._tv_title);
        _whv_items = (Wheel3DView) view.findViewById(R.id._whv_items);
        _bt_confirm = view.findViewById(R.id._bt_confirm);
        if(mTexts == null){
            _whv_items.setEntries();
            return;
        }
        CharSequence[] texts = getTextItems();
        _whv_items.setEntries(texts);
        int defIdx = getDefItemIndex(texts);
        if(defIdx != -1){
            _whv_items.setCurrentIndex(defIdx);
        }
        int itemCount = mTexts.length;
        if(itemCount <= 3) _whv_items.setVisibleItems(itemCount + 1);

        String btnText = DialogHelper.getText(mAct, mBtnText);
        if(!TextUtils.isEmpty(btnText)) _bt_confirm.setText(btnText);
        _bt_confirm.setOnClickListener(v -> {
            int idx = _whv_items.getCurrentIndex();
            String text = _whv_items.getCurrentItem().toString();
            onConfirmClick(idx, text);
            dismiss();
        });
        if(mTitle != null){
            _tv_title.setVisibility(View.VISIBLE);
            if(mTitle instanceof String) _tv_title.setText((String)mTitle);
            else if(mTitle instanceof Integer) _tv_title.setText((Integer) mTitle);
            else _tv_title.setVisibility(View.GONE);
        }
    }

    private CharSequence[] getTextItems(){
        if(mTextFormatCallback == null) {
            return mTexts;
        }else{
            CharSequence[] formattedTexts = new CharSequence[mTexts.length];
            for(int i = 0; i < mTexts.length; i++){
                CharSequence text = mTextFormatCallback.onFormat(mTexts[i]);
                formattedTexts[i] = text;
            }
            return formattedTexts;
        }
    }
    private int getDefItemIndex(CharSequence[] texts){
        if(mDefValue != null) {
            for (int i = 0; i < texts.length; i++) {
                if (TextUtils.equals(texts[i], mDefValue)) {
                    return i;
                }
            }
        }
        return -1;
    }
    private void onConfirmClick(int index, String text){
        if(_tv_anchor != null) {
            _tv_anchor.setText(text);
            _tv_anchor.setTag(index);
        }
        if(mCb != null) {
            if(mValues == null) mCb.onSelect(index, text);
            else mCb.onSelect(index < mValues.length ? mValues[index] : 0, text);
        }
    }

    public DItemBUConfirm callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DItemBUConfirm btn(Object text){
        mBtnText = text;
        return this;
    }
    public DItemBUConfirm title(Object title){
        mTitle = title;
        return this;
    }
    public DItemBUConfirm texts(List<String> texts){
        String[] chars = new String[texts.size()];
        for(int i = 0; i < texts.size(); i++) chars[i] = texts.get(i);
        return texts(chars);
    }
    public DItemBUConfirm texts(String[] texts){
        this.mTexts = texts;
        return this;
    }
    public DItemBUConfirm icons(List<Drawable> icons){
        Drawable[] drawables = new Drawable[icons.size()];
        for(int i = 0; i < icons.size(); i++) drawables[i] = icons.get(i);
        return icons(drawables);
    }
    public DItemBUConfirm icons(Drawable ... icons){
        this.mIcons = icons;
        return this;
    }
    public DItemBUConfirm icons(Context ctx, @DrawableRes int ... icons){
        List<Drawable> drawables = new ArrayList<>();
        for(int i : icons) drawables.add(ctx.getResources().getDrawable(i));
        return icons(drawables);
    }
    public DItemBUConfirm textFormat(TextFormatCallback callback){
        this.mTextFormatCallback = callback;
        return this;
    }
    public DItemBUConfirm defaultValue(String value){
        mDefValue = value;
        return this;
    }
    public DItemBUConfirm anchor(TextView _tv_anchor){
        this._tv_anchor = _tv_anchor;
        return this;
    }
    public DItemBUConfirm values(int[] values){
        this.mValues = values;
        return this;
    }
    public DItemBUConfirm range(int[] texts, String format){
        mTexts = new String[texts[1] - texts[0] + 1];
        mValues = new int[mTexts.length];
        for(int i = 0; i < mTexts.length; i++) {
            int v = texts[0] + i;
            mValues[i] = v;
            mTexts[i] = String.format(format, v);
        }
        return this;
    }
    public DItemBUConfirm range(int from, int to, String format) {
        mTexts = new String[to - from + 1];
        mValues = new int[mTexts.length];
        int idx = 0;
        for(int i = from; i <= to; i++){
            mTexts[idx] = String.format(format, i);
            mValues[idx] = i;
            idx++;
        }
        return this;
    }

    public DItemBUConfirm show(){
        super.selfShow("DItemBUConfirm");
        return this;
    }

    public DItemBUConfirm show4select(){
        mShow4Select = true;
        super.selfShow("DItemBUConfirm4Select");
        return this;
    }

    public interface Callback{
        void onSelect(int value, String text);
    }
    public interface TextFormatCallback{
        CharSequence onFormat(CharSequence chars);
    }
}
