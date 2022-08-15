package rose.android.jlib.components;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

import rose.android.jlib.kit.refresh.P2rlPageLoader;
import rose.android.jlib.R;
import rose.android.jlib.widget.adapterview.VhBase;
import rose.android.jlib.widget.adapterview.recyclerview.ApBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import io.reactivex.rxjava3.core.Observable;

/**
 * Created by RoseHongXin on 2017/11/1 0001.
 *
 */

public abstract class FBaseRefreshRv<Ap extends ApBase<Vh, D>, Vh extends VhBase<D>, D> extends FBase {

    protected PtrClassicFrameLayout _p2rl_;
    private RecyclerView _rv_;
    protected boolean sRefreshAfterInitialized = true;
    protected P2rlPageLoader mP2rlLoader;

    @Override
    public int sGetLayout() {
        return R.layout.f_base_refresh_rv;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _p2rl_ = view.findViewById(R.id._p2rl_);
        _rv_ = view.findViewById(R.id._rv_);
        ApBase adapter = getAdapter(_rv_);
        mP2rlLoader = new P2rlPageLoader() {
            @Override
            public Observable<List<D>> request(int pageIdx) {
                return getApi(pageIdx);
            }
            @Override
            public ApBase adapter() {
                return adapter;
            }
        };
        mP2rlLoader.target(_rv_)
                .anchor(_p2rl_)
                .host(getActivity())
                .create();
        if(sRefreshAfterInitialized){ mP2rlLoader.refresh(); }
    }

    @Override
    public void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 100);
    }

    protected abstract Observable<List<D>> getApi(int pageIdx);
    protected abstract Ap getAdapter(RecyclerView _rv_);
}
