package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hx.widget.adapterview.VhBase;

/**
 * Created by RoseHongXin on 2018/11/20 0020.
 */

public abstract class ApMultiSelectBase<Vh extends VhBase<D>, D> extends ApBase<Vh, D> {

    private List<D> mSelectedData = null;
    private Set<Integer> mSelectedPos = null;

    public ApMultiSelectBase(Activity act, RecyclerView _rv_) {
        super(act, _rv_);
        mSelectedData = new ArrayList<>();
        mSelectedPos = new ArraySet<>();
    }

    public void click(D dto, int position){
        if(mSelectedPos.contains(position)){
            mSelectedData.remove(dto);
            mSelectedPos.remove(position);
            notifyItemChanged(position);
            return;
        }
        mSelectedData.add(dto);
        mSelectedPos.add(position);
        notifyItemChanged(position);
    }
    public boolean selected(D dto, int position){
        return mSelectedPos.contains(position) && mSelectedData.contains(dto);
    }
    public List<D> selected(){
        return mSelectedData;
    }

    public void putSelected(D dto, int position){
        mSelectedPos.add(position);
        mSelectedData.add(dto);
    }
    public void removeSelected(D dto, int position){
        mSelectedPos.remove(position);
        mSelectedData.remove(dto);
    }
}
