package hx.widget;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 16-8-23.
 */

public class VpConfig extends IVpInterface{

    private IVpInterface mVpInterface;

    public VpConfig() {
        this(true);
    }
    public VpConfig(boolean vp4Fra) {
        if(vp4Fra) {
            mVpInterface = new VpConfig4Fra(this);
        }else {
            mVpInterface = new VpConfig4Lh(this);
        }
    }

    @Override
    public VpConfig view(ViewPager _vp_) {
        mVpInterface.view(_vp_);
        return this;
    }

    @Override
    public VpConfig objects(Object ... fras) {
        mVpInterface.objects(fras);
        return this;
    }

    @Override
    public VpConfig manager(FragmentManager fraMgr) {
        mVpInterface.manager(fraMgr);
        return this;
    }

    @Override
    public VpConfig offScreenSize(int size) {
        mVpInterface.offScreenSize(size);
        return this;
    }

    @Override
    public VpConfig pageCount(int count) {
        mVpInterface.pageCount(count);
        return this;
    }

    @Override
    public VpConfig create() {
        mVpInterface.create();
        return this;
    }

    @Override
    protected void initiate(int pageCount) {
    }

    @Override
    protected void onPageShow(int idx) {
    }

    @Override
    protected Object getObject(int idx) {
        mVpInterface.getObject(idx);
        return this;
    }

    @Override
    protected List<Object> getObjects() {
        return mVpInterface.getObjects();
    }

    public Fragment getFra(int idx){
        Object obj = mVpInterface.getObject(idx);
        if(obj instanceof Fragment) return (Fragment) obj;
        return null;
    }
    public List<Fragment> getFras(){
        List<Object> objects = mVpInterface.getObjects();
        List<Fragment> fras = new ArrayList<>();
        for(Object obj : objects){
            if(obj instanceof Fragment) fras.add((Fragment) obj);
        }
        return fras;
    }


}
