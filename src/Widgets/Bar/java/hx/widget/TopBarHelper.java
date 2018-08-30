package hx.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hx.lib.R;

/**
 * Created by RoseHongXin on 2018/1/26 0026.
 */

public class TopBarHelper implements ITopBarOpt{

//    private Activity mAct;
    private Context mCtx;
    Toolbar _tb_;
    TextView _tv_tbTitle;
    ImageView _iv_tbRight;
    TextView _tv_tbRight;

    private TopBarHelper(Activity act){
//        mAct = host;
        mCtx = act;
        _tb_ = (Toolbar) act.findViewById(R.id._tb_);
        if(_tb_ != null && act instanceof AppCompatActivity){
            ((AppCompatActivity)act).setSupportActionBar(_tb_);
        }
        _tv_tbTitle = (TextView) act.findViewById(R.id._tv_tbTitle);
        _iv_tbRight = (ImageView) act.findViewById(R.id._iv_tbRight);
        _tv_tbRight = (TextView) act.findViewById(R.id._tv_tbRight);
    }
    private TopBarHelper(View layout){
        _tb_ = (Toolbar) layout.findViewById(R.id._tb_);
        _tv_tbTitle = (TextView) layout.findViewById(R.id._tv_tbTitle);
        _iv_tbRight = (ImageView) layout.findViewById(R.id._iv_tbRight);
        _tv_tbRight = (TextView) layout.findViewById(R.id._tv_tbRight);
        mCtx = _tb_.getContext();
    }

    public static TopBarHelper obtain(Object obj){
        if(obj instanceof Activity) return new TopBarHelper((Activity)obj);
        else return new TopBarHelper((View)obj);
    }

    @Override
    public void title(@StringRes int strRes){
        title(mCtx.getString(strRes));
    }
    @Override
    public void title(String title){
        if(_tv_tbTitle != null){
            _tv_tbTitle.setText(title);
        }
        if(_tb_ != null) _tb_.setTitle("");

    }

    @Override
    public String title() {
        String title = "";
        try {
            if (_tv_tbTitle != null) {
                title = _tv_tbTitle.getText().toString();
            }
            if (_tb_ != null) title = _tb_.getTitle().toString();
        }catch (Exception e){}
        return title;
    }

    @Override
    public void icon(@DrawableRes int iconRes, View.OnClickListener listener){
        if(_iv_tbRight != null){
            _iv_tbRight.setImageResource(iconRes);
            _iv_tbRight.setOnClickListener(listener);
        }

    }

    @Override
    public void icon(int iconRes) {
        if(_iv_tbRight != null && iconRes != 0){
            _iv_tbRight.setImageResource(iconRes);
            _tv_tbRight.setVisibility(View.GONE);
        }
    }

    @Override
    public void text(String text, View.OnClickListener listener){
        if(_tv_tbRight != null){
            _tv_tbRight.setText(text);
            _tv_tbRight.setOnClickListener(listener);
        }

    }
    @Override
    public void text(@StringRes int strRes, View.OnClickListener listener){
        text(mCtx.getString(strRes), listener);
    }

    @Override
    public void text(String text) {
        if(_tv_tbRight != null && !TextUtils.isEmpty(text)){
            _tv_tbRight.setText(text);
            _iv_tbRight.setVisibility(View.GONE);
        }
    }

    @Override
    public void text(int strRes) {
        text(mCtx.getString(strRes));
    }

    @Override
    public void right(View.OnClickListener listener) {
        if(_tv_tbRight != null && _tv_tbRight.getVisibility() == View.VISIBLE){
            _tv_tbRight.setOnClickListener(listener);
        }else if(_iv_tbRight != null && _iv_tbRight.getVisibility() == View.VISIBLE){
            _iv_tbRight.setOnClickListener(listener);
        }
    }

    @Override
    public void navigation(AppCompatActivity act){
        if(_tb_ != null){
            act.setSupportActionBar(_tb_);
            _tb_.setNavigationOnClickListener(v -> act.finish());
        }
    }
    @Override
    public void navigation(@DrawableRes int iconRes){
        if(_tb_ != null && iconRes != 0){
            _tb_.setNavigationIcon(iconRes);
        }
    }
    @Override
    public void navigation(@DrawableRes int iconRes, View.OnClickListener listener){
        //        if(_tb_ != null){
//            if(listener == null && host != null) {
//                ActionBar actionBar = host.getActionBar();
//                if(actionBar != null) {
//                    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
//                    actionBar.setDisplayHomeAsUpEnabled(true);
//                }
//            } else{
//                _tb_.setNavigationOnClickListener(listener);
//            }
//            _tb_.setNavigationIcon(iconRes);
//        }
        if(_tb_ != null){
            if(listener != null) _tb_.setNavigationOnClickListener(listener);
            if(iconRes != 0) _tb_.setNavigationIcon(iconRes);
        }
    }

    @Override
    public void navigation(View.OnClickListener listener) {
        if(_tb_ != null && listener != null){
            _tb_.setNavigationOnClickListener(listener);
        }
    }

    public void right(int visibility){
        _iv_tbRight.setVisibility(visibility);
        _tv_tbRight.setVisibility(visibility);
    }



}
