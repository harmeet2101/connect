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
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.tenpearls.android.network.VolleyManager;

/**
 * Subclass of {@link android.widget.ImageView} with the added functionality of
 * seamlessly loading images from the internet.
 * 
 * @author 10Pearls
 * 
 */
public class ImageView extends NetworkImageView {

	/**
	 * Instantiates a new image view.
	 * 
	 * @param context the context
	 */
	public ImageView (Context context) {

		super (context);
	}

	/**
	 * Instantiates a new image view.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 */
	public ImageView (Context context, AttributeSet attrs) {

		super (context, attrs);
	}

	/**
	 * Instantiates a new image view.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public ImageView (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
	}

	/**
	 * Set the default placeholder image. It will get displayed when an image is
	 * being loaded or there was an error while fetching it from the network.
	 * 
	 * @param imageResourceId Drawable resource ID
	 */
	public void setPlaceHolderImageResourceId (int imageResourceId) {

		setImageResource (imageResourceId);
		setDefaultImageResId (imageResourceId);
		setErrorImageResId (imageResourceId);
	}

	/**
	 * Loads image from the network against the specified URL.
	 * 
	 * @param imageUrl A valid URL
	 */
	public void loadImageWithUrl (String imageUrl) {

		setImageUrl (imageUrl, VolleyManager.getInstance ().getImageLoader ());
	}
}
