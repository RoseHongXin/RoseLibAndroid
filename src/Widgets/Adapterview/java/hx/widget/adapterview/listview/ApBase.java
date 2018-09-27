package hx.widget.adapterview.listview;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hx.widget.adapterview.IItemClickListener;
import hx.widget.adapterview.VhBase;

/**
 * Created by Rose on 4/6/2017.
 */

public abstract class ApBase<Vh extends VhBase<T>, T> extends BaseAdapter {

    final String TAG = "--ApBase:ListView--";

    private List<T> mDatas = new ArrayList<T>();
    protected Activity mAct;
    protected ListView _lv;
    protected IItemClickListener<T> mClickListener;

    public abstract Vh getVh(Activity act);
    @CallSuper
    protected void bind(Vh holder, T data, int position){
        holder.bind(data, position);
    }
    protected void  registerItemClick(IItemClickListener<T> clickListener){
        this.mClickListener = clickListener;
    }

    public ApBase(Activity act, ListView lv){
        this.mAct = act;
        this._lv = lv;
        lv.setAdapter(this);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vh holder;
        if (convertView == null) {
            holder = getVh(mAct);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else holder = (Vh) convertView.getTag();
        holder.bind(mDatas.get(position), position);
        return holder.itemView;
    }

    public void setData(List<T> datas){
        if(mDatas == null) mDatas = new ArrayList<T>();
        else mDatas.clear();
        mDatas = new ArrayList<>();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addData(List<T> datas){
        if(mDatas == null) mDatas = new ArrayList<T>();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
    public List<T> getData(){
        return mDatas;
    }
}
