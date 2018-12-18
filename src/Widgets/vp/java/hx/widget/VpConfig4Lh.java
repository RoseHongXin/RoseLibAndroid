package hx.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hx.kit.view.LhBase;

/**
 * Created by rose on 16-8-23.
 */

class VpConfig4Lh extends IVpInterface{

    private ViewPager _vp_;
    private TabLayout _tl_;
    private List<LhBase> mLhBases;
    private List<Object> mLhBasesBak;
    private int mOffScreenSize;
    private int mPageCount;
    private int mCurrentPagePosition;
    private boolean mInitiated = false;

    private IVpInterface mVpInterface;

    public VpConfig4Lh(IVpInterface iVpInterface){
        mVpInterface = iVpInterface;
    }

    public VpConfig4Lh view(ViewPager _vp_){
        this._vp_ = _vp_;
        return this;
    }

    private VpConfig4Lh objects(List<Object> objects) {
        this.mLhBases = new ArrayList<>();
        for(Object obj : objects){
            mLhBases.add((LhBase)obj);
        }
        this.mLhBasesBak = new ArrayList<>(objects);
        return this;
    }
    public VpConfig4Lh objects(Object ... fras){
        return objects(Arrays.asList(fras));
    }
    public VpConfig4Lh manager(FragmentManager fraMgr){
        return this;
    }
    public VpConfig4Lh offScreenSize(int size){
        this.mOffScreenSize = size;
        return this;
    }public VpConfig4Lh pageCount(int count){
        this.mPageCount = count;
        if(mInitiated){
            if(_vp_.getAdapter() != null)_vp_.getAdapter().notifyDataSetChanged();
            initiate(count);
        }
        return this;
    }

    public VpConfig4Lh create(){
        _create();
        return this;
    }

    private void _create(){
        if(mOffScreenSize == 0) mOffScreenSize = 1;
        if(mPageCount == 0) mPageCount = mLhBases.size();
        _vp_.setAdapter(new PagerAdapter() {
            @Override public int getCount() {return mLhBases.size();}
            @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {return view == object;}
            @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View layout = mLhBases.get(position).itemView;
                container.addView(layout);
                return layout;
            }
            @Override public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {container.removeView(((View) object));}
        });
        _vp_.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageSelected(int position) {
                mCurrentPagePosition = position;
                onPageShow(mCurrentPagePosition);
            }
            @Override public void onPageScrollStateChanged(int state) { }
        });
        _vp_.setOffscreenPageLimit(mOffScreenSize);
        mCurrentPagePosition = 0;
        initiate(mPageCount);
        onPageShow(mCurrentPagePosition);
    }

    @Override
    protected void initiate(int pageCount){
        mInitiated = true;
        mVpInterface.initiate(pageCount);
    }
    @Override
    protected void onPageShow(int idx){
        mVpInterface.onPageShow(idx);
    }

    protected Object getObject(int idx){
        if(mLhBases == null || idx < 0 || idx > mLhBases.size()) return null;
        return mLhBases.get(idx);
    }
    protected List<Object> getObjects(){
        return mLhBasesBak;
    }

}
