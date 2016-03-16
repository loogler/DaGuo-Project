package com.daguo.util.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.daguo.R;
import com.daguo.utils.GetScreenRecUtil;

/**
 * 
 * @author Bugs_Rabbit 時間： 2015-8-14 下午6:39:51
 */
public class CurtainView extends RelativeLayout implements OnTouchListener {
	private static String TAG = "CurtainView";
	private Context mContext;
	private Scroller mScroller;
	private int mScreenHeigh = 0;
	private int mScreenWidth = 0;
	private int downY = 0;
	private int moveY = 0;
	private int scrollY = 0;
	private int upY = 0;
	private int curtainHeigh = 0;
	private boolean isOpen = false;
	private boolean isMove = false;
	private ImageView img_curtain_rope;
	private ImageView img_curtain_ad;
	private int upDuration = 1000;
	private int downDuration = 500;

	public CurtainView(Context context) {
		super(context);
		init(context);
	}

	public CurtainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CurtainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		Interpolator interpolator = new BounceInterpolator();
		mScroller = new Scroller(context, interpolator);
		mScreenHeigh = GetScreenRecUtil.getWindowHeigh(context);
		mScreenWidth = GetScreenRecUtil.getWindowWidth(context);
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		final View view = LayoutInflater.from(mContext).inflate(
				R.layout.curtain, null);
		img_curtain_ad = (ImageView) view.findViewById(R.id.img_curtain_ad);
		img_curtain_rope = (ImageView) view.findViewById(R.id.img_curtain_rope);
		addView(view);
		img_curtain_ad.post(new Runnable() {

			@Override
			public void run() {
				curtainHeigh = img_curtain_ad.getHeight();
				Log.d(TAG, "curtainHeigh= " + curtainHeigh);
				CurtainView.this.scrollTo(0, curtainHeigh);
			}
		});
		img_curtain_rope.setOnTouchListener(this);
	}

	public void startMoveAnim(int startY, int dy, int duration) {
		isMove = true;
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			isMove = true;
		} else {
			isMove = false;
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!isMove) {
			int offViewY = 0;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY = (int) event.getRawY();
				offViewY = downY - (int) event.getX();
				return true;
			case MotionEvent.ACTION_MOVE:
				moveY = (int) event.getRawY();
				scrollY = moveY - downY;
				if (scrollY < 0) {
					if (isOpen) {
						if (Math.abs(scrollY) <= img_curtain_ad.getBottom()
								- offViewY) {
							scrollTo(0, -scrollY);
						}
					}
				} else {
					if (!isOpen) {
						if (scrollY <= curtainHeigh) {
							scrollTo(0, curtainHeigh - scrollY);
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				upY = (int) event.getRawY();
				if (Math.abs(upY - downY) < 10) {
					onRopeClick();
					break;
				}
				if (downY > upY) {
					if (isOpen) {
						if (Math.abs(scrollY) > curtainHeigh / 2) {
							startMoveAnim(this.getScrollY(),
									(curtainHeigh - this.getScrollY()),
									upDuration);
							isOpen = false;
						} else {
							startMoveAnim(this.getScrollY(),
									-this.getScrollY(), upDuration);
							isOpen = true;
						}
					}
				} else {
					if (scrollY > curtainHeigh / 2) {
						startMoveAnim(this.getScrollY(), -this.getScrollY(),
								upDuration);
						isOpen = true;
					} else {
						startMoveAnim(this.getScrollY(),
								(curtainHeigh - this.getScrollY()), upDuration);
						isOpen = false;
					}
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	public void onRopeClick() {
		if (isOpen) {
			CurtainView.this.startMoveAnim(0, curtainHeigh, upDuration);
		} else {
			CurtainView.this.startMoveAnim(curtainHeigh, -curtainHeigh,
					downDuration);
		}
		isOpen = !isOpen;
	}
}
