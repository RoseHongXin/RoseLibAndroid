package hx.view.waveview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rose on 4/12/2017.
 */

public class WaveView extends View{

    private final float DEFAULT_WAVE_SHIFT_RATIO = 1.0f;
    private final float DEFAULT_WAVE_AMPLITUDE_RATIO = 1f;
    private final float DEFAULT_WATER_LEVEL_RATIO = 1f;

    public int mFirstWaveColor = Color.parseColor("#28FFFFFF");
    public int mSecondWaveColor = Color.parseColor("#3CFFFFFF");

    private int mViewWidth;
    private int mViewHeight;
    private Paint mViewPaint;
    private Rect mViewRect;

    private List<Point> mWavePoints;
    private float mAxisX, mAxisY;
    private float mPivotX, mPivotY;

    private BitmapShader mWaveShader;
    private Matrix mWaveShaderMatrix;
    private Paint mWavePaint;
    private Path mFirstWavePath;
    private Path mSecondWavePath;

    private float mWaterLevel;
    private double mAngularFrequency;
    private int mAmplitude;
    private int mWaveDiff;
    private int mWaveLength;
    private int mWaveShiftDelta = 0;
    private int mWaterLevelShiftDelta = 0;
    private int mAmplitudeShiftDelta;
    private float mWaterLevelShiftRatio = 0f;
    private float mWaveShiftRatio = 0f;
    private float mAmplitudeShiftRatio = 0f;



    private boolean mWaveEnable;
    private AnimatorSet mAnims;


