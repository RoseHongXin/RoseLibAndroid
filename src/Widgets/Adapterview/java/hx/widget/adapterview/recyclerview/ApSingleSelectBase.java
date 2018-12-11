package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import hx.widget.adapterview.VhBase;

/**
 * Created by RoseHongXin on 2018/11/20 0020.
 */

public abstract class ApSingleSelectBase<Vh extends VhBase<D>, D> extends ApBase<Vh, D> {

    private D mSelectedData = null;
    private int mSelectedPos = -1;

    public ApSingleSelectBase(Activity act, RecyclerView _rv_) {
        super(act, _rv_);
    }

    public void click(D dto, int position){
        if(mSelectedPos == position){
            mSelectedData = null;
            mSelectedPos = -1;
            notifyItemChanged(position);
            return;
        }
        mSelectedData = dto;
        if(mSelectedPos != -1) notifyItemChanged(mSelectedPos);
        mSelectedPos = position;
        notifyItemChanged(position);
    }
    public boolean selected(D dto, int position){
        return mSelectedData == dto && mSelectedPos == position;
    }
    public D selected(){
        return mSelectedData;
    }

    public void putSelected(D dto, int position){
        if(!selected(dto, position)) click(dto, position);
    }
    public void removeSelected(D dto, int position){
        if(selected(dto, position)) click(dto, position);
    }
}
