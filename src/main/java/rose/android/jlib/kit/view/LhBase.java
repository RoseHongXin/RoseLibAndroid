package rose.android.jlib.kit.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by RoseHongXin on 2017/11/17 0017.
 *
 */

public class LhBase<D> extends RecyclerView.ViewHolder{

    protected View mLayout;
    public Activity mAct;
    protected Fragment mFra;
    protected D mData;

    public LhBase(Activity act, ViewGroup _v_parent, @LayoutRes int layout){
        this(act, act.getLayoutInflater().inflate(layout, _v_parent, false));
    }

    public <F extends Fragment> LhBase(F fra, @IdRes int layoutIdRes){
        this(fra, fra.getActivity().findViewById(layoutIdRes));
    }
    public <F extends Fragment> LhBase(F fra, View layout){
        this(fra.getActivity(), layout);
        this.mFra = fra;
        this.mAct = fra.getActivity();
    }
    public LhBase(Activity act, @IdRes int layoutIdRes){
        this(act, act.findViewById(layoutIdRes));
    }
    public LhBase(Activity act){
        this(act, act.findViewById(android.R.id.content));
    }
    public LhBase(Activity act, View layout){
        this(layout);
        this.mAct = act;
    }
    private LhBase(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
        mLayout = itemView;
    }

    @CallSuper
    public void bind(D data){
        this.mData = data;
    }

    public D getData(){
        return mData;
    }

    private Context getContext(){
        return mAct == null ? mFra.getContext() : mAct;
    }

    protected Resources getResources(){
        return getContext().getResources();
    }
    protected String getString(@StringRes int res){
        return getContext().getString(res);
    }

}
