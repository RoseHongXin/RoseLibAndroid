package hx.kit.view;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

}
