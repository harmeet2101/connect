package com.mboconnect.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.mboconnect.R;
import com.mboconnect.constants.AppConstants;

/**
 * Created by ali.mehmood on 6/12/2015.
 */
public class ImageButton extends ImageView {

	public ImageButton (Context context) {

		super (context);
		init (null, 0);
	}

	public ImageButton (Context context, AttributeSet attrs) {

		super (context, attrs);
		init (attrs, 0);
	}

	public ImageButton (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
		init (attrs, defStyle);
	}

	public void init (AttributeSet attrs, int defStyle) {

		if (attrs == null)
			return;

		final TypedArray attributes = getContext ().obtainStyledAttributes (attrs, R.styleable.CustomImageButton, defStyle, 0);

		setCutomStateDrawable (attributes);

		attributes.recycle ();
	}

	private void setCutomStateDrawable (TypedArray attributes) {

		int focusedDrawable = attributes.getResourceId (R.styleable.CustomImageButton_focusedDrawable, -1);
		int unFocusedDrawable = attributes.getResourceId (R.styleable.CustomImageButton_unFocusedDrawable, -1);
		int selectedDrawable = attributes.getResourceId (R.styleable.CustomImageButton_selectedDrawable, -1);

		if (focusedDrawable == -1 && unFocusedDrawable == -1) {

			return;
		}

		StateListDrawable stateListDrawable = new StateListDrawable ();

		if (focusedDrawable != -1) {

			stateListDrawable.addState (new int[] { android.R.attr.state_pressed }, getResources ().getDrawable (focusedDrawable));
			stateListDrawable.addState (new int[] { android.R.attr.state_focused }, getResources ().getDrawable (focusedDrawable));
		}

		if (unFocusedDrawable != -1) {

			stateListDrawable.addState (new int[] {}, getResources ().getDrawable (unFocusedDrawable));
		}

		if (selectedDrawable != -1) {

			stateListDrawable.addState (new int[] { android.R.attr.state_selected }, getResources ().getDrawable (selectedDrawable));
		}

		setImageDrawable (stateListDrawable);
		setClickable (true);
	}

	@Override
	public void setEnabled (boolean enabled) {

		setAlpha (enabled ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
		super.setEnabled (enabled);
	}
}
