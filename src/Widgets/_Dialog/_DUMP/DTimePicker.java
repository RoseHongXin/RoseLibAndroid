package hx.widget.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DTimePicker {

    public static void show(Activity act, TimePicker.OnTimeChangedListener listener){
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            TimePickerDialog timePickerDialog = new TimePickerDialog(mAct);
        }else showSelfDefine(mAct, callback);*/
        showSelfDefine(act, listener);
    }

    private static void showSelfDefine(Activity act, TimePicker.OnTimeChangedListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.Dialog_BottomUp);
        View layout = act.getLayoutInflater().inflate(R.layout.d_time_picker, null);
        AlertDialog dialog = builder.setView(layout).create();

        TimePicker _tp_ = (TimePicker)layout.findViewById(R.id._tp_);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
       /* _dp.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), (view, year, monthOfYear, dayOfMonth) -> {

        });*/
//        _tp_.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
//        _tp_.setMinute(mCalendar.get(Calendar.MINUTE));
        _tp_.setIs24HourView(true);
        _tp_.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        _tp_.setCurrentMinute(calendar.get(Calendar.MINUTE));

        layout.findViewById(R.id._bt_0).setOnClickListener(view -> dialog.dismiss());
        layout.findViewById(R.id._bt_1).setOnClickListener(view -> {
            if(listener != null) listener.onTimeChanged(_tp_, _tp_.getCurrentHour(), _tp_.getCurrentMinute());
            dialog.dismiss();
        });

        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);
        dialog.show();
    }
}
