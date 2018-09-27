package hx.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rose on 16-8-23.
 */

public class VpConfig {

    private ViewPager _vp_;
    private TabLayout _tl_;
    private List<Fragment> mFras;
    private List<Fragment> mFrasBak;
    private FragmentManager mFraMgr;
    private int mOffScreenSize;
    private int mPageCount;
    private int mCurrentPagePosition;
    private boolean mInitiated = false;

    public VpConfig(){ }

    public VpConfig view(ViewPager _vp_){
        this._vp_ = _vp_;
        return this;
    }
    public <F extends Fragment> VpConfig fras(List<F> fras){
        this.mFras = new ArrayList<>(fras);
        this.mFrasBak = new ArrayList<>(mFras);
        return this;
    }
    public <F extends Fragment> VpConfig fras(F ... fras){
        return fras(Arrays.asList(fras));
    }
    public VpConfig manager(FragmentManager fraMgr){
        this.mFraMgr = fraMgr;
        return this;
    }
    public VpConfig offScreenSize(int size){
        this.mOffScreenSize = size;
        return this;
    }public VpConfig pageCount(int count){
        this.mPageCount = count;
        if(mInitiated){
            if(_vp_.getAdapter() != null)_vp_.getAdapter().notifyDataSetChanged();
            initiate(count);
        }
        return this;
    }

    public VpConfig create(){
        _create();
        return this;
    }

    private void _create(){
        if(mOffScreenSize == 0) mOffScreenSize = 1;
        if(mPageCount == 0) mPageCount = mFras.size();

        _vp_.setAdapter(new FragmentStatePagerAdapter(mFraMgr) {
            @Override
            public Fragment getItem(int position) {
                return mFras.get(position);
            }
            @Override
            public int getCount() {
                return mPageCount;
            }
            @NonNull
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment)super.instantiateItem(container,position);
                mFraMgr.beginTransaction().show(fragment).commit();
                return fragment;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                Fragment fragment = mFras.get(position);
                mFraMgr.beginTransaction().hide(fragment).commit();
            }
        });
        _vp_.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageSelected(int position) {
                mCurrentPagePosition = position;
            }
            @Override public void onPageScrollStateChanged(int state) { }
        });
        _vp_.setOffscreenPageLimit(mOffScreenSize);
        mCurrentPagePosition = 0;
        initiate(mPageCount);
    }

    protected void initiate(int pageCount){
        mInitiated = true;
    }
}
