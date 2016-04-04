/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */

package com.tenpearls.android.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tenpearls.android.R;

/**
 * Displays a circular seekbar to display progress values.
 * 
 * @author 10Pearls
 */
public class CircularSeekbar extends View {
	/*
	 * Constants used to save/restore the instance state.
	 */
	/** The Constant STATE_PARENT. */
	private static final String           STATE_PARENT              = "parent";

	/** The Constant STATE_ANGLE. */
	private static final String           STATE_ANGLE               = "angle";

	/** The m on circle seek bar change listener. */
	private OnCircleSeekBarChangeListener mOnCircleSeekBarChangeListener;

	/**
	 * The m color wheel paint. {@code Paint} instance used to draw the color
	 * wheel.
	 */
	private Paint                         mColorWheelPaint;

	/**
	 * The m pointer halo paint. {@code Paint} instance used to draw the
	 * pointer's "halo".
	 */
	private Paint                         mPointerHaloPaint;

	/**
	 * The m pointer color. {@code Paint} instance used to draw the pointer (the
	 * selected color).
	 */
	private Paint                         mPointerColor;

	/**
	 * The stroke width used to paint the color wheel (in pixels).
	 */
	private int                           mColorWheelStrokeWidth;

	/**
	 * The radius of the pointer (in pixels).
	 */
	private int                           mPointerRadius;

	/**
	 * The rectangle enclosing the color wheel.
	 */
	private RectF                         mColorWheelRectangle      = new RectF ();

	/**
	 * The m user is moving pointer. {@code true} if the user clicked on the
	 * pointer to start the move mode. {@code false} once the user stops touching
	 * the screen. @see #onTouchEvent(MotionEvent)
	 */
	private boolean                       mUserIsMovingPointer      = false;

	/**
	 * The ARGB value of the currently selected color.
	 */
	@SuppressWarnings("unused")
	private int                           mColor;

	/**
	 * Number of pixels the origin of this view is moved in X- and Y-direction.
	 * 
	 * <p>
	 * We use the center of this (quadratic) View as origin of our internal
	 * coordinate system. Android uses the upper left corner as origin for the
	 * View-specific coordinate system. So this is the value we use to translate
	 * from one coordinate system to the other.
	 * </p>
	 * 
	 * <p>
	 * Note: (Re)calculated in {@link #onMeasure(int, int)}.
	 * </p>
	 * 
	 * @see #onDraw(Canvas)
	 */
	private float                         mTranslationOffset;

	/**
	 * Radius of the color wheel in pixels.
	 * 
	 * <p>
	 * Note: (Re)calculated in {@link #onMeasure(int, int)}.
	 * </p>
	 */
	private float                         mColorWheelRadius;

	/**
	 * The pointer's position expressed as angle (in rad).
	 */
	private float                         mAngle;

	/** The text paint. */
	private Paint                         textPaint;

	/** The text. */
	private String                        text;

	/** The conversion. */
	private int                           conversion                = 0;

	/** The max. */
	private int                           max                       = 100;

	/** The color_attr. */
	private String                        color_attr;

	/** The color. */
	@SuppressWarnings("unused")
	private int                           color;

	/** The s. */
	private SweepGradient                 s;

	/** The m arc color. */
	private Paint                         mArcColor;

	/** The text_color_attr. */
	private String                        wheel_color_attr,
	        wheel_unactive_color_attr,
	        pointer_color_attr,
	        pointer_halo_color_attr,
	        text_color_attr;

	/** The init_position. */
	private int                           wheel_color,
	        unactive_wheel_color,
	        pointer_color,
	        pointer_halo_color,
	        text_size,
	        text_color,
	        init_position;

	/** The block_end. */
	private boolean                       block_end                 = false;

	/** The last x. */
	private float                         lastX;

	/** The last_radians. */
	private int                           last_radians              = 0;

	/** The block_start. */
	private boolean                       block_start               = false;

