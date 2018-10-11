package hx.kit.refresh;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by RoseHongXin on 2017/10/30 0030.
 */

public abstract class P2rlLoader {

    private PtrFrameLayout _p2rl_;
    private ViewGroup _vg_target;
    private Activity mAct;
    private Fragment mFra;
    private PtrFrameLayout.Mode mMode = null;

    public P2rlLoader host(Activity act){
        this.mAct = act;
        return this;
    }
    public P2rlLoader host(Fragment fra){
        this.mFra = fra;
        return this;
    }
    public P2rlLoader target(ViewGroup _vg_target){
        this._vg_target = _vg_target;
        return this;
    }
    public <P extends PtrFrameLayout> P2rlLoader anchor(P _p2rl_){
        this._p2rl_ = _p2rl_;
        return this;
    }
    public P2rlLoader mode(PtrFrameLayout.Mode mode){
        if(_p2rl_ != null) _p2rl_.setMode(mode);
        this.mMode = mode;
        return this;
    }
    public P2rlLoader create(){
        initPtrRefresh();
        return this;
    }


    private void initPtrRefresh() {
        _p2rl_.setMode(mMode == null ? PtrFrameLayout.Mode.REFRESH : mMode);
        if(_p2rl_ instanceof PtrClassicFrameLayout) ((PtrClassicFrameLayout) _p2rl_).setLastUpdateTimeRelateObject(mAct == null ? (mFra == null ? new Object() : mFra) : mAct);
        _p2rl_.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, _vg_target, header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }
        });

    }

    public void refresh(){
        if(_p2rl_ != null){
            _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 200);
        }
    }

    protected abstract void onRefresh();

}
