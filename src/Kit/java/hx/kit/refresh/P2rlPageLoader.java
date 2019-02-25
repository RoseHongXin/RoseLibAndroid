package hx.kit.refresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
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

@SuppressLint("CheckResult")
public abstract class P2rlPageLoader<Ap extends ApBase<Vh, D>, Vh extends VhBase<D>, D> {

    private final static int LAZY_INIT_REFRESH_DELAY = 200;

    protected PtrFrameLayout _p2rl_;
    private ViewGroup _vg_target;
    private Activity mAct;
    private Fragment mFra;
    private Ap mAdapter;
    private PtrFrameLayout.Mode mMode = null;

    private int mPageIdx = 0;


    public P2rlPageLoader host(Activity act){
        this.mAct = act;
        return this;
    }
    public P2rlPageLoader host(Fragment fra){
        this.mFra = fra;
        return this;
    }
    public P2rlPageLoader target(ViewGroup _vg_target){
        this._vg_target = _vg_target;
        return this;
    }
    public <P extends PtrFrameLayout> P2rlPageLoader anchor(P _p2rl_){
        this._p2rl_ = _p2rl_;
        return this;
    }
    public P2rlPageLoader mode(PtrFrameLayout.Mode mode){
        if(_p2rl_ != null) _p2rl_.setMode(mode);
        this.mMode = mode;
        return this;
    }
    public P2rlPageLoader create(){
        mAdapter = adapter();
        initPtrRefresh();
        refresh();
        return this;
    }

    private void initPtrRefresh() {
        if(_p2rl_ instanceof PtrClassicFrameLayout) ((PtrClassicFrameLayout) _p2rl_).setLastUpdateTimeRelateObject(mAct == null ? (mFra == null ? new Object() : mFra) : mAct);
        _p2rl_.setMode(mMode == null ? PtrFrameLayout.Mode.BOTH : mMode);
        _p2rl_.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                Observable<List<D>> observable = request(mPageIdx + 1);
                if(observable != null) {
                    observable
                            .map(datas -> {
                                if (!datas.isEmpty()) {
                                    mPageIdx++;
                                    mAdapter.addData(datas);
                                }
                                return datas;
                            })
                            .doOnComplete(() -> refreshIdle())
                            .doOnError(throwable -> {
                                throwable.printStackTrace();
                                refreshIdle();
                            })
                            .subscribe();
                }
            }
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIdx = 0;
                Observable<List<D>> observable = request(mPageIdx);
                if(observable != null) {
                    observable
                            .map(datas -> {
                                mAdapter.setData(datas);
                                return datas;
                            })
                            .doOnComplete(() -> refreshIdle())
                            .doOnError(throwable -> {
                                throwable.printStackTrace();
                                refreshIdle();
                            })
                            .subscribe();
                }
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, _vg_target, header);
            }
        });

    }

    public P2rlPageLoader refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), LAZY_INIT_REFRESH_DELAY);
        return this;
    }
    public P2rlPageLoader refreshIdle(){
        _p2rl_.postDelayed(() -> _p2rl_.refreshComplete(), LAZY_INIT_REFRESH_DELAY);
        return this;
    }

    public abstract Observable<List<D>> request(int pageIdx);
    public abstract Ap adapter();

}
