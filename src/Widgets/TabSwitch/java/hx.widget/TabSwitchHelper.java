package hx.widget;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by RoseHongXin on 2017/9/12 0012.
 */

public class TabSwitchHelper {

    private SparseArray<TextView> mSwitchs;
    private OnTabSwitchSelectListener mListener;
    private int mSelectedId;
    private int mDefaultSelectIdx;

    public TabSwitchHelper(){
        mSwitchs = new SparseArray<>();
        mSelectedId = -1;
        mDefaultSelectIdx = -1;
    }

    public TabSwitchHelper switchs(ViewGroup _vg){
        int count = _vg.getChildCount();
        for(int i = 0; i < count; i++){
            TextView tv = (TextView) _vg.getChildAt(i);
            mSwitchs.put(tv.getId(), tv);
        }
        return this;
    }
    public TabSwitchHelper switchs(TextView ... _tvs){
        for(TextView tv : _tvs){
            mSwitchs.put(tv.getId(), tv);
        }
        return this;
    }
    public TabSwitchHelper listener(OnTabSwitchSelectListener listener){
        this.mListener = listener;
        return this;
    }
    public TabSwitchHelper defaultSelectIdx(@IdRes int idx){
        this.mDefaultSelectIdx = idx;
        return this;
    }

    public TabSwitchHelper handle(){
        View.OnClickListener onClickListener = v -> {
            int id = v.getId();
            if(id == mSelectedId) return;
            if(mSwitchs.get(mSelectedId) != null) mSwitchs.get(mSelectedId).setSelected(false);
            TextView tv = mSwitchs.get(v.getId());
            tv.setSelected(true);
            mListener.onSelected(v.getId(), tv);
            mSelectedId = id;
        };
        for(int i = 0; i < mSwitchs.size(); i++){
            int id = mSwitchs.keyAt(i);
            mSwitchs.get(id).setOnClickListener(onClickListener);
        }
        if(mSwitchs.get(mDefaultSelectIdx) != null) mSwitchs.get(mDefaultSelectIdx).callOnClick();
        return this;
    }
}
