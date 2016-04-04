package com.mboconnect.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mboconnect.R;

/**
 * Created by ali.mehmood on 8/4/2015.
 */
public class CustomViewPager extends ViewPager {

	private boolean swipeable;

	public CustomViewPager (Context context) {

		super (context);
	}

	public CustomViewPager (Context context, AttributeSet attrs) {

		super (context, attrs);
		initUI (attrs);
	}

	private void initUI (AttributeSet attrs) {

		TypedArray a = getContext ().obtainStyledAttributes (attrs, R.styleable.CustomViewPager);
		try {
			swipeable = a.getBoolean (R.styleable.CustomViewPager_isSwipeable, false);
		}
		finally {
			a.recycle ();
		}
	}

	@Override
	public boolean onInterceptTouchEvent (MotionEvent event) {

		// Never allow swiping to switch between pages
		return false;
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {

		// Never allow swiping to switch between pages
		return false;
	}
}
