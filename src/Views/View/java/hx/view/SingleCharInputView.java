package hx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hx.lib.R;

/**
 * Created by RoseHongXin on 2018/3/7 0007.
 */

public class SingleCharInputView extends RelativeLayout {

    private final static int MAX = 6;

    private EditText _et_singleCharInput;
    private TextView[] _tv_captchaNumbers;
    private View[] _li_captchaContainers;
    private String mInputContent;
    private Callback mCb;
    private int mRequiredCharCount = 4;

    public void callback(Callback callback) {
        this.mCb = callback;
    }

    public SingleCharInputView(Context context) {
        this(context, null);
    }

    public SingleCharInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleCharInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.l_singlechar_input_view, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SingleCharInputView);
        int charCount = ta.getInteger(R.styleable.SingleCharInputView_charCount, 4);
        ta.recycle();

        _tv_captchaNumbers = new TextView[MAX];
        _li_captchaContainers = new View[MAX];
        _tv_captchaNumbers[0] = (TextView) findViewById(R.id._tv_captchaNo1);
        _tv_captchaNumbers[1] = (TextView) findViewById(R.id._tv_captchaNo2);
        _tv_captchaNumbers[2] = (TextView) findViewById(R.id._tv_captchaNo3);
        _tv_captchaNumbers[3] = (TextView) findViewById(R.id._tv_captchaNo4);
        _tv_captchaNumbers[4] = (TextView) findViewById(R.id._tv_captchaNo5);
        _tv_captchaNumbers[5] = (TextView) findViewById(R.id._tv_captchaNo6);
        _li_captchaContainers[0] = findViewById(R.id._li_captchaNo1);
        _li_captchaContainers[1] = findViewById(R.id._li_captchaNo2);
        _li_captchaContainers[2] = findViewById(R.id._li_captchaNo3);
        _li_captchaContainers[3] = findViewById(R.id._li_captchaNo4);
        _li_captchaContainers[4] = findViewById(R.id._li_captchaNo5);
        _li_captchaContainers[5] = findViewById(R.id._li_captchaNo6);

        _et_singleCharInput = (EditText) findViewById(R.id._et_singleCharInput);
        charCount(charCount);

        _et_singleCharInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                mInputContent = _et_singleCharInput.getText().toString();
                for (int i = 0; i < mRequiredCharCount; i++) {
                    if (i < mInputContent.length()) {
                        _tv_captchaNumbers[i].setText(String.valueOf(mInputContent.charAt(i)));
                    } else {
                        _tv_captchaNumbers[i].setText("");
                    }
                }
                if (mCb != null && mInputContent.length() == mRequiredCharCount) { mCb.onComplete(mInputContent); }
            }
        });
    }

    public void charCount(int count){
        mRequiredCharCount = count;
        _et_singleCharInput.setMaxEms(mRequiredCharCount);
        for(int i = 0; i < mRequiredCharCount; i++){
            _li_captchaContainers[i].setVisibility(VISIBLE);
        }
    }

    public void triggerInvalidFeedback(){
//        Animation scaleAnim = new ScaleAnimation(0.8f, 1.2f, 0.8f, 1.2f);
        Animation rotateAnim = new RotateAnimation(-30, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnim.setDuration(2);
        rotateAnim.setDuration(2);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setRepeatCount(4);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        AnimationSet animSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.8f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(8);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setInterpolator(new BounceInterpolator());

//        animSet.addAnimation(scaleAnim);
//        animSet.addAnimation(rotateAnim);
        animSet.addAnimation(translateAnimation);
        for(int i = 0; i < mRequiredCharCount; i++) {
            if (TextUtils.isEmpty(_tv_captchaNumbers[i].getText())) _li_captchaContainers[i].startAnimation(animSet);
        }
        try {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if(vibrator != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, 128));
                } else {
                    vibrator.vibrate(1000);
                }
            }
        }catch (Exception e){
        }
    }

    public boolean captchaReady(){
        return !(TextUtils.isEmpty(_et_singleCharInput.getText()) || _et_singleCharInput.getText().length() < mRequiredCharCount);
    }
    public String text(){
        return _et_singleCharInput.getText().toString();
    }


    public interface Callback {
        void onComplete(String captcha);
    }
}
