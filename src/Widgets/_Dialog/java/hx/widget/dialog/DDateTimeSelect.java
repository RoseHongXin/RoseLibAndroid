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
import android.view.WindowManager;
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

@SuppressLint("DefaultLocale")
public class DDateTimeSelect extends DialogFragment{

    public static final int MASK_YEAR       = 1 << 6;
    public static final int MASK_MONTH      = 1 << 5;
    public static final int MASK_DAY        = 1 << 4;
    public static final int MASK_HOUR       = 1 << 3;
    public static final int MASK_MINUTE     = 1 << 2;
    public static final int MASK_SECOND     = 1 << 0;

    public static int MODE_YEAR_MONTH_DAY = MASK_YEAR | MASK_MONTH | MASK_DAY;
    public static int MODE_HOUR_MINUTE = MASK_HOUR | MASK_MINUTE;
    public static int MODE_HOUR_MINUTE_SECOND = MASK_HOUR | MASK_MINUTE | MASK_SECOND;
    public static int MODE_FULL = MODE_YEAR_MONTH_DAY | MODE_HOUR_MINUTE_SECOND;
    public static int MODE_EXCLUDE_SECOND = MODE_YEAR_MONTH_DAY | MODE_HOUR_MINUTE;

    private View _li_year;
    private View _li_month;
    private View _li_day;
    private View _li_hour;
    private View _li_minute;
    private View _li_second;
    private Wheel3DView _whv_year;
    private Wheel3DView _whv_month;
    private Wheel3DView _whv_day;
    private Wheel3DView _whv_hour;
    private Wheel3DView _whv_minute;
    private Wheel3DView _whv_second;

    private Callback mCb;
    private Activity mAct;
    private int mMode = MODE_FULL;
    private String mFormat = "yyyy-MM-dd hh:mm:ss";
    private boolean mFillAfterSelect = true;
    private TextView _tv_anchor;
    private int mYear, mMonthIdx;
    private int mMaxYear = 2040, mMinYear = 1990;
    private Calendar mCalendar = Calendar.getInstance();
    private int[] mDefValues;

    public static DDateTimeSelect obtain() {
        return new DDateTimeSelect();
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
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.2f;
            window.setAttributes(params);
        }
        return inflater.inflate(R.layout.d_full_datetime, container, true);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _li_year = view.findViewById(R.id._li_year);
        _li_month = view.findViewById(R.id._li_month);
        _li_day = view.findViewById(R.id._li_day);
        _li_hour = view.findViewById(R.id._li_hour);
        _li_minute = view.findViewById(R.id._li_minute);
        _li_second = view.findViewById(R.id._li_second);
        _whv_year = (Wheel3DView) view.findViewById(R.id._whv_year);
        _whv_month = (Wheel3DView) view.findViewById(R.id._whv_month);
        _whv_day = (Wheel3DView) view.findViewById(R.id._whv_day);
        _whv_hour = (Wheel3DView) view.findViewById(R.id._whv_hour);
        _whv_minute = (Wheel3DView) view.findViewById(R.id._whv_minute);
        _whv_second = (Wheel3DView) view.findViewById(R.id._whv_second);

        _li_year.setVisibility((mMode & MASK_YEAR) != 0 ? View.VISIBLE : View.GONE);
        _li_month.setVisibility((mMode & MASK_MONTH) != 0 ? View.VISIBLE : View.GONE);
        _li_day.setVisibility((mMode & MASK_DAY) != 0 ? View.VISIBLE : View.GONE);
        _li_hour.setVisibility((mMode & MASK_HOUR) != 0 ? View.VISIBLE : View.GONE);
        _li_minute.setVisibility((mMode & MASK_MINUTE) != 0 ? View.VISIBLE : View.GONE);
        _li_second.setVisibility((mMode & MASK_SECOND) != 0 ? View.VISIBLE : View.GONE);

        defValuesExtract();
        initYearMonthDay();
        initHourMinuteSecond();

