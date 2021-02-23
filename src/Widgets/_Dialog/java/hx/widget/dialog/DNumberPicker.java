package hx.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cncoderx.wheelview.Wheel3DView;

import java.util.Calendar;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DNumberPicker extends DialogFragment{

    public static final int TYPE_DAY = 1;

    public static final int MODE_PICKER = 1;
    public static final int MODE_WHEEL = 2;


    private Wheel3DView _whv_items;
    private NumberPicker _np_;
    private Callback mCb;
    private NumberPicker.Formatter mTextFormatter;
    private int[] mRange;
    private int mDefNum;
    private Activity mAct;
    private TextView _tv_anchor;
    private int mMode = MODE_WHEEL;

    public static DNumberPicker obtain() {
        return new DNumberPicker();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.setCancelable(true);
        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.windowBackground)));
        }
        return inflater.inflate(R.layout.d_numberpicker, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _whv_items = (Wheel3DView)view.findViewById(R.id._whv_items);
        _np_ = (NumberPicker) view.findViewById(R.id._np_);
        if(mMode == MODE_WHEEL){
            _np_.setVisibility(View.GONE);
            _whv_items.setVisibility(View.VISIBLE);
            int num = mRange[0];
            int idx = 0;
            int max = mRange[1];
            int defNumIdx = 0;
            CharSequence[] texts = new CharSequence[(mRange[1] - mRange[0] + 1)];
            while (num <= max){
                texts[idx] = String.valueOf(num);
                if(num == mDefNum) defNumIdx = idx;
                idx++;
                num++;
            }
            _whv_items.setEntries(texts);
            if(defNumIdx != 0) _whv_items.setCurrentIndex(defNumIdx);
        }else{
            _whv_items.setVisibility(View.GONE);
            _np_.setVisibility(View.VISIBLE);
            _np_.setMinValue(mRange[0]);
            _np_.setMaxValue(mRange[1]);
            if(mDefNum != 0) _np_.setValue(mDefNum);
        }
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int number;
            if(mMode == MODE_WHEEL){
                number = Integer.parseInt(_whv_items.getCurrentItem().toString());
            }else{
                number = _np_.getValue();
            }
            if(_tv_anchor != null) {
                _tv_anchor.setText(mTextFormatter == null ? String.valueOf(number) : mTextFormatter.format(number));
                _tv_anchor.setTag(number);
            }
            if(mCb != null) mCb.onSelect(number);
            dismiss();
        });
    }

    public DNumberPicker host(Activity act){
        mAct = act;
        return this;
    }

    public DNumberPicker callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DNumberPicker type(int type){
        if(type == TYPE_DAY){
            mRange = new int[]{1, 28};
            Calendar calendar = Calendar.getInstance();
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            mDefNum = dayOfMonth > 28 ? 28 : dayOfMonth;
        }
        return this;
    }
    public DNumberPicker range(int[] range){
        this.mRange = range;
        return this;
    }
    public DNumberPicker defNum(int num){
        mDefNum = num;
        return this;
    }
    public DNumberPicker anchor(TextView _tv_anchor){
        return anchor(_tv_anchor, null);
    }
    public DNumberPicker anchor(TextView _tv_anchor, NumberPicker.Formatter formatter){
        this._tv_anchor = _tv_anchor;
        this.mTextFormatter = formatter;
        return this;
    }

    public DNumberPicker show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DNumberPicker");
        return this;
    }
    
    public interface Callback{
        void onSelect(int number);
    }
}
