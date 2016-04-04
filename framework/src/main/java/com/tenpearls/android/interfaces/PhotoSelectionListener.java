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
package com.tenpearls.android.interfaces;

import android.graphics.Bitmap;

/**
 * Interface definition for callback to be invoked in Photo selection flow.
 * 
 * @author 10Pearls
 * 
 */
public interface PhotoSelectionListener {

	/**
	 * Called when the Photo is received successfully.
	 * 
	 * @param photo The Bitmap photo that was selected.
	 */
	public void onPhotoSelected (Bitmap photo);
	
	/**
	 * Called when there was an error while picking the photo.
	 * 
	 * @param exception The exception generated.
	 */
	public void onPhotoSelectionFailure (Exception exception);
}
