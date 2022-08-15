package rose.android.jlib.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rose on 16-8-11.
 */

public abstract class FBase extends Fragment implements IRetrofitApi {

    private final static int PAGE_DEFAULT_EXPIRE = 3 * 60 * 1000;
    private final static int PAGE_NO_EXPIRE = -1;

    int mExpireThreshold = PAGE_NO_EXPIRE;
    int mPageLoadCnt = 1;
    long mPageLastLoadTime = 0;

    private boolean mPageLoaded = false;
    private View _v_layout;
    private List<Disposable> mDisposables = new ArrayList<>();

    @LayoutRes public abstract int sGetLayout();

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
        _v_layout = inflater.inflate(sGetLayout(), container, false);
        ButterKnife.bind(this, _v_layout);
        return _v_layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPageLoaded = true;
        checkIfNeedRefreshPage();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPageLoaded = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        checkIfNeedRefreshPage();
    }
//    @Override
//    public void setUserVisibleHint(boolean mIsVisibleToUser) {
//        super.setUserVisibleHint(mIsVisibleToUser);
//        checkIfNeedRefreshPage();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        API_DISPOSE();
    }

    protected void checkIfNeedRefreshPage(){
        if(mExpireThreshold == PAGE_NO_EXPIRE) { return; }
//        //解决fragment嵌套,父fragment的visible属性不能下放到子fragment的情况.
//        boolean curFraVisible = getUserVisibleHint();
//        boolean parentFraVisible = getParentFragment() == null || getParentFragment().getUserVisibleHint();
//        boolean curVisible = curFraVisible && parentFraVisible;
        boolean curVisible = mPageLoaded && isAdded();
        if(!curVisible) { return; }
        long currentTime = System.currentTimeMillis();
        mPageLoadCnt++;
        boolean refresh = false;
        if (mPageLoadCnt == 1) {
            refresh = true;
        } else if((currentTime - mPageLastLoadTime) > mExpireThreshold){
            mPageLoadCnt = 0;
            refresh = true;
        }
        mPageLastLoadTime = currentTime;
        if(!refresh) { return; }
        onPageReload();
        refresh();
    }
    protected void runOnMainThread(Runnable runnable){
        _v_layout.post(runnable);
    }
    protected boolean sIfIsFirstVisible(){
        boolean yes = mPageLoadCnt == 1;
        if(yes) { mPageLoadCnt++; }
        return yes;
    }
    @Override
    public void API_REQUEST(Observable observable){
        Disposable disposable = observable.subscribe();
        mDisposables.add(disposable);
    }
    @Override
    public void API_DISPOSE(){
        for(Disposable disposable : mDisposables){
            if(disposable != null && !disposable.isDisposed()) disposable.dispose();
        }
    }

    protected void onPageReload() {}
    @Deprecated
    public void refresh(){}

}
