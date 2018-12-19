package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hx.widget.adapterview.VhBase;

/**
 * Created by rose on 16-8-12.
 */

public abstract class ApBase<Vh extends VhBase<D>, D> extends RecyclerView.Adapter<Vh> {

    final String TAG = "--ApBase--";

    protected List<D> mData = new ArrayList<D>();
    public Activity mAct;
    public RecyclerView _rv_;
    protected RecyclerView.LayoutManager mLayoutMgr;

    public abstract Vh getVh(Activity act);
    @CallSuper protected void bind(Vh holder, D data, int position){
        holder.bind(data, position);
    }

     public ApBase(Activity act, RecyclerView _rv_){
        this.mAct = act;
        this._rv_ = _rv_;
         mLayoutMgr = new LinearLayoutManager(mAct);
         ((LinearLayoutManager)mLayoutMgr).setOrientation(LinearLayoutManager.VERTICAL);
         this._rv_.setLayoutManager(mLayoutMgr);
         this._rv_.setAdapter(this);
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Vh holder = getVh(mAct);
//        viewType 未做额外处理，此处只作为position用。
//        if(mClickListener != null) holder.itemView.setOnClickListener(view -> mClickListener.onClick(mData.get(viewType), viewType));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        bind(holder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<D> data){
        if(mData == null) mData = new ArrayList<D>();
        else mData.clear();
        if(data != null) mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<D> data){
        if(mData == null) mData = new ArrayList<D>();
        if(data != null) mData.addAll(data);
        notifyDataSetChanged();
    }
    public void addData(D data){
        if(mData == null) mData = new ArrayList<D>();
        if(data != null) {
            mData.add(data);
            notifyItemInserted(getItemCount());
            notifyItemRangeInserted(getItemCount(), 1);
        }
    }
    public List<D> getData(){
        return mData;
    }
    public D getData(int position){
        D data = null;
        try {
            data = getData().get(position);
        }catch (Exception e){

        }
        return data;
    }

    public void remove(int position){
        try {
            if (mData != null && mData.size() > position) mData.remove(position);
            notifyItemRemoved(position);
            if(mData != null && position != mData.size()){ // 如果移除的是最后一个，忽略
                notifyItemRangeChanged(position, mData.size() - position);
            }
        }catch (Exception e){}
    }

    public void clear(){
        if(mData != null) mData.clear();
        notifyDataSetChanged();
    }

}
