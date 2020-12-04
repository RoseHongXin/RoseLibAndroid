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
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cncoderx.wheelview.Wheel3DView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

@Deprecated
public class DYearMonthDay extends DialogFragment{

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final String _FORMAT = "%1$d-%2$02d-%3$02d";

    private Wheel3DView _whv_year;
    private Wheel3DView _whv_month;
    private Wheel3DView _whv_day;
    private Callback mCb;
    private Activity mAct;
    private TextView _tv_anchor;
    private int mYear = -1, mMonthIdx = -1, mDay = -1;
    private int mMaxYear = 2020, mMinYear = 1990;
    Calendar mCalendar = Calendar.getInstance();

    public static String current(){
        Calendar calendar = Calendar.getInstance();
        return DateFormat.format(DATE_FORMAT, calendar.getTime()).toString();
    }
    public static String format(String time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = "----";
        try {
            Date date = simpleDateFormat.parse(time);
            dateStr = DateFormat.format(DATE_FORMAT, date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            dateStr = "----";
        }
        return dateStr;
    }

    public static DYearMonthDay obtain() {
        return new DYearMonthDay();
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
        }
        return inflater.inflate(R.layout.d_year_month_day, container, true);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _whv_year = (Wheel3DView) view.findViewById(R.id._whv_year);
        _whv_month = (Wheel3DView) view.findViewById(R.id._whv_month);
        _whv_day = (Wheel3DView) view.findViewById(R.id._whv_day);

        int curYear = mCalendar.get(Calendar.YEAR);
        if(mYear == -1 || mMonthIdx == -1 || mDay == -1) defDate(new Date());
        if(mYear < mMinYear || mYear > mMaxYear) mYear = curYear;

        int yearRangeLength = mMaxYear - mMinYear;
        CharSequence[] yearTexts = new String[(yearRangeLength + 1)];
        CharSequence[] monthTexts = new String[12];
        int defYearIdx = 0;
        for(int i = 0; i < yearTexts.length; i++){
            int year = i + mMinYear;
            yearTexts[i] = String.valueOf(year);
            if(year == mYear) defYearIdx = i;
        }
        for(int i = 0; i < 12; i++){
            monthTexts[i] = String.valueOf(i + 1);
        }
        _whv_year.setEntries(yearTexts);
        _whv_month.setEntries(monthTexts);
        _whv_year.setCurrentIndex(defYearIdx);
        _whv_month.setCurrentIndex(mMonthIdx);
        loadDays();
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int day = Integer.parseInt(_whv_day.getCurrentItem().toString());
            int month = mMonthIdx + 1;
            if(_tv_anchor != null) {
                String time = String.format(_FORMAT, mYear, month, day);
                _tv_anchor.setText(time);
                _tv_anchor.setTag(time);
            }
            if(mCb != null) {
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.MONTH, mMonthIdx);
                mCalendar.set(Calendar.DAY_OF_MONTH, day);
                mCb.onSelect(mYear, month, day, mCalendar.getTime());
            }
            dismiss();
        });
        _whv_year.setOnWheelChangedListener((wheelView, from, to) -> {
            mYear = Integer.parseInt(yearTexts[to].toString());
            loadDays();
        });
        _whv_month.setOnWheelChangedListener((wheelView, from, to) -> {
            mMonthIdx = to;
            loadDays();
        });
    }

    private void loadDays(){
        int maxDays;
        if(mMonthIdx == 1){
            maxDays = 28;
            if((mYear % 4 == 0 && mYear % 100 != 0) || (mYear % 100 == 0 && mYear % 400 == 0)) maxDays = 29;
        }
        else if(mMonthIdx == 0 || mMonthIdx == 2 || mMonthIdx == 4 || mMonthIdx == 6 || mMonthIdx == 7 || mMonthIdx == 9 || mMonthIdx == 11){
            maxDays = 31;
        }else{
            maxDays = 30;
        }
        CharSequence[] dayTexts = new String[maxDays];
        for(int i = 0; i < maxDays; i++){
            dayTexts[i] = String.valueOf(i + 1);
        }
        _whv_day.setEntries(dayTexts);
        _whv_day.setCurrentIndex(mDay - 1);
    }

    public DYearMonthDay host(Activity act){
        mAct = act;
        return this;
    }
    public DYearMonthDay defDate(Date date){
        if(date == null) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonthIdx = calendar.get(Calendar.MONTH);
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return this;
    }
    public DYearMonthDay defDate(String time){
        if(TextUtils.isEmpty(time)) return defDate(new Date());
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return defDate(date);
    }
    public DYearMonthDay yearRange(int min, int max){
        this.mMaxYear = max;
        this.mMinYear = min;
        return this;
    }
    public DYearMonthDay yearRange(int range){
        Calendar calendar = Calendar.getInstance();
        this.mMinYear = calendar.get(Calendar.YEAR);
        this.mMaxYear = mMinYear + range;
        return this;
    }

    public DYearMonthDay callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DYearMonthDay anchor(TextView _tv_anchor){
        this._tv_anchor = _tv_anchor;
        return this;
    }

    public DYearMonthDay show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DYearMonthDay");
        return this;
    }

    public interface Callback{
        void onSelect(int year, int month, int day, Date date);
    }
}
