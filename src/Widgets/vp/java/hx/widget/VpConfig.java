package hx.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by rose on 16-8-23.
 */

public class VpConfig {

    public  static <T extends Fragment> void stateAdapter(ViewPager _vp_, List<T> fras, FragmentManager fm) {
        stateAdapter(_vp_, fras, fm, fras.size());
    }
    public static <T extends Fragment> void stateAdapter(ViewPager _vp_, List<T> fras, FragmentManager fm, int offScreenSize){
        _vp_.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return fras.get(position);
            }
            @Override
            public int getCount() {
                return fras.size();
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment)super.instantiateItem(container,position);
//                Fragment fragment = fras.get(position);
                fm.beginTransaction().show(fragment).commit();
                return fragment;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                Fragment fragment = fras.get(position);
                fm.beginTransaction().hide(fragment).commit();
            }
        });
        _vp_.setOffscreenPageLimit(offScreenSize);
    }
}
