package hx.components;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import hx.kit.refresh.Pull2RefreshLayoutLoader;
import hx.lib.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by rose on 16-8-12.
 */
public abstract class ABaseRefresh<D> extends ABase implements IRefresh<D> {

    protected PtrClassicFrameLayout _p2rl_;
    private ViewGroup mLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        ViewGroup container = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.a_base_refresh, (ViewGroup) getWindow().getDecorView(), true);
        mLayout = (ViewGroup) LayoutInflater.from(this).inflate(layoutResID, container, true);
        setContentView(mLayout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _p2rl_ = findViewById(R.id._p2rl_);
        _p2rl_.setMode(PtrFrameLayout.Mode.REFRESH);
        new Pull2RefreshLayoutLoader() {
            @Override public void onRefresh() {
                getApi()
                        .doOnComplete(() -> _p2rl_.refreshComplete())
                        .subscribe(data -> onBind(data));
            }
        }
        .act(this)
        .target(_p2rl_)
        .content(mLayout)
        .create();
    }
}
