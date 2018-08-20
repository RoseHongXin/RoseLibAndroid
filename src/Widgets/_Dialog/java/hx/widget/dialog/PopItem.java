package hx.widget.dialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hx.kit.data.UnitConverter;
import hx.kit.log.Log4Android;
import hx.lib.R;

/**
 * Created by RoseHongXin on 2017/7/28 0028.
 */

public class PopItem {

    private Activity mAct;
    private View _v_anchor;
    private int mWidth = -1;
    private int mHeight = -1;
    private ListPopupWindow mPopup;
    private Callback mCb;
    private String[] mTexts;
    private boolean mFillAfterSelect = true;

    public static PopItem builder(){
        return new PopItem();
    }

    public PopItem create(){
        mPopup = new ListPopupWindow(mAct);
        mPopup.setAnchorView(_v_anchor);
        mPopup.setWidth(mWidth == -1 ? ListPopupWindow.WRAP_CONTENT : mWidth);
        mPopup.setHeight(mHeight == -1 ? ListPopupWindow.WRAP_CONTENT : mHeight);
        mPopup.setAdapter(new BaseAdapter() {
            @Override public int getCount() {
                return mTexts.length;
            }
            @Override public Object getItem(int position) {
                return mTexts[position];
            }
            @Override public long getItemId(int position) {
                return position;
            }
            @Override public View getView(int position, View convertView, ViewGroup parent) {
                VhItem holder;
                if(convertView == null) {
                    TextView _tv = new TextView(mAct);
                    _tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAct.getResources().getDimension(R.dimen.txt_content));
                    _tv.setGravity(Gravity.CENTER);
                    _tv.setPadding(0, 16, 0, 16);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    _tv.setLayoutParams(layoutParams);
                    holder = new VhItem(_tv);
                    holder.itemView.setOnClickListener(view -> {
                        String text = mTexts[position];
                        if (mCb != null) {
                            if (mTexts.length == getCount()) mCb.onClick(text, mTexts[position], position);
                            else mCb.onClick(text, text, position);
                        }
                        if (mFillAfterSelect && _v_anchor instanceof TextView) ((TextView)_v_anchor).setText(text);
                        mPopup.dismiss();
                    });
                    convertView = holder.itemView;
                    convertView.setTag(holder);
                }else{
                    holder = (VhItem)convertView.getTag();
                }
                holder.bind(mTexts[position]);
                return convertView;
            }
        });
        return this;
    }

    public PopItem act(Activity act){
        this.mAct = act;
        return this;
    }
    public PopItem anchor(View _v_anchor){
        this._v_anchor = _v_anchor;
        return this;
    }
    public PopItem texts(String[] texts){
        this.mTexts = texts;
        return this;
    }
    public PopItem texts(List<String> texts){
        this.mTexts = new String[texts.size()];
        for(int i = 0; i < texts.size(); i++) mTexts[i] = texts.get(i);
        return this;
    }
    public PopItem width(int width){
        this.mWidth = width;
        return this;
    }
    public PopItem height(int height){
        this.mHeight = height;
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

    public void show(){
        mPopup.show();
    }

    private class VhItem extends RecyclerView.ViewHolder{
        TextView _tv;
        VhItem(View itemView) {
            super(itemView);
            _tv = (TextView)itemView;
        }
        void bind(String text){
            _tv.setText(text);
        }
    }

    public interface Callback{
        void onClick(String text, String value, int position);
    }

}
