package hx.kit.refresh;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hx.widget.adapterview.VhBase;
import hx.widget.adapterview.recyclerview.ApBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

/**
 * Created by RoseHongXin on 2017/10/30 0030.
 */

public abstract class P2rlPageLoader<Ap extends ApBase<Vh, D>, Vh extends VhBase<D>, D> {

    private final static int LAZY_INIT_REFRESH_DELAY = 100;

    protected PtrClassicFrameLayout _ptrl_;
    private ViewGroup _vg_container;
    private Activity mAct;
    private Ap mAdapter;
    private PtrFrameLayout.Mode mMode = null;

    private int mPageIdx = 0;


    public P2rlPageLoader act(Activity act){
        this.mAct = act;
        return this;
    }
    public P2rlPageLoader content(ViewGroup _vg_container){
        this._vg_container = _vg_container;
        return this;
    }
    public P2rlPageLoader target(PtrClassicFrameLayout _ptrl_){
        this._ptrl_ = _ptrl_;
        return this;
    }
    public P2rlPageLoader create(){
        mAdapter = adapter();
        initPtrRefresh();
        refresh();
        return this;
    }

    public P2rlPageLoader refresh(){
        _ptrl_.postDelayed(() -> _ptrl_.autoRefresh(), LAZY_INIT_REFRESH_DELAY);
        return this;
    }

    public P2rlPageLoader mode(PtrFrameLayout.Mode mode){
        if(_ptrl_ != null) _ptrl_.setMode(mode);
        this.mMode = mode;
        return this;
    }

    public void testMode(List<D> data){
        _ptrl_.postDelayed(() -> _ptrl_.refreshComplete(), LAZY_INIT_REFRESH_DELAY + 50);
        mAdapter.setData(data);
    }

    private void initPtrRefresh() {
        _ptrl_.setLastUpdateTimeRelateObject(mAct);
        _ptrl_.setMode(mMode == null ? PtrFrameLayout.Mode.BOTH : mMode);
        _ptrl_.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                Observable<List<D>> observable = request(mPageIdx + 1);
                if(observable != null) {
                    observable
                            .doOnComplete(() -> _ptrl_.refreshComplete())
                            .subscribe(datas -> {
                                if (!datas.isEmpty()) {
                                    mPageIdx++;
                                    mAdapter.addData(datas);
                                }
                            }, throwable -> {
                                _ptrl_.refreshComplete();
                            });
                }
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIdx = 0;
                Observable<List<D>> observable = request(mPageIdx);
                if(observable != null) {
                    observable
                            .doOnComplete(() -> _ptrl_.refreshComplete())
                            .subscribe(datas -> {
                                mAdapter.setData(datas);
                            }, throwable -> {
                                _ptrl_.refreshComplete();
                                mAdapter.setData(new ArrayList<>());
                            });
                }
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, _vg_container, header);
            }
        });
    }

    public abstract Observable<List<D>> request(int pageIdx);
    public abstract Ap adapter();

}
