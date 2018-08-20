package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cncoderx.wheelview.Wheel3DView;

import java.util.List;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DItemBUConfirm extends DialogFragment{

    private Wheel3DView _whv_items;
    private Callback mCb;
    private CharSequence[] mTexts;
    private Activity mAct;
    private TextView _tv_anchor;

    public static DItemBUConfirm obtain() {
        return new DItemBUConfirm();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_item_bu_confirm, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
        _whv_items = (Wheel3DView) view.findViewById(R.id._whv_items);
        _whv_items.setEntries(mTexts);
        int itemCount = mTexts.length;
        if(itemCount <= 3) _whv_items.setVisibleItems(itemCount + 1);
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int idx = _whv_items.getCurrentIndex();
            String text = _whv_items.getCurrentItem().toString();
            if(_tv_anchor != null) {
                _tv_anchor.setText(text);
                _tv_anchor.setTag(idx);
            }
            if(mCb != null) mCb.onSelect(idx, text);
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
    public DItemBUConfirm anchor(TextView _tv_anchor){
        this._tv_anchor = _tv_anchor;
        return this;
    }

    public DItemBUConfirm show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DItemBUConfirm");
        return this;
    }
    
    public interface Callback{
        void onSelect(int idx, String text);
    }
}
