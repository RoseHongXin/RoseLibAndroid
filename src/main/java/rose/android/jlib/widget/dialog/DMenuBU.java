package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import hx.lib.R;
import rose.android.jlib.widget.adapterview.VhBase;
import rose.android.jlib.widget.adapterview.recyclerview.ApBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DMenuBU extends DlgBase {

    private RecyclerView _rv_menus;
    private Callback mCb;
    private List<String> mTexts;
    private @ColorRes int mCancelBtnColor = -1;
    private @ColorRes List<Integer> mItemTextColors;

    public static DMenuBU obtain(Activity act) {
        DMenuBU dlg = new DMenuBU();
        dlg.selfHost(act);
        return dlg;
    }

    public DMenuBU(){
        mItemTextColors = new ArrayList<>();
    }

    @Override
    protected int sGetLayout() {
        return R.layout.d_menu_bu;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, R.style.Dialog_Menu);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _rv_menus = (RecyclerView) view.findViewById(R.id._rv_menus);
        ApItem adapter = new ApItem(mAct, _rv_menus);
        adapter.setData(mTexts);
        Button _bt_cancel = view.findViewById(R.id._bt_cancel);
        if(mCancelBtnColor != -1) _bt_cancel.setTextColor(getResources().getColor(mCancelBtnColor));
        _bt_cancel.setOnClickListener(v -> dismiss());
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
            for(int i = 1; i < colors.length; i++) mItemTextColors.add(colors[i]);
        }
        return this;
    }

    public DMenuBU show(){
        super.selfShow("DMenuBU");
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
        }
        @Override
        public void bind(String data, int position) {
            super.bind(data, position);
            _v_menuItemDivider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            _tv_menuItem.setText(data);
            if(!mItemTextColors.isEmpty()) {
                @ColorRes int colorRes = position < mItemTextColors.size() ? mItemTextColors.get(position) : mItemTextColors.get(mItemTextColors.size() - 1);
                _tv_menuItem.setTextColor(getResources().getColor(colorRes));
            }
        }
    }

    public interface Callback{
        void onSelect(int idx, String text);
    }
}
