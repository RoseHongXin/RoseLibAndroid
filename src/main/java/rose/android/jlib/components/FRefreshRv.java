package rose.android.jlib.components;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

import rose.android.jlib.kit.refresh.P2rlPageLoader;
import hx.lib.R;
import rose.android.jlib.widget.adapterview.VhBase;
import rose.android.jlib.widget.adapterview.recyclerview.ApBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import io.reactivex.Observable;

/**
 * Created by rose on 16-8-11.
 */

public class FRefreshRv extends FBase {

    public PtrClassicFrameLayout _p2rl_;
    private RecyclerView _rv_;

    private P2rlPageLoader mLoader;
    private Callback mCb;

    public static FRefreshRv newInstance(Callback cb) {
        FRefreshRv fragment = new FRefreshRv();
        fragment.mCb = cb;
        return fragment;
    }

    @Override
    public int sGetLayout() {
        return R.layout.f_base_refresh_rv;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _p2rl_ = view.findViewById(R.id._p2rl_);
        _rv_ = view.findViewById(R.id._rv_);
        if(mCb != null) {
            ApBase adapter = mCb.getAdapter(_rv_);
            mLoader = new P2rlPageLoader() {
                @Override
                public Observable<List> request(int pageIdx) {
                    return mCb.getApi(pageIdx);
                }
                @Override
                public ApBase adapter() {
                    return adapter;
                }
            };
            mLoader.target(_rv_)
                    .anchor(_p2rl_)
                    .host(getActivity())
                    .create();
        }
    }

    @Override
    public void refresh(){
        _p2rl_.postDelayed(() -> _p2rl_.autoRefresh(), 100);
    }

    public interface Callback<Ap extends ApBase<Vh, D>, Vh extends VhBase<D>, D>{
        Observable<List<D>> getApi(int pageIdx);
        Ap getAdapter(RecyclerView _rv_);
    }

}
