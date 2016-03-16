package com.daguo.util.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 说说界面  显示说说的图片
 * @author Bugs_Rabbit
 *  時間： 2015-8-20 下午12:07:44
 */
public class MyGridView extends GridView{
	

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec); 
	}

}
