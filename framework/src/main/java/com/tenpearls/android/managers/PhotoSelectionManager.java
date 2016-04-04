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
package com.tenpearls.android.managers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.tenpearls.android.interfaces.PhotoSelectionListener;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

/**
 * This class is a PhotoSelection Manager that has made regular photo selection
 * related tasks simpler. All you need to do is call a simple helper method to
 * select the source of photo selection. It has added functionality of image
 * cropping.
 * 
 * @author 10Pearls
 */
public class PhotoSelectionManager {

	/**
	 * This Request Code for Image Gallery Intent.
	 */
	static final int       GALLERY_REQUEST_CODE       = 502;

	/**
	 * This Request Code for Camera Intent.
	 */
	static final int       IMAGE_CAPTURE_REQUEST_CODE = 504;

	/**
	 * This Request Code for Crop Intent.
	 */
	static final int       CROP_REQUEST_CODE          = 505;

	/**
	 * The Activity from where you want to open the intent.
	 */
	Activity               mActivity;

	/**
	 * The listener which will receive the Photo selection callback methods.
	 */
	PhotoSelectionListener mPhotoSelectionListener;

	/**
	 * If {@code true}, cropping is enabled, {@code false} otherwise.
	 */

	boolean                mShouldCropImage;

	/**
	 * The aspect ratio for x-axis. Default value is 1.0.
	 */
	float                  mAspectX                   = 1;

	/**
	 * The aspect ratio for y-axis. Default value is 1.0.
	 */
	float                  mAspectY                   = 1;

	/**
	 * The width of the output image in pixels.
	 */
	int                    mOutputWidth               = 118;

	/**
	 * The height of the output image in pixels.
	 */
	int                    mOutputHeight              = 118;

	/**
	 * Constructor.
	 * 
	 * @param activity The Activity from where you want to open the intent.
	 */
	public PhotoSelectionManager (Activity activity) {

		this.mActivity = activity;
		this.mShouldCropImage = false;
	}

	/**
	 * Constructor.
	 * 
	 * @param activity The Activity from where you want to open the intent
	 * @param shouldCropImage Set {@code true} to enable cropping, {@code false}
	 *            otherwise
	 */
	public PhotoSelectionManager (Activity activity, boolean shouldCropImage) {

		this.mActivity = activity;
		this.mShouldCropImage = shouldCropImage;
	}

	/**
	 * Set a listener that will receive Photo Selection callback methods.
	 * 
	 * @param photoSelectionListener The listener that will receive Photo
	 *            Selection callback methods
	 */
	public void setPhotoSelectionListener (PhotoSelectionListener photoSelectionListener) {

		this.mPhotoSelectionListener = photoSelectionListener;
	}

	/**
	 * Set to enable or disable cropping.
	 * 
	 * @param shouldCropImage Set {@code true} to enable cropping, {@code false}
	 *            otherwise
	 */
	public void setShouldCropImage (boolean shouldCropImage) {

		this.mShouldCropImage = shouldCropImage;
	}

	/**
	 * Set crop parameters.
	 * 
	 * @param aspectX The aspect ratio for x-axis. Default value is 1.0
	 * @param aspectY The aspect ratio for Y-axis. Default value is 1.0
	 * @param outputWidth The width of the output image in pixels
	 * @param outputHeight The height of the output image in pixels
	 */
	public void setCropParameters (float aspectX, float aspectY, int outputWidth, int outputHeight) {

		this.mAspectX = aspectX;
		this.mAspectY = aspectY;
		this.mOutputWidth = outputWidth;
		this.mOutputHeight = outputHeight;
	}

