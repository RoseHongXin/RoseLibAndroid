package hx.widget.adapterview.recyclerview;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import hx.widget.adapterview.VhBase;

/**
 * Created by rose on 16-8-12.
 */

public class RvLoader {

    private RecyclerView _rv;
    private ApBase mAdapter;
    private Activity mAct;


    private RvLoader(Activity act, RecyclerView _rv){
        this.mAct = act;
        this._rv = _rv;
    }

    public static RvLoader get(Activity act, RecyclerView _rv){
        return new RvLoader(act, _rv);
    }

    private RvLoader init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mAct);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        _rv.setLayoutManager(layoutManager);
        _rv.setAdapter(mAdapter);
        return this;
    }

    /*public  <Ap extends ApBase<Vh, T>, Vh extends VhBase<T> , T> RvLoader init(Activity mAct, RecyclerView _rv_, Ap adapter){
        this.mAct = mAct;
        this._rv_ = _rv_;
        this.mAdapter = adapter;
        return init();
    }*/

    public <Ap extends ApBase<Vh, T>, Vh extends VhBase<T> , T> RvLoader create(Ap adapter){
        this.mAdapter = adapter;
        return init();
    }
}
