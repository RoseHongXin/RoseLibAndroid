package rose.android.jlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 *
 */

public class CountdownProgressBar extends View {

	/*--------------------------Progress params----------------------------------*/
	private Paint mProgressPaint = new Paint();
	private TextPaint mTextPaint = new TextPaint();
	private RectF mProgressArcRectF = new RectF();
	private int mTextSize = 80;

	private int mBackgroundColor = Color.parseColor("#EEFFFFFF");
	private int[] mProgressColors = new int[]{Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFFFFF")};
	private int mTextColor = Color.parseColor("#FFFFFFFF");

	private long mDuration = 5000;
	private long mProgress = 5000;
	private float mAnimProgressRatio = 0f;
	/*--------------------------Progress params----------------------------------*/

	private Listener mListener;
	private CountDownTimer mTimer;

	public void countdown(long duration){
		mDuration = duration;
		mTimer = new CountDownTimer(duration, 10) {
			@Override public void onTick(long millisUntilFinished) {
				mProgress = millisUntilFinished;
				invalidate();
				if(mListener != null) mListener.onTick(mProgress);
			}
			@Override public void onFinish() {
				mProgress = 0;
				invalidate();
				if(mListener != null) mListener.onTick(mProgress);
				if(mListener != null) mListener.onFinish();
			}
		};
		mTimer.onTick(mDuration);
		mTimer.start();
	}
	public void register(Listener listener){
		this.mListener = listener;
	}
	public void cancel(){
		if(mTimer != null) mTimer.cancel();
	}
	public boolean hasFinished(){
		return mTimer == null || mProgress == 0;
	}
	public long remain(){
		return mProgress;
	}

	public CountdownProgressBar(Context context) {
		super(context);
	}

	public CountdownProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setStyle(Paint.Style.STROKE);
		mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
		mProgressPaint.setStrokeJoin(Paint.Join.ROUND);

		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setTextSize(mTextSize);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed){
			int mCenterX = getWidth() / 2;
			int mCenterY = getHeight() / 2;
			int radius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2;
			int mRadius = (int) (radius - radius * 0.2f);
			mProgressArcRectF.top = mCenterY - mRadius;
			mProgressArcRectF.bottom = mCenterY + mRadius;
			mProgressArcRectF.left = mCenterX - mRadius;
			mProgressArcRectF.right = mCenterX + mRadius;

			mProgressPaint.setStrokeWidth(mRadius * 0.1f);
			mTextSize = (int) (mProgressArcRectF.width() / 3);
			mTextPaint.setTextSize(mTextSize);
		}
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//progress
		float x = mProgressArcRectF.centerX();
		float y = mProgressArcRectF.centerY();
		float progressAngle = progressAngle(progressRatio());
		SweepGradient shader = new SweepGradient(x, y, mProgressColors, new float[]{0f, mAnimProgressRatio + 0.01f});
		Matrix matrix = new Matrix();
		matrix.setRotate(-90, x, y);
		shader.setLocalMatrix(matrix);
		mProgressPaint.setShader(shader);
		canvas.drawArc(mProgressArcRectF, -90, progressAngle, false, mProgressPaint);

		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		String text = String.valueOf((int)(mProgress / 1000f + 0.5f));
		float textWidth = mTextPaint.measureText(text);
		canvas.drawText(text, x - textWidth / 2, y + fontMetrics.bottom, mTextPaint);
	}

	private float progressRatio() {
		return (float)mProgress / (float)mDuration;
	}
	private float progressAngle() {
		return -progressRatio() * 360.0f;
	}
	private float progressAngle(float ratio) {
		return -ratio * 360.0f;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		cancel();
		mTimer = null;
	}

	public interface Listener{
		void onTick(long progressMillis);
		void onFinish();
	}
}
