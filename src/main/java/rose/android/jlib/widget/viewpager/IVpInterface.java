package rose.android.jlib.widget.viewpager;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by rose on 16-8-23.
 */

public abstract class IVpInterface{

    public abstract <I extends IVpInterface> I view(ViewPager _vp_);
//    public abstract <I extends IVpInterface> I objects(List<Object> objects);
    public abstract <I extends IVpInterface> I objects(Object ... objects);
    public abstract <I extends IVpInterface> I manager(FragmentManager fraMgr);
    public abstract <I extends IVpInterface> I offScreenSize(int size);
    public abstract <I extends IVpInterface> I pageCount(int count);
    public abstract <I extends IVpInterface> I create();

    protected abstract void initiate(int pageCount);
    protected abstract void onPageShow(int idx);
    protected abstract Object getObject(int idx);
    protected abstract List<Object> getObjects();

}
