package hx.widget.util;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import hx.lib.R;


/**
 * Created by Rose on 3/10/2017.
 */

public class LhPassword {

    /*private boolean visible = false;

    private LhPassword(){}
    public LhPassword(EditText _et_pwd, ImageView _iv_eye){
        new LhPassword().handle(_et_pwd, _iv_eye);
    }

    public void handle(EditText _et_pwd, ImageView _iv_eye){
        _iv_eye.setImageResource(visible ? R.mipmap.i_eye_visible : R.mipmap.i_eye_invisible);
        _et_pwd.setTransformationMethod(visible ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        _iv_eye.setOnClickListener(view -> {
            visible = !visible;
            _iv_eye.setImageResource(visible ? R.mipmap.i_eye_visible : R.mipmap.i_eye_invisible);
            _et_pwd.setTransformationMethod(visible ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        });
    }

    public static LhPassword get(){
        return new LhPassword();
    }*/
}

