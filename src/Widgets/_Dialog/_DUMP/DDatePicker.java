package hx.widget.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DDatePicker {

    public static void show(Activity act, DatePicker.OnDateChangedListener listener, Date date){
        showSelfDefine(act, listener, date);
    }
    public static void show(Activity act, DatePicker.OnDateChangedListener listener){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            DatePickerDialog datePickerDialog = new DatePickerDialog(act);
            datePickerDialog.setOnDateSetListener(listener::onDateChanged);
        }else showSelfDefine(act, listener, null);
    }
    private static void showSelfDefine(Activity act, DatePicker.OnDateChangedListener listener, Date date){
        AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.Dialog_BottomUp);
        View layout = act.getLayoutInflater().inflate(R.layout.d_date_picker, null);
        AlertDialog dialog = builder.setView(layout).create();

        DatePicker _dp = (DatePicker)layout.findViewById(R.id._dp_);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
//       _dp.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), (view, year, monthOfYear, dayOfMonth) -> {});
        if(date != null) calendar.setTime(date);
        _dp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        layout.findViewById(R.id._bt_0).setOnClickListener(view -> dialog.dismiss());
        layout.findViewById(R.id._bt_1).setOnClickListener(view -> {
            if(listener != null) listener.onDateChanged(_dp, _dp.getYear(), _dp.getMonth(), _dp.getDayOfMonth());
            dialog.dismiss();
        });

        DialogHelper.erasePadding(dialog, Gravity.BOTTOM);

        dialog.show();
    }
}
