package hx.components;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;

import static hx.components.IConstants.PAGE_DEFAULT_EXPIRE;
import static hx.components.IConstants.PAGE_NO_EXPIRE;


/**
 * Created by rose on 16-8-11.
 */

public abstract class FBase extends Fragment {

    int mExpireThreshold = PAGE_NO_EXPIRE;
    int mPageVisibleCount = 1;
    long mPageLastVisibleTime = 0;

    @LayoutRes public abstract int sGetLayoutRes();

    public void expire(int expire){
        if(mExpireThreshold <= 0) this.mExpireThreshold = PAGE_DEFAULT_EXPIRE;
        else this.mExpireThreshold = expire;
    }

    public void sSetText(TextView _tv, @StringRes int strRes, Object ... params){
        _tv.setText(sGetText(strRes, params));
    }
    public String sGetText(@StringRes int strRes, Object ... params){
        if(!isAdded() || getContext() == null) return "";
        if(params == null || params.length == 0){
            return getString(strRes);
        }else{
            return String.format(getString(strRes), params);
        }
    }
    public String[] sGetStrArray(@ArrayRes int arrayRes){
        if(!isAdded() || getContext() == null) return new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; //length = 16, 保证调用不会出现下标越界.
        return getContext().getResources().getStringArray(arrayRes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(sGetLayoutRes(), container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfNeedRefreshPage();
    }

    @Override
    public void setUserVisibleHint(boolean mIsVisibleToUser) {
        super.setUserVisibleHint(mIsVisibleToUser);
        checkIfNeedRefreshPage();
    }

    protected void checkIfNeedRefreshPage(){
        //解决fragment嵌套,父fragment的visible属性不能下放到子fragment的情况.
        boolean curFraVisible = getUserVisibleHint();
        boolean parentFraVisible = getParentFragment() == null || getParentFragment().getUserVisibleHint();
        boolean refresh = false;
        if(curFraVisible && parentFraVisible) {
            if(mExpireThreshold == PAGE_NO_EXPIRE) refresh = false;
            else if (mPageVisibleCount == 1) {
                mPageLastVisibleTime = System.currentTimeMillis();
                ++mPageVisibleCount;
                refresh = true;
            } else {
                long cur = System.currentTimeMillis();
                if((cur - mPageLastVisibleTime) > mExpireThreshold){
                    mPageVisibleCount = 0;
                    mPageLastVisibleTime = cur;
                    refresh = true;
                }
                ++mPageVisibleCount;
            }
        }
        if(refresh) refresh();
    }

    public void refresh(){};

}
