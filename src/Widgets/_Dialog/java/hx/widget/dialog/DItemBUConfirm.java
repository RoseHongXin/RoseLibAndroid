package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cncoderx.wheelview.Wheel3DView;

import java.util.ArrayList;
import java.util.List;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DItemBUConfirm extends DialogFragment{

    private Wheel3DView _whv_items;
    private Callback mCb;
    private CharSequence[] mTexts;
    private Drawable[] mIcons;
    private int[] mValues;
    private String mDefValue;
    private Activity mAct;
    private TextView _tv_anchor;
    private TextFormatCallback mTextFormatCallback;

    public static DItemBUConfirm obtain() {
        return new DItemBUConfirm();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setWindowAnimations(R.style.dialog_bottom_up);
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
        }
        return inflater.inflate(R.layout.d_item_bu_confirm, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _whv_items = (Wheel3DView) view.findViewById(R.id._whv_items);
        if(mTextFormatCallback == null) {
            _whv_items.setEntries(mTexts);
        }else{
            CharSequence[] formattedTexts = new CharSequence[mTexts.length];
            for(int i = 0; i < mTexts.length; i++){
                CharSequence text = mTextFormatCallback.onFormat(mTexts[i]);
                formattedTexts[i] = text;
            }
            _whv_items.setEntries(formattedTexts);
        }
        int defIdx = -1;
        if(mDefValue != null){
            for(int i = 0; i < mTexts.length; i++){
                if(TextUtils.equals(mTexts[i], mDefValue)){
                    defIdx = i;
                    break;
                }
            }
        }
        if(defIdx != -1){
            _whv_items.setCurrentIndex(defIdx);
        }
        int itemCount = mTexts.length;
        if(itemCount <= 3) _whv_items.setVisibleItems(itemCount + 1);
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int idx = _whv_items.getCurrentIndex();
            String text = _whv_items.getCurrentItem().toString();
            if(_tv_anchor != null) {
                _tv_anchor.setText(text);
                _tv_anchor.setTag(idx);
            }
            if(mCb != null) {
                if(mValues == null) mCb.onSelect(idx, text);
                else mCb.onSelect(mValues[idx], text);
            }
            dismiss();
        });
    }

    public DItemBUConfirm host(Activity act){
        mAct = act;
        return this;
    }

    public DItemBUConfirm callback(Callback cb){
        mCb = cb;
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
        for(int i : icons) drawables.add(ctx.getDrawable(i));
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

    public DItemBUConfirm show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DItemBUConfirm");
        return this;
    }

    public interface Callback{
        void onSelect(int value, String text);
    }
    public interface TextFormatCallback{
        CharSequence onFormat(CharSequence chars);
    }
}
