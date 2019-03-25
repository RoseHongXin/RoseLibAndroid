package hx.widget.dialog.address;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

import hx.lib.R;
import hx.view.sv.WheelView;

/**
 * Created by Rose on 3/2/2017.
 */

public class DAddressPicker implements View.OnClickListener{

    private WheelView _whv_province;
    private WheelView _whv_city;
    private WheelView _whv_county;

    private AlertDialog mDialog;
    private Callback mCb;
    private Address mAddress;
    
    private List<City> mProvinces;
    private List<City> mCities;
    private List<City> mCounties;

    public DAddressPicker(Activity act){
        AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.Dialog_BottomUp);
        View layout = act.getLayoutInflater().inflate(R.layout.d_address_picker, null);
        mDialog = builder.setView(layout).create();
        Window window = mDialog.getWindow();
        if(window != null) {
            window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        _whv_province = (WheelView)layout.findViewById(R.id._whv_province);
        _whv_city = (WheelView)layout.findViewById(R.id._whv_city);
        _whv_county = (WheelView)layout.findViewById(R.id._whv_county);
        layout.findViewById(R.id._bt_0).setOnClickListener(this);
        layout.findViewById(R.id._bt_1).setOnClickListener(this);
        mAddress = new Address();
    }

    public DAddressPicker data(List<City> data){
        this.mProvinces = data;
        return this;
    }
    public DAddressPicker callback(Callback cb){
        this.mCb = cb;
        return this;
    }
    public DAddressPicker build(){
        List<String> ps = new ArrayList<>(), cs = new ArrayList<>(), ts = new ArrayList<>(), empty = new ArrayList<>();
        _whv_province.setOnWheelViewListener((selectedIndex, item) -> {
            _whv_city.setItems(empty);
            _whv_county.setItems(empty);
            _whv_province.post(() -> {
                City province = mProvinces.get(selectedIndex - 1);
                mCities = province.sub;
                cs.clear();
                for(City c : mCities) cs.add(c.name);
                _whv_city.setItems(cs);
                _whv_city.getOnWheelViewListener().onSelected(1, cs.get(0));
            });
        });
        _whv_city.setOnWheelViewListener((selectedIndex, item) -> {
            _whv_county.setItems(empty);
            _whv_county.post(() -> {
                City city = mCities.get(selectedIndex - 1);
                mCounties = city.sub;
                ts.clear();
                for(City c : mCounties) ts.add(c.name);
                _whv_county.setItems(ts);
            });

        });
        for(City c : mProvinces) ps.add(c.name);
        _whv_province.setItems(ps);
        _whv_province.getOnWheelViewListener().onSelected(1, ps.get(0));
        return this;
    }

    public void show(){
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id._bt_0){
            mDialog.dismiss();
        }else if(id == R.id._bt_1){
            City province = mProvinces.get(_whv_province.getSeletedIndex());
            City city = mCities.get(_whv_city.getSeletedIndex());
            City county = mCounties.get(_whv_county.getSeletedIndex());
            mAddress.setProvince(province);
            mAddress.setCity(city);
            mAddress.setCounty(county);
            mCb.callback(mAddress);
            mDialog.dismiss();
        }
    }

    public interface Callback{
        void callback(Address address);
    }
}