	/**
	 * Call this method to select the photo using Image Gallery.
	 */
	public void openGallery () {

		checkForActivityState ();

		Intent openCameraRollIntent = new Intent (Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mActivity.startActivityForResult (openCameraRollIntent, GALLERY_REQUEST_CODE);
	}

	/**
	 * Call this method to select the photo using Camera.
	 */
	public void openCamera () {

		checkForActivityState ();

		Intent takePhotoIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
		mActivity.startActivityForResult (takePhotoIntent, IMAGE_CAPTURE_REQUEST_CODE);
	}

	/**
	 * This method must be called in the onActivityResult method of the Activity
	 * used for this manager.
	 * 
	 * See {@link Activity} for more details
	 * 
	 * @param requestCode the request code
	 * @param resultCode the result code
	 * @param data the data
	 * @throws ActivityNotFoundException the activity not found exception
	 * @throws OutOfMemoryError the out of memory error
	 * @throws Exception the exception
	 */
	public void onActivityResult (int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		checkForActivityState ();

		try {
			if (mShouldCropImage && requestCode != CROP_REQUEST_CODE) {
				Uri imageUri = getImageUriFromData (data);
				openCrop (imageUri);
				return;
			}

			Bitmap photo = getBitmapFromIntent (data);
			mPhotoSelectionListener.onPhotoSelected (photo);

		}
		catch (ActivityNotFoundException anfe) {
			anfe.printStackTrace ();
			mPhotoSelectionListener.onPhotoSelectionFailure(new ActivityNotFoundException ("PhotoSelectionManager >> Your device doesn't support the crop action."));
		}
		catch (Exception ex) {
			ex.printStackTrace ();
			mPhotoSelectionListener.onPhotoSelectionFailure(new Exception ("PhotoSelectionManager >> There was some problem while getting the photo. Try again later."));
		}
		catch (OutOfMemoryError error) {
			error.printStackTrace();
			mPhotoSelectionListener.onPhotoSelectionFailure(new Exception ("PhotoSelectionManager >> There was some problem while getting the photo. Try again later."));
		}
	}

	/**
	 * This method returns a Bitmap object from the Intent that is returned from
	 * a Camera or Gallery.
	 * 
	 * @param data The Intent object returned from Camera or Gallery
	 * @return the bitmap from intent
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Bitmap getBitmapFromIntent (Intent data) throws FileNotFoundException, IOException {

		Bitmap photo = null;
		photo = (Bitmap) data.getExtras ().get ("data");
		if (photo != null) {
			return photo;
		}

		Uri imageUri = data.getData ();
		photo = MediaStore.Images.Media.getBitmap (mActivity.getContentResolver (), imageUri);
		return photo;
	}

	/**
	 * This method returns a Uri object from the Intent that is returned from a
	 * Camera or Gallery.
	 * 
	 * @param data The Intent object returned from Camera or Gallery
	 * @return the image uri from data
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Uri getImageUriFromData (Intent data) throws FileNotFoundException, IOException {

		Uri imageUri = null;
		imageUri = data.getData ();
		if (imageUri != null) {
			return imageUri;
		}

		Bitmap picture = getBitmapFromIntent (data);
		if (picture == null) {
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream ();
		picture.compress (Bitmap.CompressFormat.JPEG, 100, stream);

		String path = Images.Media.insertImage (mActivity.getContentResolver (), picture, "campic", null);
		imageUri = Uri.parse (path);
		return imageUri;
	}

	/**
	 * Call this method to open the Crop activity if available.
	 * 
	 * @param picUri the pic uri
	 * @throws ActivityNotFoundException the activity not found exception
	 */
	public void openCrop (Uri picUri) throws ActivityNotFoundException {

		Intent cropIntent = new Intent ("com.android.camera.action.CROP");
		cropIntent.setDataAndType (picUri, "image/*");
		cropIntent.putExtra ("crop", "true");
		cropIntent.putExtra ("aspectX", mAspectX);
		cropIntent.putExtra ("aspectY", mAspectY);
		cropIntent.putExtra ("outputX", mOutputWidth);
		cropIntent.putExtra ("outputY", mOutputHeight);
		cropIntent.putExtra ("return-data", true);
		mActivity.startActivityForResult (cropIntent, CROP_REQUEST_CODE);
	}

	/**
	 * This method asserts that the Activity has been set. It throws an
	 * {@link IllegalStateException} if the Activity has not been set.
	 */
	private void checkForActivityState () {

		if (mActivity == null) {
			throw new IllegalStateException ("PhotoSelectionManager >> An Activity must be set using the constructor");
		}
	}
}
