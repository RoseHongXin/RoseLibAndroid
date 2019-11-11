package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.util.SparseArray;

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
    private SparseArray<D> mSelectedData = null;
    private Set<Integer> mSelectedPos = null;

    public ApSelectBase(Activity act, RecyclerView _rv_) {
        super(act, _rv_);
        mSelectedData = new SparseArray<>();
        mSelectedPos = new ArraySet<>();
    }

    public void click(D dto, int position){
        if(mSelectedPos.contains(position)){
            mSelectedData.remove(position);
            mSelectedPos.remove(position);
            notifyItemChanged(position);
            return;
        }
        if(mSelectMode == SELECT_MODE_SINGLE){
            if(!mSelectedPos.isEmpty()) {
                int lastPos = mSelectedPos.iterator().next();
                mSelectedData.remove(lastPos);
                mSelectedPos.remove(lastPos);
                notifyItemChanged(lastPos);
            }
        }
        mSelectedData.put(position, dto);
        mSelectedPos.add(position);
        notifyItemChanged(position);
    }
    public boolean selected(D dto, int position){
        return mSelectedPos.contains(position) && mSelectedData.get(position) != null;
    }
    public List<D> selected(){
        List<D> mTheSelectedData = new ArrayList<>();
        for(int i = 0; i < mSelectedData.size(); i++){
            int key = mSelectedData.keyAt(i);
            mTheSelectedData.add(mSelectedData.get(key));
        }
        return mTheSelectedData;
    }

    public D singleSelected(){
        return mSelectedPos.isEmpty() ? null : mSelectedData.get(mSelectedPos.iterator().next());
    }

    public void putSelected(D dto, int position){
        mSelectedPos.add(position);
        mSelectedData.put(position, dto);
    }
    public void removeSelected(D dto, int position){
        mSelectedPos.remove(position);
        mSelectedData.remove(position);
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

    @Override
    public void clear(){
        clearSelect();
        super.clear();
    }
}
