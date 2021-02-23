package hx.components;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import hx.lib.R;
import hx.widget.TopBar;


public class ABase extends AppCompatActivity{

    public TopBar _tb_;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        try{
            _tb_ = findViewById(R.id._tb_);
        }catch (Exception e){
        }
    }

}
