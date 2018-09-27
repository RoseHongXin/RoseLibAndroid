package hx.request;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by RoseHongXin on 2017/10/30 0030.
 */

public abstract class Pull2RefreshLoader {

    private final static int TIME_UI_REFRESH_DELAY = 200;

    protected PtrClassicFrameLayout _ptrl_;
    private ViewGroup _vg_container;
    private Activity mAct;


    public Pull2RefreshLoader act(Activity act){
        this.mAct = act;
        return this;
    }
    public Pull2RefreshLoader content(ViewGroup _vg_container){
        this._vg_container = _vg_container;
        return this;
    }
    public Pull2RefreshLoader target(PtrClassicFrameLayout _ptrl_){
        this._ptrl_ = _ptrl_;
        return this;
    }
    public Pull2RefreshLoader create(){
        initPtrRefresh();
        return this;
    }

    public void refreshIdle(){
        if(_ptrl_ != null)  _ptrl_.postDelayed(() -> _ptrl_.refreshComplete(), TIME_UI_REFRESH_DELAY);
    }
    public void refresh(){
        _ptrl_.postDelayed(() -> _ptrl_.autoRefresh(), TIME_UI_REFRESH_DELAY);
    }

    private void initPtrRefresh() {
        _ptrl_.setLastUpdateTimeRelateObject(mAct);
        _ptrl_.setMode(PtrFrameLayout.Mode.REFRESH);
        _ptrl_.setPtrHandler(new PtrDefaultHandler() {
            @Override public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, _vg_container, header);
            }
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }
        });

    }

    public abstract void onRefresh();

}
