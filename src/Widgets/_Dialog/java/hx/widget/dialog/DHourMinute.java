package hx.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cncoderx.wheelview.Wheel3DView;

import java.util.Calendar;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DHourMinute extends DialogFragment{

    private static final String _FORMAT = "%1$02d:%2$02d";

    private Wheel3DView _whv_hour;
    private Wheel3DView _whv_minute;
    private Callback mCb;
    private Activity mAct;
    private TextView _tv_anchor;
    private int mDefHour = 0, mDefMinute = 0;

    @SuppressLint("DefaultLocale")
    public static String current(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return String.format("%1$02d:%2$02d:%3$02d", hour, minute, second);
    }
    public static String hourMinuteFormat(int second){
        int hour = second / 3600;
        int minute = second % 3600 / 60;
        return String.format(_FORMAT, hour, minute);
    }

    public static DHourMinute obtain() {
        return new DHourMinute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.setCancelable(true);
        DialogHelper.NoPadding(dialog, Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.windowBackground)));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
        }
        return inflater.inflate(R.layout.d_hour_minute, container, true);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _whv_hour = (Wheel3DView) view.findViewById(R.id._whv_hour);
        _whv_minute = (Wheel3DView) view.findViewById(R.id._whv_minute);
        CharSequence[] hourTexts = new String[24];
        CharSequence[] minuteTexts = new String[60];
        for(int i = 0; i < 24; i++){
            hourTexts[i] = String.valueOf(i);
        }
        for(int i = 0; i < 60; i++){
            minuteTexts[i] = String.valueOf(i);
        }
        _whv_hour.setEntries(hourTexts);
        _whv_minute.setEntries(minuteTexts);
        _whv_hour.setCurrentIndex(mDefHour);
        _whv_minute.setCurrentIndex(mDefMinute);
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int hour = Integer.parseInt(_whv_hour.getCurrentItem().toString());
            int minute = Integer.parseInt(_whv_minute.getCurrentItem().toString());
            if(_tv_anchor != null) {
                _tv_anchor.setText(String.format(_FORMAT, hour, minute));
                int seconds = hour * 3600 + minute * 60;
                _tv_anchor.setTag(seconds);
            }
            if(mCb != null) mCb.onSelect(hour, minute);
            dismiss();
        });
    }

    public DHourMinute host(Activity act){
        mAct = act;
        return this;
    }
    public DHourMinute defValues(int minutes){
        int[] hourAndMinute = hourAndMinute(minutes);
        return defValues(hourAndMinute[0], hourAndMinute[1]);
    }
    public DHourMinute defValues(int hour, int minute){
        this.mDefHour = hour;
        this.mDefMinute = minute;
        return this;
    }
    public DHourMinute defValues(String time){
        if(!TextUtils.isEmpty(time)) {
            String[] times = time.split(":");
            if(times.length == 2) {
                try {
                    this.mDefHour = Integer.parseInt(times[0]);
                    this.mDefMinute = Integer.parseInt(times[1]);
                }catch (Exception e){}
            }
        }
        return this;
    }

    public DHourMinute callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DHourMinute anchor(TextView _tv_anchor){
        this._tv_anchor = _tv_anchor;
        return this;
    }

    public DHourMinute show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DHourMinute");
        return this;
    }

    public static int[] hourAndMinute(int minutes){
        int[] hourAndMinute = new int[2];
        hourAndMinute[0] = minutes / 60;
        hourAndMinute[1] = minutes % 60;
        return hourAndMinute;
    }

    public interface Callback{
        void onSelect(int hour, int minute);
    }
}
