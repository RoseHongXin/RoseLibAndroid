package hx.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hx.kit.refresh.P2rlLoader;
import hx.lib.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by rose on 16-8-11.
 */

public abstract class FBaseRefresh extends FBase{

    protected PtrClassicFrameLayout _p2rl_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup base = (ViewGroup) inflater.inflate(R.layout.f_base_refresh, container, true);
        inflater.inflate(sGetLayoutRes(), base, true);
        return base;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup layout = (ViewGroup) LayoutInflater.from(getContext()).inflate(sGetLayoutRes(), (ViewGroup)view, true);
        _p2rl_ = view.findViewById(R.id._p2rl_);
        _p2rl_.setMode(PtrFrameLayout.Mode.REFRESH);
        new P2rlLoader() {
            @Override public void onRefresh() {
                refresh();
            }
        }
        .host(getActivity())
        .anchor(_p2rl_)
        .target(layout)
        .create();
    }

}
