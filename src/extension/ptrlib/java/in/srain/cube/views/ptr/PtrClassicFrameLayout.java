package in.srain.cube.views.ptr;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader _l_header;
    private PtrClassicDefaultFooter _l_footer;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
        if(mTxtColor != 0){
            textColor(mTxtColor);
        }
    }

    private void initViews() {
        _l_header = new PtrClassicDefaultHeader(getContext());
        setHeaderView(_l_header);
        addPtrUIHandler(_l_header);
        _l_footer = new PtrClassicDefaultFooter(getContext());
        setFooterView(_l_footer);
        addPtrUIHandler(_l_footer);
    }

    public PtrClassicDefaultHeader getHeader() {
        return _l_header;
    }

    public void textColor(@ColorInt int color){
        _l_header._tv_ptr_headerTitle.setTextColor(color);
        _l_header._tv_ptr_headerLastUpdate.setTextColor(color);
        _l_footer._tv_ptr_footerTitle.setTextColor(color);
        _l_footer._tv_ptr_footerLastUpdate.setTextColor(color);
    }
    public void headerIcon(@DrawableRes int header){
        _l_header._iv_headerRotate.setImageResource(header);
    }
    public void icon(@DrawableRes int header, @DrawableRes int footer){
        _l_header._iv_headerRotate.setImageResource(header);
        _l_footer._iv_footerRotate.setImageResource(footer);
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        setLastUpdateTimeHeaderKey(key);
        setLastUpdateTimeFooterKey(key);
    }

    public void setLastUpdateTimeHeaderKey(String key) {
        if (_l_header != null) {
            _l_header.setLastUpdateTimeKey(key);
        }
    }

    public void setLastUpdateTimeFooterKey(String key) {
        if (_l_footer != null) {
            _l_footer.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeHeaderRelateObject(object);
        setLastUpdateTimeFooterRelateObject(object);
    }

    public void setLastUpdateTimeHeaderRelateObject(Object object) {
        if (_l_header != null) {
            _l_header.setLastUpdateTimeRelateObject(object);
        }
    }

    public void setLastUpdateTimeFooterRelateObject(Object object) {
        if (_l_footer != null) {
            _l_footer.setLastUpdateTimeRelateObject(object);
        }
    }
}
