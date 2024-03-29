package rose.android.jlib.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import rose.android.jlib.R;
import rose.android.jlib.widget.adapterview.VhBase;
import rose.android.jlib.widget.adapterview.recyclerview.ApBase;

/**
 * Created by RoseHongXin on 2017/7/28 0028.
 */

public class PopItem extends PopupWindow {

    private Activity mAct;
    private RecyclerView _rv_;
    private FrameLayout _fr_layout;
    private int mShowGravity = Gravity.TOP | Gravity.END;

    private View _v_anchor;
    private ApBase<VhItem, String> mAdapter;
    private Callback mCb;
    private List<String> mTexts;
    private List<Integer> mIcons;
    private List<Object> mValues;
    private boolean mFillAfterSelect = true;

    public static PopItem builder(){
        return new PopItem();
    }

    public PopItem create(){
        _fr_layout = (FrameLayout) ((LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.p_item, null);
        initView();
        setPopup();
        mAdapter.setData(mTexts);
        return this;
    }

    private void initView() {
        _rv_ = (RecyclerView) _fr_layout.findViewById(R.id._rv_items);
        mAdapter = new ApBase<VhItem, String>(mAct, _rv_) {
            @Override
            public VhItem getVh(Activity act) {
                TextView _tv = new TextView(mAct);
                _tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAct.getResources().getDimension(R.dimen.txt_content));
                _tv.setGravity(Gravity.CENTER);
                _tv.setPadding(36, 20, 36, 20);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                _tv.setLayoutParams(layoutParams);
                return new VhItem(_tv);
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPopup() {
        this.setContentView(_fr_layout);
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
//        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.dialog_pop_am);

        _fr_layout.setOnTouchListener((v, event) -> {
            int[] location = new int[2];
            _rv_.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
//            int left = _rv_.getLeft();
//            int top = _rv_.getTop();
            int right = left + _rv_.getWidth();
            int bottom = top + _rv_.getHeight();
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (event.getAction() == MotionEvent.ACTION_UP && (x < left || y < top || x > right || y > bottom)) {
                dismiss();
            }
            return true;
        });
    }

    public PopItem act(Activity act){
        this.mAct = act;
        return this;
    }
    public PopItem anchor(View _v_anchor){
        this._v_anchor = _v_anchor;
        return this;
    }
    public PopItem texts(String ... texts){
        this.mTexts = Arrays.asList(texts);
        return this;
    }
    public PopItem texts(List<String> texts){
        this.mTexts = new ArrayList<>(texts);
        return this;
    }
    public PopItem icons(@DrawableRes Integer ... icons){
        this.mIcons = Arrays.asList(icons);
        return this;
    }
    public PopItem icons(List<Integer> icons){
        this.mIcons = new ArrayList<>(icons);
        return this;
    }
    public PopItem values(Object ... objects){
        this.mValues = Arrays.asList(objects);
        return this;
    }
    public PopItem values(List<Object> objects){
        this.mValues = new ArrayList<>(objects);
        return this;
    }
    public PopItem callback(Callback cb){
        mCb = cb;
        return this;
    }
    public PopItem fillAfterSelect(boolean fillAfterSelect){
        this.mFillAfterSelect = fillAfterSelect;
        return this;
    }
    public PopItem gravity(int gravity){ this.mShowGravity = gravity; return this; }

    public void show(){
        if(mAct == null || mAct.isFinishing() || mAct.isDestroyed()) { return; }
//        showAsDropDown(_v_anchor);
        showAsDropDown(_v_anchor, 0, 0, mShowGravity);
    }

    private class VhItem extends VhBase<String> implements View.OnClickListener {
        TextView _tv;
        VhItem(View itemView) {
            super(itemView);
            _tv = (TextView)itemView;
            _tv.setOnClickListener(this);
        }
        @Override
        public void bind(String data, int position) {
            super.bind(data, position);
            _tv.setText(data);
            if(hasIcon()) _tv.setCompoundDrawablesWithIntrinsicBounds(mIcons.get(position), 0, 0, 0);
        }
        @Override
        public void onClick(View v) {
            String text = data;
            Object value = mValues == null || position > mValues.size() ? text : mValues.get(position);
            if(mCb != null){
                mCb.onClick(text, value, position);
            }
            if(mFillAfterSelect && _v_anchor instanceof TextView) ((TextView) _v_anchor).setText(text);
            dismiss();
        }
        private boolean hasIcon(){
            return mIcons != null && position < mIcons.size();
        }
    }

    public interface Callback{
        void onClick(String text, Object value, int position);
    }

}
