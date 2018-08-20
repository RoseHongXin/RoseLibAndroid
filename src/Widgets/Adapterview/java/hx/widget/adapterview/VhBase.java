package hx.widget.adapterview;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import hx.widget.adapterview.recyclerview.ApBase;

/**
 * Created by rose on 16-9-9.
 */
public class VhBase<T>  extends RecyclerView.ViewHolder  {

    protected Activity mAct;
    protected T data;
    protected int position;
    protected ApBase mAdapter;
    protected RecyclerView _rv;

    public <Ap extends ApBase> VhBase(Ap adapter, @LayoutRes int layoutRes){
        this(adapter.mAct.getLayoutInflater().inflate(layoutRes, adapter._rv_, false));
        this.mAdapter = adapter;
        this.mAct = adapter.mAct;
        this._rv = adapter._rv_;
    }
    public VhBase(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @CallSuper
    public void bind(T data, int position){
        this.data = data;
        this.position = position;
    }

    public T getData(){
        return data;
    }
}
