package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hx.widget.adapterview.VhBase;

/**
 * Created by rose on 16-8-12.
 *
 * Based on item position as the viewType
 *
 * the subclass check the raw type of mData to initiate viewholder as well as mData bind.
 *
 */

public abstract class Ap2SelectBase<D> extends ApBase<VhBase<D>, D> {

    public static final int SELECT_MODE_SINGLE = 1;
    public static final int SELECT_MODE_MULTI = 2;

    private int mSelectMode = SELECT_MODE_MULTI;
    private List<D> mSelectedData = null;
    private Set<Integer> mSelectedPos = null;

    public Ap2SelectBase(Activity act, RecyclerView _rv_) {
        super(act, _rv_);
        mSelectedData = new ArrayList<>();
        mSelectedPos = new ArraySet<>();
    }

    protected abstract int getViewType(int position, Object data);
    @SuppressWarnings("unchecked")
    protected abstract VhBase<D> getVh(Activity act, int viewType);

    @NonNull
    @Override
    public VhBase<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getVh(mAct, viewType);
    }

    @Override
    public VhBase<D> getVh(Activity act) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = null;
        try {
            data = getData() == null || getData().isEmpty() ? null : getData().get(position);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            data = null;
        }
        return getViewType(position, data);
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

