package com.daguo.util.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.daguo.R;

/**
 * 重写的edittext 背景为下划线
 * 
 * @author Bugs_Rabbit 時間： 2015-8-6 下午5:18:32
 */
public class LineEditText extends EditText {

	private Paint mPaint;

	/**
	 * @param context
	 * @param attrs
	 */
	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(getResources().getColor(R.color.green_home));
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 画底线
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
				this.getHeight() - 1, mPaint);
	}
}