	/** The arc_finish_radians. */
	private int                           arc_finish_radians        = 360;

	/** The start_arc. */
	private int                           start_arc                 = 270;

	/** The pointer position. */
	private float[]                       pointerPosition;

	/** The m color center halo. */
	private Paint                         mColorCenterHalo;

	/** The m color center halo rectangle. */
	private RectF                         mColorCenterHaloRectangle = new RectF ();

	/** The m circle text color. */
	private Paint                         mCircleTextColor;

	/** The end_wheel. */
	private int                           end_wheel;

	/** The show_text. */
	private boolean                       show_text                 = true;

	/** The is enabled. */
	public boolean                        isEnabled;

	/** The is nob touched. */
	private boolean                       isNobTouched;

	/** The knob drawable. */
	private Drawable                      knobDrawable;

	/** The associative sub values. */
	private static double[]               associativeSubValues      = { -0.66576755, -0.69464743, -0.6437939, -0.58869404, -0.5457743, -0.4826504, -0.46147528, -0.37774816, -0.34220728, -0.28194657, -0.24968542, -0.23424351, -0.1881346, -0.12903176, -0.09535727, -0.04526411, 0.002828274, 0.047091257, 0.09381257, 0.13591342, 0.18132089, 0.22429445, 0.26342952, 0.3001171, 0.33854678, 0.41410032, 0.45251814, 0.495051, 0.5548258, 0.5845225, 0.6220315, 0.68351346, 0.69926476, 0.757369, 0.791273, 0.84407884, 0.8982447, 0.9476667, 1.002528, 1.0533262, 1.0769792, 1.1449993, 1.1948318, 1.2251348, 1.2798043, 1.2929298, 1.3444663, 1.3679678, 1.4138112, 1.4611379, 1.5054452, 1.543468, 1.5624136, 1.6369487, 1.6728332, 1.7074515, 1.7468847, 1.791603, 1.8406689, 1.8857391, 1.933175, 1.9815756, 2.040369, 2.0714304, 2.134784, 2.164404, 2.2250712, 2.2525218, 2.3143306, 2.3535233, 2.3781466, 2.446746, 2.4828033, 2.5207112, 2.5968115, 2.6113133, 2.6778982, 2.7131612, 2.7678072, 2.816181, 2.8647475, 2.891193, 2.9166791, 2.97147, 3.0332828, 3.0975244, 3.138134, -3.108075, -3.0455546, -3.0152254, -2.970619, -2.9031858, -2.8872247, -2.8333056, -2.806976, -2.7602053, -2.7186742, -2.6857786, -2.6098142, -2.574751 };

	/**
	 * Sets the checks if is enabled.
	 * 
	 * @param isEnabled the new checks if is enabled
	 */
	public void setIsEnabled (boolean isEnabled) {

		this.isEnabled = isEnabled;
	}

	/**
	 * Instantiates a new circular seekbar.
	 * 
	 * @param context the context
	 */
	public CircularSeekbar (Context context) {

		super (context);
		init (null, 0);
	}

