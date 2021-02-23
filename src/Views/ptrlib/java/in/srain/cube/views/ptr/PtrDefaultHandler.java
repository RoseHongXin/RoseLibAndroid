package in.srain.cube.views.ptr;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public abstract class PtrDefaultHandler implements PtrHandler {

    private static boolean canChildScrollingUp(View view) {
        if(view instanceof ViewPager){
            ViewPager _vp_ = ((ViewPager) view);
            ViewGroup _vg_ = (ViewGroup) _vp_.getChildAt(0);
            return _vg_.canScrollVertically(-1);
        }
        return view.canScrollVertically(-1);
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollingUp(content);
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollingUp(content) && header != null;
    }

}