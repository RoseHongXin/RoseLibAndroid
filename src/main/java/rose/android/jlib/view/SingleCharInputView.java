package rose.android.jlib.view;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
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

    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) { return; }
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    private void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) { return; }
        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }

    public void triggerInvalidFeedback(){
        for(int i = 0; i < mRequiredCharCount; i++) {
            if (TextUtils.isEmpty(_tv_captchaNumbers[i].getText())) {
                View _v_ =_li_captchaContainers[i];
//                startShakeByViewAnim(_v_, 0.8f, 1.2f, 30, 1000);
                startShakeByPropertyAnim(_v_, 0.8f, 1.2f, 8, 1500);
            }
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
