package hx.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import hx.lib.R;
import hx.widget.VpConfig;
import hx.widget.VpNoSwipe;

/**
 * Created by Rose on 2017/4/24.
 */

@Deprecated
public abstract class ABaseTabFra extends AppCompatActivity {

    CoordinatorLayout _cl_;
    AppBarLayout _abl_;
    CollapsingToolbarLayout _ctb_;
    TabLayout _tl_;
    VpNoSwipe _vp_;

    public abstract List<Fragment> sGetFras();
    public abstract void sLoadTabs(TabLayout _tl_);
    public abstract void sSetAppBar(AppBarLayout _abl_, CollapsingToolbarLayout _ctb_);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_base_tab_fra);
        init();
    }
    private void init(){
        _cl_ = (CoordinatorLayout)findViewById(R.id._cl_);
        _abl_ = (AppBarLayout)findViewById(R.id._abl_);
        _ctb_ = (CollapsingToolbarLayout)findViewById(R.id._ctb_);
        _tl_ = (TabLayout)findViewById(R.id._tl_);
        _vp_ = (VpNoSwipe)findViewById(R.id._vp_);

        sSetAppBar(_abl_, _ctb_);

        List<Fragment> fras = sGetFras();
        VpConfig.stateAdapter(_vp_, fras, getSupportFragmentManager());
        _tl_.setupWithViewPager(_vp_, true);
        sLoadTabs(_tl_);

    }
}
