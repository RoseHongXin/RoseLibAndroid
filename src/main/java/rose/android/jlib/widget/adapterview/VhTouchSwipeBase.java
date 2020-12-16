package rose.android.jlib.widget.adapterview;

import rose.android.jlib.view.TouchSwipe2LeftLayout;

import rose.android.jlib.widget.adapterview.recyclerview.ApBase;

/**
 * Created by RoseHongXin on 2018/3/15 0015.
 */

public abstract class VhTouchSwipeBase<D> extends VhBase<D>{


    public VhTouchSwipeBase(ApBase adapter, int layoutRes) {
        super(adapter, layoutRes);
    }

    @Override
    public void bind(D data, int position) {
        super.bind(data, position);
        ((TouchSwipe2LeftLayout)itemView).reset();
    }
}