	/**
	 * Instantiates a new circular seekbar.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CircularSeekbar (Context context, AttributeSet attrs) {

		super (context, attrs);
		init (attrs, 0);
	}

	/**
	 * Instantiates a new circular seekbar.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public CircularSeekbar (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
		init (attrs, defStyle);
	}

	/**
	 * Inits the.
	 * 
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	private void init (AttributeSet attrs, int defStyle) {

		final TypedArray a = getContext ().obtainStyledAttributes (attrs, R.styleable.CircularSeekbar, defStyle, 0);

		initAttributes (a);

		a.recycle ();

		mColorWheelPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
		mColorWheelPaint.setShader (s);
		mColorWheelPaint.setColor (unactive_wheel_color);
		mColorWheelPaint.setStyle (Paint.Style.STROKE);
		mColorWheelPaint.setStrokeWidth (mColorWheelStrokeWidth);

		mColorCenterHalo = new Paint (Paint.ANTI_ALIAS_FLAG);
		mColorCenterHalo.setColor (Color.CYAN);
		mColorCenterHalo.setAlpha (0xCC);

		mPointerHaloPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
		mPointerHaloPaint.setColor (pointer_halo_color);
		mPointerHaloPaint.setStrokeWidth (mPointerRadius + 10);

		textPaint = new Paint (Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		textPaint.setColor (text_color);
		textPaint.setStyle (Style.FILL_AND_STROKE);
		textPaint.setTextAlign (Align.LEFT);
		textPaint.setTextSize (text_size);

		mPointerColor = new Paint (Paint.ANTI_ALIAS_FLAG);
		mPointerColor.setStrokeWidth (mPointerRadius);

		mPointerColor.setColor (pointer_color);

		mArcColor = new Paint (Paint.ANTI_ALIAS_FLAG);
		mArcColor.setColor (wheel_color);
		mArcColor.setStyle (Paint.Style.STROKE);
		mArcColor.setStrokeWidth (mColorWheelStrokeWidth);

		mCircleTextColor = new Paint (Paint.ANTI_ALIAS_FLAG);
		mCircleTextColor.setColor (Color.WHITE);
		mCircleTextColor.setStyle (Paint.Style.FILL);

		arc_finish_radians = (int) calculateAngleFromText (init_position) - 90;

		if (arc_finish_radians > end_wheel)
			arc_finish_radians = end_wheel;
		mAngle = calculateAngleFromRadians (arc_finish_radians > end_wheel ? end_wheel : arc_finish_radians);
		text = String.valueOf (calculateTextFromAngle (arc_finish_radians));

		invalidate ();
	}

	/**
	 * Inits the attributes.
	 * 
	 * @param a the a
	 */
	private void initAttributes (TypedArray a) {

		mColorWheelStrokeWidth = a.getInteger (R.styleable.CircularSeekbar_wheel_size, 16);
		mPointerRadius = a.getInteger (R.styleable.CircularSeekbar_pointer_size, 0);
		max = a.getInteger (R.styleable.CircularSeekbar_max, 100);

		color_attr = a.getString (R.styleable.CircularSeekbar_color_value);
		wheel_color_attr = a.getString (R.styleable.CircularSeekbar_wheel_active_color);
		wheel_unactive_color_attr = a.getString (R.styleable.CircularSeekbar_wheel_unactive_color);
		pointer_color_attr = a.getString (R.styleable.CircularSeekbar_pointer_color);
		pointer_halo_color_attr = a.getString (R.styleable.CircularSeekbar_pointer_halo_color);

		text_color_attr = a.getString (R.styleable.CircularSeekbar_text_color);

		text_size = a.getInteger (R.styleable.CircularSeekbar_text_size, 95);

		init_position = a.getInteger (R.styleable.CircularSeekbar_init_position, 0);

		start_arc = a.getInteger (R.styleable.CircularSeekbar_start_angle, 0);
		end_wheel = a.getInteger (R.styleable.CircularSeekbar_end_angle, 360);

		show_text = a.getBoolean (R.styleable.CircularSeekbar_show_text, true);

		last_radians = end_wheel;

		if (init_position < start_arc)
			init_position = calculateTextFromStartAngle (start_arc);

		if (color_attr != null) {
			try {
				color = Color.parseColor (color_attr);
			}
			catch (IllegalArgumentException e) {
				color = Color.CYAN;
			}
			color = Color.parseColor (color_attr);
		}
		else {
			color = Color.CYAN;
		}

		if (wheel_color_attr != null) {
			try {
				wheel_color = Color.parseColor (wheel_color_attr);
			}
			catch (IllegalArgumentException e) {
				wheel_color = Color.DKGRAY;
			}

		}
		else {
			wheel_color = Color.DKGRAY;
		}
		if (wheel_unactive_color_attr != null) {
			try {
				unactive_wheel_color = Color.parseColor (wheel_unactive_color_attr);
			}
			catch (IllegalArgumentException e) {
				unactive_wheel_color = Color.CYAN;
			}

		}
		else {
			unactive_wheel_color = Color.CYAN;
		}

		if (pointer_color_attr != null) {
			try {
				pointer_color = Color.parseColor (pointer_color_attr);
			}
			catch (IllegalArgumentException e) {
				pointer_color = Color.CYAN;
			}

		}
		else {
			pointer_color = Color.CYAN;
		}

		if (pointer_halo_color_attr != null) {
			try {
				pointer_halo_color = Color.parseColor (pointer_halo_color_attr);
			}
			catch (IllegalArgumentException e) {
				pointer_halo_color = Color.CYAN;
			}

		}
		else {
			pointer_halo_color = Color.DKGRAY;
		}

		if (text_color_attr != null) {
			try {
				text_color = Color.parseColor (text_color_attr);
			}
			catch (IllegalArgumentException e) {
				text_color = Color.CYAN;
			}
		}
		else {
			text_color = Color.CYAN;
		}

		knobDrawable = a.getDrawable (R.styleable.CircularSeekbar_knob_img);

	}