        view.findViewById(R.id._bt_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id._bt_confirm).setOnClickListener(v -> {
            int day = Integer.parseInt(_whv_day.getCurrentItem().toString());
            int hour = Integer.parseInt(_whv_hour.getCurrentItem().toString());
            int minute = Integer.parseInt(_whv_minute.getCurrentItem().toString());
            int second = Integer.parseInt(_whv_second.getCurrentItem().toString());
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.MONTH, mMonthIdx);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mCalendar.set(Calendar.SECOND, second);
            Date date = mCalendar.getTime();
            if(mFillAfterSelect && _tv_anchor != null && !TextUtils.isEmpty(mFormat)) {
                String time = DateFormat.format(mFormat, date).toString();
                _tv_anchor.setText(time);
                _tv_anchor.setTag(date);
            }
            if(mCb != null) {
                mCb.onSelect(date);
            }
            dismiss();
        });
    }
    private void defValuesExtract(){
        if(mDefValues == null) {
            mDefValues = new int[]{-1, -1, -1, -1, -1, -1};
        }
        if(mDefValues[0] == -1) mDefValues[0] = mCalendar.get(Calendar.YEAR);
        if(mDefValues[1] == -1) mDefValues[1] = mCalendar.get(Calendar.MONTH);
        if(mDefValues[2] == -1) mDefValues[2] = mCalendar.get(Calendar.DAY_OF_MONTH);
        if(mDefValues[3] == -1) mDefValues[3] = mCalendar.get(Calendar.HOUR_OF_DAY);
        if(mDefValues[4] == -1) mDefValues[4] = mCalendar.get(Calendar.MINUTE);
        if(mDefValues[5] == -1) mDefValues[5] = mCalendar.get(Calendar.SECOND);
    }
    private void initYearMonthDay(){
        int curYear = mCalendar.get(Calendar.YEAR);
        mYear = mDefValues[0];
        mMonthIdx = mDefValues[1];
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
        _whv_year.setOnWheelChangedListener((wheelView, from, to) -> {
            mYear = Integer.parseInt(yearTexts[to].toString());
            loadDays();
        });
        _whv_month.setOnWheelChangedListener((wheelView, from, to) -> {
            mMonthIdx = to;
            loadDays();
        });
        loadDays();
    }
    private void initHourMinuteSecond(){
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
        _whv_second.setEntries(minuteTexts);
        _whv_hour.setCurrentIndex(mDefValues[3]);
        _whv_minute.setCurrentIndex(mDefValues[4]);
        _whv_second.setCurrentIndex(mDefValues[5]);
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
        _whv_day.setCurrentIndex(mDefValues[2] - 1);
    }

    public DDateTimeSelect host(Activity act){
        mAct = act;
        return this;
    }
    public DDateTimeSelect mode(int mode){
        mMode = mode;
        return this;
    }
    public DDateTimeSelect decoFormat(String format){
        mFormat = format;
        return this;
    }
    public DDateTimeSelect defDate(Date date){
        if(date == null) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mDefValues = new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)};
        return this;
    }
    public DDateTimeSelect defDate(String time){
        if(TextUtils.isEmpty(time)) return defDate(new Date());
        if(time.contains("-")) {
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss", Locale.getDefault());
            Date date;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
            return defDate(date);
        }else {
            if(time.contains(":")){
                String[] times = time.split(":");
                if(times.length >= 2) {
                    try {
                        int hour = Integer.parseInt(times[0]);
                        int minute = Integer.parseInt(times[1]);
                        int second = -1;
                        if(times.length > 2) second = Integer.parseInt(times[2]);
                        return defValues(MODE_HOUR_MINUTE_SECOND, hour, minute, second);
                    }catch (Exception e){}
                }
            }
        }
        return this;
    }
    public DDateTimeSelect defValues(int mode, int... defValues){
        if(mode == MODE_YEAR_MONTH_DAY){
            mDefValues = new int[]{defValues[0], defValues[1], defValues[2], -1, -1, -1};
        }else if(mode == MODE_HOUR_MINUTE){
            mDefValues = new int[]{-1, -1, -1, defValues[0], defValues[1], -1};
        }else if(mode == MODE_HOUR_MINUTE_SECOND){
            mDefValues = new int[]{-1, -1, -1, defValues[0], defValues[1], defValues[2]};
        }
        return this;
    }
    public DDateTimeSelect yearRange(int min, int max){
        this.mMaxYear = max;
        this.mMinYear = min;
        return this;
    }
    public DDateTimeSelect yearRange(int range){
        Calendar calendar = Calendar.getInstance();
        this.mMinYear = calendar.get(Calendar.YEAR);
        this.mMaxYear = mMinYear + range;
        return this;
    }
    public DDateTimeSelect callback(Callback cb){
        mCb = cb;
        return this;
    }
    public DDateTimeSelect anchor(TextView _tv_anchor){
        this._tv_anchor = _tv_anchor;
        return this;
    }
    public DDateTimeSelect fillAfterSelect(boolean fillAfterSelect){
        this.mFillAfterSelect = fillAfterSelect;
        return this;
    }

    public DDateTimeSelect show(){
        show(((AppCompatActivity)mAct).getSupportFragmentManager(), "DDateTimeSelect");
        return this;
    }


    public static String current(String format){
        Calendar calendar = Calendar.getInstance();
        return DateFormat.format(format, calendar.getTime()).toString();
    }
    public static String format(String time, String format){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = "----";
        try {
            Date date = simpleDateFormat.parse(time);
            dateStr = DateFormat.format(format, date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public interface Callback{
        void onSelect(Date date);
    }
}
