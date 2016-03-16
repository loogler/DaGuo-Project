package com.daguo.util.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
/**
 * 外部嵌套的scrollview
 * 
 * @author Bugs_Rabbit
 *  時間： 2015-9-8 下午3:37:34
 */
public class OutScrollView extends ScrollView {
	public OutScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 添加了一个手势选择器
		gestureDetector = new GestureDetector(new Yscroll());
		setFadingEdgeLength(0);
	}

	GestureDetector gestureDetector;
	View.OnTouchListener onTouchListener;

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// return super.onInterceptTouchEvent(ev);
		return super.onInterceptTouchEvent(ev)
				&& gestureDetector.onTouchEvent(ev);
	}

	class Yscroll extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 控制手指滑动的距离
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}

	}
}
