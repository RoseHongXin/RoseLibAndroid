package hx.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import hx.kit.refresh.P2rlPageLoader;
import hx.lib.R;
import hx.widget.adapterview.VhBase;
import hx.widget.adapterview.recyclerview.ApBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import io.reactivex.Observable;

/**
 * Created by RoseHongXin on 2017/11/1 0001.
 */

public abstract class ABaseRefreshRv<D, Vh extends VhBase<D>, Ap extends ApBase<Vh, D>> extends ABase {

    private PtrClassicFrameLayout _p2rl_;
    private RecyclerView _rv_;

    protected Ap mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_base_refresh_rv);
        _p2rl_ = findViewById(R.id._p2rl_);
        _rv_ = findViewById(R.id._rv_);
        mAdapter = getAdapter();
        new P2rlPageLoader<Ap, Vh, D>() {
            @Override
            public Observable<List<D>> request(int pageIdx) {
                return getApi(pageIdx);
            }
            @Override
            public Ap adapter() {
                return mAdapter;
            }
        }
        .content(_rv_)
        .target(_p2rl_)
        .act(this)
        .create();

    }

    protected void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 100);
    }

    protected abstract Observable<List<D>> getApi(int pageIdx);
    protected abstract Ap getAdapter();

}