	/**
	 * Sets the inits the position.
	 * 
	 * @param pos the new inits the position
	 */
	public void setInitPosition (int pos) {

		int finalPos = (100 / max) * pos;

		if (finalPos >= associativeSubValues.length)
			finalPos -= 1;

		arc_finish_radians = calculateRadiansFromAngle ((float) associativeSubValues[finalPos]);

		if (arc_finish_radians > end_wheel)
			arc_finish_radians = end_wheel;

		mAngle = calculateAngleFromRadians (arc_finish_radians > end_wheel ? end_wheel : arc_finish_radians);

		pointerPosition = calculatePointerPosition (mAngle);

		text = String.valueOf (calculateTextFromAngle (arc_finish_radians));

		invalidate ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw (Canvas canvas) {

		canvas.translate (mTranslationOffset, mTranslationOffset);

		canvas.drawArc (mColorWheelRectangle, start_arc + 270, end_wheel - (start_arc), false, mColorWheelPaint);

		canvas.drawArc (mColorWheelRectangle, start_arc + 270, (arc_finish_radians) > (end_wheel) ? end_wheel - (start_arc) : arc_finish_radians - start_arc, false, mArcColor);

		if (mPointerRadius != 0) {

			canvas.drawCircle (pointerPosition[0], pointerPosition[1], mPointerRadius, mPointerHaloPaint);
			canvas.drawCircle (pointerPosition[0], pointerPosition[1], (float) (mPointerRadius / 1.1), mPointerColor);
		}
		else {

			if (knobDrawable != null) {

				Bitmap b = ((BitmapDrawable) knobDrawable).getBitmap ();
				canvas.drawBitmap (b, pointerPosition[0] - convertDPToPX (18), pointerPosition[1] - convertDPToPX (18), null);
			}
		}

		Rect bounds = new Rect ();
		textPaint.getTextBounds (text, 0, text.length (), bounds);

		if (show_text)
			canvas.drawText (text, (mColorWheelRectangle.centerX ()) - (textPaint.measureText (text) / 2), mColorWheelRectangle.centerY () + bounds.height () / 2, textPaint);

	}

	/**
	 * Convert dp to px.
	 * 
	 * @param dp the dp
	 * @return the int
	 */
	private int convertDPToPX (int dp) {

		float density = getResources ().getDisplayMetrics ().density;
		return Math.round ((float) dp * density);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

		int height = getDefaultSize (getSuggestedMinimumHeight (), heightMeasureSpec);
		int width = getDefaultSize (getSuggestedMinimumWidth (), widthMeasureSpec);
		int min = Math.min (width, height);
		setMeasuredDimension (min, min);

		mTranslationOffset = min * 0.5f;

		if (mPointerRadius == 0)
			mColorWheelRadius = mTranslationOffset - 48;
		else
			mColorWheelRadius = mTranslationOffset - mPointerRadius;

		mColorWheelRectangle.set (-mColorWheelRadius, -mColorWheelRadius, mColorWheelRadius, mColorWheelRadius);

		mColorCenterHaloRectangle.set (-mColorWheelRadius / 2, -mColorWheelRadius / 2, mColorWheelRadius / 2, mColorWheelRadius / 2);

		pointerPosition = calculatePointerPosition (mAngle);

	}

	/**
	 * Calculate text from angle.
	 * 
	 * @param angle the angle
	 * @return the int
	 */
	private int calculateTextFromAngle (float angle) {

		float m = angle - start_arc;
		float f = (float) ((end_wheel - start_arc) / m);

		return (int) (max / f);
	}

	/**
	 * Calculate text from start angle.
	 * 
	 * @param angle the angle
	 * @return the int
	 */
	private int calculateTextFromStartAngle (float angle) {

		float m = angle;

		float f = (float) ((end_wheel - start_arc) / m);

		return (int) (max / f);
	}

	/**
	 * Calculate angle from text.
	 * 
	 * @param position the position
	 * @return the double
	 */
	private double calculateAngleFromText (int position) {

		if (position == 0)
			return (float) 50;

		else if (position == max)
			return (float) 310;

		double f = (double) max / (double) position;

		double f_r = 360 / f;

		double ang = f_r + 90;

		return ang;

	}

	/**
	 * Calculate radians from angle.
	 * 
	 * @param angle the angle
	 * @return the int
	 */
	private int calculateRadiansFromAngle (float angle) {

		float unit = (float) (angle / (2 * Math.PI));
		if (unit < 0) {
			unit += 1;
		}
		int radians = (int) ((unit * 360) - ((360 / 4) * 3));
		if (radians < 0)
			radians += 360;
		return radians;
	}

	/**
	 * Calculate angle from radians.
	 * 
	 * @param radians the radians
	 * @return the float
	 */
	private float calculateAngleFromRadians (int radians) {

		return (float) (((radians + 270) * (2 * Math.PI)) / 360);
	}

	/**
	 * Get the selected value.
	 * 
	 * @return the value between 0 and max
	 */
	public int getValue () {

		return conversion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent (MotionEvent event) {

		// Convert coordinates to our internal coordinate system
		float x = event.getX () - mTranslationOffset;
		float y = event.getY () - mTranslationOffset;

		if (isEnabled) {

			switch (event.getAction ()) {

				case MotionEvent.ACTION_DOWN:

					float tempAngle = (float) java.lang.Math.atan2 (y, x);
					float[] tempPosition = calculatePointerPosition (tempAngle);

					if (pointerPosition[0] - tempPosition[0] < 10 && pointerPosition[1] - tempPosition[1] < 10)
						isNobTouched = true;
					else
						isNobTouched = false;

					if (isNobTouched) {
						// Check whether the user pressed on (or near) the
						// pointer
						mAngle = (float) java.lang.Math.atan2 (y, x);

						block_end = false;
						block_start = false;
						mUserIsMovingPointer = true;

						arc_finish_radians = calculateRadiansFromAngle (mAngle);

						if (arc_finish_radians > end_wheel) {
							arc_finish_radians = end_wheel;
							block_end = true;
						}

						if (!block_end && !block_start) {

							text = String.valueOf (calculateTextFromAngle (arc_finish_radians));
							pointerPosition = calculatePointerPosition (mAngle);
							invalidate ();
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (mUserIsMovingPointer) {
						mAngle = (float) java.lang.Math.atan2 (y, x);

						int radians = calculateRadiansFromAngle (mAngle);

						if (last_radians > radians && radians < (360 / 6) && x > lastX && last_radians > (360 / 6)) {

							if (!block_end && !block_start)
								block_end = true;
						}
						else if (last_radians >= start_arc && last_radians <= (360 / 4) && radians <= (360 - 1) && radians >= ((360 / 4) * 3) && x < lastX) {
							if (!block_start && !block_end)
								block_start = true;
						}
						else if (radians >= end_wheel && !block_start && last_radians < radians) {
							block_end = true;
						}
						else if (radians < end_wheel && block_end && last_radians > end_wheel) {
							block_end = false;
						}
						else if (radians < start_arc && last_radians > radians && !block_end) {
							block_start = true;
						}
						else if (block_start && last_radians < radians && radians > start_arc && radians < end_wheel) {
							block_start = false;
						}

						if (block_end) {

							arc_finish_radians = end_wheel - 1;
							text = String.valueOf (max);
							mAngle = calculateAngleFromRadians (arc_finish_radians);
							pointerPosition = calculatePointerPosition (mAngle);
						}
						else if (block_start) {

							arc_finish_radians = start_arc;
							mAngle = calculateAngleFromRadians (arc_finish_radians);
							text = String.valueOf (0);
							pointerPosition = calculatePointerPosition (mAngle);
						}
						else {

							arc_finish_radians = calculateRadiansFromAngle (mAngle);
							text = String.valueOf (calculateTextFromAngle (arc_finish_radians));
							pointerPosition = calculatePointerPosition (mAngle);
						}
						invalidate ();
						if (mOnCircleSeekBarChangeListener != null)
							mOnCircleSeekBarChangeListener.onProgressChanged (this, Integer.parseInt (text), true);

						last_radians = radians;

					}
					break;
				case MotionEvent.ACTION_UP:
					mUserIsMovingPointer = false;
					break;
			}
			// Fix scrolling
			if (event.getAction () == MotionEvent.ACTION_MOVE && getParent () != null) {
				getParent ().requestDisallowInterceptTouchEvent (true);
			}
			lastX = x;

			return true;
		}
		return false;
	}

	/**
	 * Calculate the pointer's coordinates on the color wheel using the supplied
	 * angle.
	 * 
	 * @param angle The position of the pointer expressed as angle (in rad).
	 * 
	 * @return The coordinates of the pointer's center in our internal
	 *         coordinate system.
	 */
	private float[] calculatePointerPosition (float angle) {

		float x = (float) (mColorWheelRadius * Math.cos (angle));
		float y = (float) (mColorWheelRadius * Math.sin (angle));

		return new float[] { x, y };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	protected Parcelable onSaveInstanceState () {

		Parcelable superState = super.onSaveInstanceState ();

		Bundle state = new Bundle ();
		state.putParcelable (STATE_PARENT, superState);
		state.putFloat (STATE_ANGLE, mAngle);

		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	protected void onRestoreInstanceState (Parcelable state) {

		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable (STATE_PARENT);
		super.onRestoreInstanceState (superState);

		mAngle = savedState.getFloat (STATE_ANGLE);
		arc_finish_radians = calculateRadiansFromAngle (mAngle);
		text = String.valueOf (calculateTextFromAngle (arc_finish_radians));
		pointerPosition = calculatePointerPosition (mAngle);

	}

	/**
	 * Sets the on seek bar change listener.
	 * 
	 * @param l the new on seek bar change listener
	 */
	public void setOnSeekBarChangeListener (OnCircleSeekBarChangeListener l) {

		mOnCircleSeekBarChangeListener = l;
	}

	/**
	 * The listener interface for receiving onCircleSeekBarChange events. The
	 * class that is interested in processing a onCircleSeekBarChange event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addOnCircleSeekBarChangeListener<code> method. When
	 * the onCircleSeekBarChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnCircleSeekBarChangeEvent
	 */
	public interface OnCircleSeekBarChangeListener {

		/**
		 * On progress changed.
		 * 
		 * @param seekBar the seek bar
		 * @param progress the progress
		 * @param fromUser the from user
		 */
		public abstract void onProgressChanged (CircularSeekbar seekBar, int progress, boolean fromUser);

	}

}
