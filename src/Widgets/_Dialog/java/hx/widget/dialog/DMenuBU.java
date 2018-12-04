package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hx.lib.R;
import hx.widget.adapterview.VhBase;
import hx.widget.adapterview.recyclerview.ApBase;

/**
 * Created by Rose on 3/2/2017.
 */

public class DMenuBU extends DialogFragment{

    private RecyclerView _rv_menus;
    private Callback mCb;
    private List<String> mTexts;
    private Activity mAct;
    private @ColorRes int mCancelBtnColor = -1;
    private @ColorRes int mItemTextColor = -1;

    public static DMenuBU obtain() {
        return new DMenuBU();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_menu_bu, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setWindowAnimations(R.style.dialog_menu_anim);
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
        _rv_menus = (RecyclerView) view.findViewById(R.id._rv_menus);
        ApItem adapter = new ApItem(mAct, _rv_menus);
        adapter.setData(mTexts);
        Button _bt_cancel = view.findViewById(R.id._bt_cancel);
        if(mCancelBtnColor != -1) _bt_cancel.setTextColor(getResources().getColor(mCancelBtnColor));
        _bt_cancel.setOnClickListener(v -> dismiss());
    }

    public DMenuBU host(Activity act){
        mAct = act;
        return this;
    }

    public DMenuBU callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DMenuBU texts(List<String> texts){
        this.mTexts = texts;
        return this;
    }
    public DMenuBU texts(String ... texts){
        List<String> list = new ArrayList<>();
        Collections.addAll(list, texts);
        return texts(list);
    }
    public DMenuBU colors(@ColorRes int ... colors){
        if(colors.length == 1) mCancelBtnColor = colors[0];
        else if(colors.length >= 2){
            mCancelBtnColor = colors[0];
            mItemTextColor = colors[1];
        }
        return this;
    }

    public DMenuBU show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DMenuBU");
        return this;
    }

    private class ApItem extends ApBase<VhItem, String>{
        ApItem(Activity act, RecyclerView rv) {
            super(act, rv);
        }
        @Override
        public VhItem getVh(Activity act) {
            return new VhItem(this, R.layout.ir_item_menu);
        }
    }

    private class VhItem extends VhBase<String>{
        View _v_menuItemDivider;
        TextView _tv_menuItem;
        <Ap extends ApBase> VhItem(Ap adapter, @LayoutRes int layoutRes) {
            super(adapter, layoutRes);
            _v_menuItemDivider = itemView.findViewById(R.id._v_menuItemDivider);
            _tv_menuItem = itemView.findViewById(R.id._tv_menuItem);
            _tv_menuItem.setOnClickListener(v -> {
                mCb.onSelect(position, data);
                dismiss();
            });
            if(mItemTextColor != -1) _tv_menuItem.setTextColor(getResources().getColor(mItemTextColor));
        }
        @Override
        public void bind(String data, int position) {
            super.bind(data, position);
            _v_menuItemDivider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            _tv_menuItem.setText(data);
        }
    }
    
    public interface Callback{
        void onSelect(int idx, String text);
    }
}
