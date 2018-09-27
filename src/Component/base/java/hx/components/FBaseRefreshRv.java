package hx.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import hx.kit.refresh.P2rlPageLoader;
import hx.lib.R;
import hx.widget.adapterview.VhBase;
import hx.widget.adapterview.recyclerview.ApBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import io.reactivex.Observable;

/**
 * Created by RoseHongXin on 2017/11/1 0001.
 *
 */

public class FBaseRefreshRv extends FBase {

    private PtrClassicFrameLayout _ptrl_;
    private RecyclerView _rv_;

    private Callback mCb;
    private P2rlPageLoader mLoader;

    public static FBaseRefreshRv newInstance(Callback cb) {
        FBaseRefreshRv fragment = new FBaseRefreshRv();
        fragment.mCb = cb;
        return fragment;
    }

    @Override
    public int sGetLayoutRes() {
        return R.layout.f_base_refresh_rv;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _ptrl_ = view.findViewById(R.id._ptrl_);
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
            mLoader.content(_rv_)
                    .target(_ptrl_)
                    .act(getActivity())
                    .create();
        }
    }

    @Override
    public void refresh(){
        _ptrl_.postDelayed(() -> _ptrl_.autoRefresh(), 100);
    }

    public interface Callback<Ap extends ApBase<Vh, D>, Vh extends VhBase<D>, D>{
        Observable<List<D>> getApi(int pageIdx);
        Ap getAdapter(RecyclerView _rv_);
    }
}
