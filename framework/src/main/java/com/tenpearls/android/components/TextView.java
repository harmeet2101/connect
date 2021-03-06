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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.tenpearls.android.R;
import com.tenpearls.android.utilities.FontUtility;

/**
 * 
 * {@link android.widget.TextView}'s subclass to assist in overriding default
 * drawing behavior. Mainly to allow single point of change across the whole
 * application.
 * 
 * @author 10Pearls
 */
public class TextView extends android.widget.TextView {

	/**
	 * Instantiates a new text view.
	 * 
	 * @param context the context
	 */
	public TextView (Context context) {

		super (context);
		init (null, 0);
	}

	/**
	 * Instantiates a new text view.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 */
	public TextView (Context context, AttributeSet attrs) {

		super (context, attrs);
		init (attrs, 0);
	}

	/**
	 * Instantiates a new text view.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public TextView (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
		init (attrs, defStyle);
	}

	/**
	 * This method is used to read custom attribute values set from XML. For
	 * example custom font.
	 * 
	 * @param attrs Attribute set
	 * @param defStyle Def style
	 */
	public void init (AttributeSet attrs, int defStyle) {

		if (attrs == null)
			return;

		final TypedArray attributes = getContext ().obtainStyledAttributes (attrs, R.styleable.Generic, defStyle, 0);		

		setCustomTypeface (attributes);
	
		attributes.recycle ();
	}

	private void setCustomTypeface (final TypedArray attributes) {

	    String fontPathFromAssets = null;
		
		int attributeResourceValue = attributes.getResourceId (R.styleable.Generic_font_path_from_assets, -1);

		if (attributeResourceValue == -1) {
			fontPathFromAssets = attributes.getString (R.styleable.Generic_font_path_from_assets);
		}
		else {
			fontPathFromAssets = getContext ().getString (attributeResourceValue);
		}

		if(fontPathFromAssets == null){
			fontPathFromAssets = getContext().getString(R.string.application_font);	
			Typeface typeface = FontUtility.getFontFromAssets (fontPathFromAssets, getContext ());
			setTypeface (typeface, Typeface.BOLD);
		}
		else{
			Typeface typeface = FontUtility.getFontFromAssets (fontPathFromAssets, getContext ());
			setTypeface (typeface, Typeface.NORMAL);
		}
			
    }
	
	@Override
	protected void onDraw (Canvas canvas) {
	    super.onDraw (canvas);
	}
}
