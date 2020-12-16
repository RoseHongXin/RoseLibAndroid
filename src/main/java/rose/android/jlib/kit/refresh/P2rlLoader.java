package rose.android.jlib.kit.refresh;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by RoseHongXin on 2017/10/30 0030.
 */

public abstract class P2rlLoader {

    private final static int TIME_UI_REFRESH_DELAY = 200;

    private PtrFrameLayout _p2rl_;
    private ViewGroup _vg_target;
    private Activity mAct;
    private Fragment mFra;
    private PtrFrameLayout.Mode mMode = null;
    private @ColorRes int mTxtColor = -1;

    public P2rlLoader(){}

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
    public P2rlLoader color(@ColorRes int color){
        this.mTxtColor = color;
        return this;
    }
    public P2rlLoader create(){
        initPtrRefresh();
        return this;
    }


    private void initPtrRefresh() {
        if(_p2rl_ instanceof PtrClassicFrameLayout && mTxtColor != -1){
            ((PtrClassicFrameLayout)_p2rl_).textColor(_p2rl_.getResources().getColor(mTxtColor));
        }
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

    public void refreshIdle(){
        if(_p2rl_ != null)  _p2rl_.postDelayed(() -> _p2rl_.refreshComplete(), TIME_UI_REFRESH_DELAY);
    }
    public void idle(){
        _p2rl_.refreshComplete();
    }
    public void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), TIME_UI_REFRESH_DELAY);
    }

    protected abstract void onRefresh();

}
