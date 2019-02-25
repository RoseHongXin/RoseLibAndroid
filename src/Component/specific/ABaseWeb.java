package hx.components.specific;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;

import hx.components.ABase;
import hx.lib.R;

/**
 * Created by RoseHongXin on 2017/6/5 0005.
 */

public abstract class ABaseWeb extends ABase {

    WebView _wv_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_base_web);
        _wv_ = (WebView)findViewById(R.id._wv_);
        initWv(_wv_);
        _wv_.loadUrl(sGetUrl());
    }

    protected abstract String sGetUrl();
    protected void initWv(WebView _wv_){
        WvConfig.config(_wv_);
    }
}
