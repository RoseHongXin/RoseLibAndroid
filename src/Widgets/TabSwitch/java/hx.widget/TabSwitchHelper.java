package hx.widget;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by RoseHongXin on 2017/9/12 0012.
 */

public class TabSwitchHelper {

    private SparseArray<TextView> mSwitchs;
    private int[] mSwitchIds;
    private OnTabSwitchSelectListener mListener;
    private int mSelectedId;
    private int mDefaultSelectIdx;
    private int mFraContainerId = -1;
    private Fragment[] mFras;
    private FragmentManager mFraMgr;

    public TabSwitchHelper(){
        mSwitchs = new SparseArray<>();
        mSelectedId = -1;
        mDefaultSelectIdx = -1;
    }

    private void setFocus4All(){
        for(int i = 0; i < mSwitchs.size(); i++){
            TextView _v_ = mSwitchs.valueAt(i);
            //PFLAG_FOCUSED 0x00000002
//            _v_.isFocused()
            _v_.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            _v_.setMarqueeRepeatLimit(-1);
            _v_.setSingleLine(true);
            _v_.setHorizontallyScrolling(true);
            _v_.setFocusable(true);
            try{
                Field privateFlagsField = _v_.getClass().getDeclaredField("mPrivateFlags");
                int privateFlags = privateFlagsField.getInt(_v_);
                privateFlagsField.setAccessible(true);
                privateFlagsField.setInt(_v_, privateFlags | 0x00000002);
                privateFlagsField.setAccessible(false);
            }catch (Exception e){
            }
            _v_.invalidate();
        }
    }

    public TabSwitchHelper switchs(ViewGroup _vg){
        int count = _vg.getChildCount();
        mSwitchIds = new int[count];
        for(int i = 0; i < count; i++){
            TextView tv = (TextView) _vg.getChildAt(i);
            mSwitchs.put(tv.getId(), tv);
            mSwitchIds[i] = tv.getId();
        }
        setFocus4All();
        return this;
    }
    public TabSwitchHelper switchs(TextView ... _tvs){
        mSwitchIds = new int[_tvs.length];
        for(int i = 0; i < _tvs.length; i++){
            TextView tv = _tvs[i];
            mSwitchs.put(tv.getId(), tv);
            mSwitchIds[i] = tv.getId();
        }
        setFocus4All();
        return this;
    }
    public TabSwitchHelper fras(@IdRes int id, FragmentManager fraMgr, Fragment ... fras){
        this.mFraContainerId = id;
        this.mFraMgr = fraMgr;
        this.mFras = fras;
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
            int idx = idx(id);
            if(mFraContainerId != -1 && mFraMgr != null && mFras != null && idx != -1 && idx < mFras.length){
                int formerIdx = idx(mSelectedId);
                Fragment formerFra = formerIdx >= 0 && formerIdx < mFras.length ? mFras[formerIdx] : null;
                Fragment fra = mFras[idx];
                FragmentTransaction transaction = mFraMgr.beginTransaction();
                if(formerFra != null) transaction.hide(formerFra);
                if(mFraMgr.findFragmentByTag(fra.getClass().getSimpleName()) == null){
                    transaction.add(mFraContainerId, fra, fra.getClass().getSimpleName());
                }
                transaction.show(fra).commit();
            }
            if(mListener != null) mListener.onSelected(v.getId(), idx, tv);
            mSelectedId = id;
        };
        for(int i = 0; i < mSwitchs.size(); i++){
            int id = mSwitchs.keyAt(i);
            mSwitchs.get(id).setOnClickListener(onClickListener);
        }
        if(mSwitchs.get(mDefaultSelectIdx) != null) mSwitchs.get(mDefaultSelectIdx).callOnClick();
        return this;
    }

    private int idx(@IdRes int id){
        for(int i = 0; i < mSwitchIds.length; i++){
            if(id == mSwitchIds[i]) {
                return i;
            }
        }
        return -1;
    }

    public void switch2Idx(int idx){
        if(mSwitchIds != null && mSwitchIds.length > idx && mSwitchs.get(mSwitchIds[idx]) != null){
            TextView _tv_ = mSwitchs.get(mSwitchIds[idx]);
            if(!_tv_.isSelected()) {
                for (int id : mSwitchIds) {
                    TextView _tv = mSwitchs.get(id);
                    if (_tv != null) {
                        _tv.setSelected(false);
                    }
                }
                _tv_.setSelected(true);
            }
        }
    }

    public void triggerSelectedCallback(){
        if(mSwitchs != null && mListener != null && mSwitchs.get(mSelectedId) != null) {
            mListener.onSelected(mSelectedId, idx(mSelectedId), mSwitchs.get(mSelectedId));
        }
    }
}
