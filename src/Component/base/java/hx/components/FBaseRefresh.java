package hx.components;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import hx.kit.refresh.P2rlLoader;
import hx.lib.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by RoseHongXin on 2017/11/1 0001.
 */

public abstract class FBaseRefresh extends Fragment {

    private PtrClassicFrameLayout _p2rl_;
    private NestedScrollView _nsv_container;

    protected abstract @LayoutRes int sGetLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup _v_layoutParent = (ViewGroup) getLayoutInflater().inflate(R.layout.f_base_refresh, container, false);
        getLayoutInflater().inflate(sGetLayout(), _v_layoutParent.findViewById(R.id._nsv_container), true);
        ButterKnife.bind(this, _v_layoutParent);
        return _v_layoutParent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _p2rl_ = view.findViewById(R.id._p2rl_);
        _nsv_container = view.findViewById(R.id._nsv_container);
        new P2rlLoader() {
            @Override
            protected void onRefresh() {
                FBaseRefresh.this.onRefresh();
            }
        }
        .target(_nsv_container)
        .anchor(_p2rl_)
        .host(this)
        .create();
    }

    public void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 100);
    }
    protected void refreshIdle(){
        _p2rl_.post(() -> _p2rl_.refreshComplete());
    }
    protected void onRefresh(){

    }

}