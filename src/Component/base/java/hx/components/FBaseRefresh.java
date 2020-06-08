package hx.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import butterknife.ButterKnife;
import hx.kit.refresh.P2rlLoader;
import hx.lib.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import io.reactivex.Observable;

/**
 * Created by RoseHongXin on 2017/11/1 0001.
 */

public abstract class FBaseRefresh extends FBase {

    private PtrClassicFrameLayout _p2rl_;
    private NestedScrollView _nsv_container;

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
                if(getApi() != null){
                    API_REQUEST(getApi().doOnTerminate(() -> refreshIdle()));
                }else {
                    FBaseRefresh.this.onRefresh();
                }
            }
        }
        .target(_nsv_container)
        .anchor(_p2rl_)
        .host(this)
        .create();
    }

    @Override
    public void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 100);
    }
    protected void refreshIdle(){
        _p2rl_.post(() -> _p2rl_.refreshComplete());
    }
    protected void onRefresh(){}
    protected Observable getApi(){
        return null;
    }

}