    public WaveView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);

        mWaveShaderMatrix = new Matrix();

        mFirstWavePath = new Path();
        mSecondWavePath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed) init();
    }

    private void init(){
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mViewRect = new Rect(getLeft(), getTop(), getRight(), getBottom());

        mAmplitude = mViewHeight / 8;
        mWaveLength = mViewWidth;
        mWaterLevel = mViewHeight / 7 * 4;

        mAxisX = -mWaveLength + mViewRect.left;
        /*
         * very important.
         * make sure a whole wave's coordinate y the wave lower than the bottom edge.
         *      which means the wave higher than the bottom edge by human seen.
         *
         * the shader of canvas coordinate y's TileMode is CLAMP.
          *     if first draw wave and bottom edge is not enough,
          *     the attribute CLAMP will make the wave curve missing. (the missing part cut by bottom edge, fill in the background color.)
         * */
        mAxisY = mViewRect.bottom - mAmplitude - 20;

        mPivotX = mViewWidth / 2 + mViewRect.left;
        mPivotY =  mViewRect.bottom;

        mAngularFrequency = 2.0f * Math.PI / mWaveLength;
        mWaveDiff = -mWaveLength / 3;

        createWaveShader();
    }

    private void calPoints(){
        int initialX = (int)mAxisX;
        int initialY = (int)mAxisY;
        int pointCnt = 9 + 5;
        mWavePoints = new ArrayList<>(pointCnt);
        for (int i = 0; i < pointCnt; i++) {
            int y = 0;
            switch (i % 4) {
                case 0:
                    y = initialY;
                    break;
                case 1:
                    y = initialY + mAmplitude;
                    break;
                case 2:
                    y = initialY;
                    break;
                case 3:
                    y = initialY - mAmplitude;
                    break;
            }
            Point point = new Point((initialX + i * mWaveLength / 4), y);
            mWavePoints.add(point);
        }
    }

    private void createWaveShader(){
        Bitmap bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawWaveByQuad(canvas);
//        drawWaveBySin(canvas);

        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }
    private void drawWaveByQuad(Canvas canvas){
        calPoints();
        int initialX = (int)mAxisX;
        int initialY = (int)mAxisY;
        int pointCnt = 9 + 3;
        mWavePoints = new ArrayList<>(pointCnt);
        for (int i = 0; i < pointCnt; i++) {
            int y = 0;
            switch (i % 4) {
                case 0:
                    y = initialY;
                    break;
                case 1:
                    y = initialY + mAmplitude;
                    break;
                case 2:
                    y = initialY;
                    break;
                case 3:
                    y = initialY - mAmplitude;
                    break;
            }
            Point point = new Point((initialX + i * mWaveLength / 4), y);
            mWavePoints.add(point);
        }
        mFirstWavePath.rewind();
        mSecondWavePath.rewind();
        int i = 0;
        mFirstWavePath.moveTo(mWavePoints.get(i).x, mWavePoints.get(i).y);
        mSecondWavePath.moveTo(mWavePoints.get(i).x + mWaveDiff, mWavePoints.get(i).y);
        for (; i < mWavePoints.size() - 2; i += 2) {
            Point first = mWavePoints.get(i + 1);
            Point second = mWavePoints.get(i + 2);
            mFirstWavePath.quadTo(first.x, first.y, second.x, second.y);
            mSecondWavePath.quadTo(first.x + mWaveDiff, first.y, second.x + mWaveDiff, second.y);
        }
        mFirstWavePath.lineTo(mWavePoints.get(i).x, mViewRect.bottom);
        mFirstWavePath.lineTo(mWavePoints.get(0).x, mViewRect.bottom);
        mFirstWavePath.close();

        mSecondWavePath.lineTo(mWavePoints.get(i).x + mWaveDiff, mViewRect.bottom);
        mSecondWavePath.lineTo(mWavePoints.get(0).x + mWaveDiff, mViewRect.bottom);
        mSecondWavePath.close();

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);

        mWavePaint.setColor(mFirstWaveColor);
        canvas.drawPath(mFirstWavePath, mWavePaint);

        mWavePaint.setColor(mSecondWaveColor);
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
//        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawPath(mSecondWavePath, mWavePaint);
    }
    private void drawWaveBySin(Canvas canvas){
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        final int endX = mViewWidth + 1;
        final int endY = mViewHeight;
        float[] waveY = new float[endX];
        mWavePaint.setColor(mFirstWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mAngularFrequency;
            float beginY = (float) (mAxisY + mAmplitude * Math.sin(wx));
//            float beginY = (float) (mAxisY + mAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, mWavePaint);
            waveY[beginX] = beginY;
        }
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        mWavePaint.setColor(mSecondWaveColor);
        final int wave2Shift = mWaveLength / 4;
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, mWavePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mWaveEnable) {
//            mWaveShaderMatrix.setScale(1f, mAmplitudeShiftRatio, mPivotX, mWaterLevel);
            mWaveShaderMatrix.setScale(1f, mAmplitudeShiftRatio, mPivotX, mPivotY);
            mWaveShaderMatrix.postTranslate(mWaveShiftDelta, mWaterLevelShiftDelta);
            mWaveShader.setLocalMatrix(mWaveShaderMatrix);
            canvas.drawRect(mViewRect, mViewPaint);
//        canvas.drawCircle(mViewRect.right / 2 , mViewRect.top / 2, mWaveLength / 2, mViewPaint);
        }else{
            mWaveShader.setLocalMatrix(null);
        }
    }

    public void setWaveShift(float ratio){
        mWaveShiftDelta = (int)(ratio * mWaveLength + 0.5f);
        mWaveShiftRatio = ratio;
        invalidate();
    }
    public void setAmplitudeShift(float ratio){
        mAmplitudeShiftRatio = ratio;
        mAmplitudeShiftDelta = (int) (mAmplitude * mAmplitudeShiftRatio + 0.5f);
        invalidate();
    }
    public void setWaterLevelShift(float ratio){
        mWaterLevelShiftDelta = (int)(mWaterLevel * ratio + 0.5f);
        mWaterLevelShiftRatio = ratio;
        invalidate();
    }

    public void start(){
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(WaveView.this, "waveShift", 0f, 1f).setDuration(2000);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        ObjectAnimator amplitudeShiftAnim = ObjectAnimator.ofFloat(WaveView.this, "amplitudeShift", 0f, 1f).setDuration(6000);
        amplitudeShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeShiftAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeShiftAnim.setInterpolator(new LinearInterpolator());

        ObjectAnimator waterLevelShiftAnim = ObjectAnimator.ofFloat(WaveView.this, "waterLevelShift", -0.0001f, -1.0f).setDuration(10000);
        waterLevelShiftAnim.setInterpolator(new DecelerateInterpolator());
//        waterLevelShiftAnim.setInterpolator(new LinearInterpolator());

        mAnims = new AnimatorSet();
        mAnims.playTogether(waveShiftAnim, waterLevelShiftAnim, amplitudeShiftAnim);
        mWaveEnable = true;
        mAnims.start();
    }
    public void stop(){
        mWaveEnable = false;
        if(mAnims != null) mAnims.cancel();
        if(mAnims != null) mAnims.end();
        invalidate();
    }

}
