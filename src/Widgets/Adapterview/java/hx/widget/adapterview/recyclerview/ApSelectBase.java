package hx.widget.adapterview.recyclerview;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;
import hx.widget.adapterview.VhBase;

/**
 * Created by RoseHongXin on 2018/11/20 0020.
 */

public abstract class ApSelectBase<Vh extends VhBase<D>, D> extends ApBase<Vh, D> {

    public static final int SELECT_MODE_SINGLE = 1;
    public static final int SELECT_MODE_MULTI = 2;

    private int mSelectMode = SELECT_MODE_MULTI;
    private List<D> mSelectedData = null;
    private Set<Integer> mSelectedPos = null;

    public ApSelectBase(Activity act, RecyclerView _rv_) {
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
        if(mSelectMode == SELECT_MODE_SINGLE){
            if(!mSelectedPos.isEmpty()) {
                int lastPos = mSelectedPos.iterator().next();
                mSelectedData.remove(getData(lastPos));
                mSelectedPos.remove(lastPos);
                notifyItemChanged(lastPos);
            }
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

    public D singleSelected(){
        return mSelectedData.isEmpty() ? null : mSelectedData.get(0);
    }

    public void putSelected(D dto, int position){
        mSelectedPos.add(position);
        mSelectedData.add(dto);
    }
    public void removeSelected(D dto, int position){
        mSelectedPos.remove(position);
        mSelectedData.remove(dto);
    }

    public void selectMode(int mode){
        this.mSelectMode = mode;
    }

    public void clearSelect(){
        List<Integer> posBak = new ArrayList<>(mSelectedPos);
        mSelectedData.clear();
        mSelectedPos.clear();
        for(int pos : posBak) {
            notifyItemChanged(pos);
        }
    }
}
