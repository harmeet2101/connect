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
package com.tenpearls.android.utilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This utility methods related to manipulating images should go inside this
 * class.
 * 
 * @author 10Pearls
 * 
 */
public class ImageUtility {

	/**
	 * Gets a bitmap object from an image resource. Also applies compression to
	 * reduce in memory size of the image.
	 * 
	 * @param res Resources. You can get resources by doing
	 *            {@code getResources()} on a Context or Activity.
	 * @param resId Resource ID for the required image.
	 * @return Compressed bitmap object against the image resource.
	 */
	public static Bitmap decodeBitmapFromResource (Resources resources, int resourceId) {

		final BitmapFactory.Options options = new BitmapFactory.Options ();

		options.inPurgeable = true;
		options.inScaled = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeResource (resources, resourceId, options);
	}

}
