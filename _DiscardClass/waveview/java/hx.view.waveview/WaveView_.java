/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class WaveView_ extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public int mFirstWaveColor = Color.parseColor("#28FFFFFF");
    public int mSecondWaveColor = Color.parseColor("#3CFFFFFF");

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    private boolean mShowWave;

    private BitmapShader mWaveShader;
    private Matrix mShaderMatrix;
    private Paint mViewPaint;
    private Paint mBorderPaint;

    private float mAmplitude;
    private float mWaterLevel;
    private float mWaveLength;
    private double mAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private float mWaveShiftDelta;
    private float mWaterLeveDelta;

    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    public WaveView_(Context context) {
        super(context);
        init();
    }

    public WaveView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);
        invalidate();
    }
    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mSecondWaveColor = behindWaveColor;
        mFirstWaveColor = frontWaveColor;
        if (getWidth() > 0 && getHeight() > 0) {
            // need to recreate shader when color changed
            mWaveShader = null;
            createShader();
            invalidate();
        }
    }
    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        drawWaveByQuad(canvas);
        drawWaveBySin(canvas);

        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    private void drawWaveByQuad(Canvas canvas){
        Paint wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStyle(Paint.Style.FILL);
        Path firstPath = new Path();
        Path secondPath = new Path();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int mAxisX = (int)-mWaveLength;
        int mAxisY = (int)(getBottom() - mAmplitude);
        int initialX = (int)mAxisX;
        int initialY = (int)mAxisY;
        List<Point> mPoints = new ArrayList<>();
        int pointCnt = 9 * width / (int)mWaveLength;
        for (int i = 0; i < pointCnt; i++) {
            int y = 0;
            switch (i % 4) {
                case 0:
                    y = initialY;
                    break;
                case 1:
                    y = initialY - (int)mAmplitude;
                    break;
                case 2:
                    y = initialY;
                    break;
                case 3:
                    y = initialY + (int)mAmplitude;
                    break;
            }
            Point point = new Point((int)(initialX + i * mWaveLength / 4), y);
            mPoints.add(point);
        }
        Rect mViewRect = new Rect(getLeft(), getTop(), getRight(), getBottom());

        int i = 0;
        int firstWaveShift = 0;
        int secondWaveShift = (int)-(mWaveLength / 3);
        firstPath.moveTo(mPoints.get(i).x + firstWaveShift, mPoints.get(i).y);
        secondPath.moveTo(mPoints.get(i).x + secondWaveShift, mPoints.get(i).y);
        for (; i < mPoints.size() - 2; i += 2) {
            Point first = mPoints.get(i + 1);
            Point second = mPoints.get(i + 2);
            firstPath.quadTo(first.x  + firstWaveShift, first.y, second.x  + firstWaveShift, second.y);
            secondPath.quadTo(first.x + secondWaveShift, first.y, second.x + secondWaveShift, second.y);
        }
        firstPath.lineTo(mPoints.get(i).x + firstWaveShift, mViewRect.bottom);
        firstPath.lineTo(mPoints.get(0).x + firstWaveShift, mViewRect.bottom);
        firstPath.close();

        secondPath.lineTo(mPoints.get(i).x + secondWaveShift, mViewRect.bottom);
        secondPath.lineTo(mPoints.get(0).x + secondWaveShift, mViewRect.bottom);
        secondPath.close();
        
        wavePaint.setColor(mFirstWaveColor);
        canvas.drawPath(firstPath, wavePaint);

        wavePaint.setColor(mSecondWaveColor);
//        mWavePaint.setColor(Color.CYAN);
        wavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawPath(secondPath, wavePaint);
    }
    private void drawWaveBySin(Canvas canvas){
        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mSecondWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mAngularFrequency;
            float beginY = (float) (mWaterLevel + mAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFirstWaveColor);
        final int wave2Shift = (int) (mWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowWave && mWaveShader != null) {
            if (mViewPaint.getShader() == null) mViewPaint.setShader(mWaveShader);
            mShaderMatrix.setScale(1, mAmplitudeRatio, 0, mWaterLevel);
            mShaderMatrix.postTranslate(mWaveShiftDelta, mWaterLeveDelta);
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
                    if (borderWidth > 0) {
                        canvas.drawRect(
                                borderWidth / 2f,
                                borderWidth / 2f,
                                getWidth() - borderWidth / 2f - 0.5f,
                                getHeight() - borderWidth / 2f - 0.5f,
                                mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth, mViewPaint);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        mWaveShiftRatio = waveShiftRatio;
        mWaveShiftDelta = mWaveShiftRatio * getWidth();
        invalidate();
    }
    public void setWaterLevelRatio(float waterLevelRatio) {
        mWaterLevelRatio = waterLevelRatio;
        mWaterLeveDelta = (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight();
        invalidate();
    }
    public void setAmplitudeRatio(float amplitudeRatio) {
        mAmplitudeRatio = amplitudeRatio  / DEFAULT_AMPLITUDE_RATIO;
        invalidate();
    }
    
    public void startWave(){

        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(WaveView_.this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(WaveView_.this, "waterLevelRatio", 0f, 0.5f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(WaveView_.this, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(waveShiftAnim, waterLevelAnim, amplitudeAnim);
        setShowWave(true);
        animatorSet.start();
    }
}