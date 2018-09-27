package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import hx.widget.adapterview.VhBase;

/**
 * Created by rose on 16-8-12.
 *
 * Based on item position as the viewType
 *
 * the subclass check the raw type of mData to initiate viewholder as well as mData bind.
 *
 */

public abstract class Ap2Base<T> extends ApBase<VhBase<T>, T> {

    public Ap2Base(Activity act, RecyclerView _rv_) {
        super(act, _rv_);
    }

    protected abstract int getViewType(int position, Object data);
    @SuppressWarnings("unchecked")
    protected abstract VhBase<T> getVh(Activity act, int viewType);

    @NonNull
    @Override
    public VhBase<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getVh(mAct, viewType);
    }

    @Override
    public VhBase<T> getVh(Activity act) {
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
}

