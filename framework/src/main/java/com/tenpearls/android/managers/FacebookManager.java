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

import com.tenpearls.android.interfaces.FacebookRequestsListener;
import com.tenpearls.android.interfaces.FacebookSessionStatusChangeListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * This class serves as a skeleton for the Facebook Helpers. 
 * All you need to do is integrate it with the latest Facebook SDK.
 * Apart from the abstract methods, you should override the basic helper methods like getUserData etc. 
 * This class must also implement the Session.StatusCallback, Request.GraphUserCallback and Request.GraphUserListCallback from Facebook SDK and uncomment the commented interface methods. 
 * 
 * @author 10Pearls
  */

public abstract class FacebookManager <T, J> {	
	
	protected FacebookSessionStatusChangeListener<T> mFacebookSessionStatusChangeListener;
	protected FacebookRequestsListener<J>            mFacebookRequestsListener;

	/**
	 * This method gets your the user profile picture URL.
	 * @param profileID The ProfileID of the User
	 * @param width The width of the profile picture
	 * @param height The height of the profile picture
	 * 
	 * @return String The Profile Picture URL
	  */
	
	public String getProfilePictureURL (String profileID, int width, int height) {

		return "https://graph.facebook.com/" +	profileID +
				"/picture?width=" + width + 
				"&height=" + height;
	}
	
	/**
	 * This method is used to set the FacebookSessionStatusChangeListener member variable.
	 * 
	 * @param facebookSessionStatusChangeListener The FacebookSessionStatusChangeListener for Session type of Facebook SDK
	 * 
	  */
	
	public void setFacebookSessionStatusListener(FacebookSessionStatusChangeListener<T> facebookSessionStatusChangeListener) {
		this.mFacebookSessionStatusChangeListener = facebookSessionStatusChangeListener;
	}
	
	/**
	 * This method is used to set the FacebookRequestsListener member variable.
	 * 
	 * @param facebookRequestsListener The FacebookRequestsListener for GraphUser type of Facebook SDK
	 * 
	  */
	
	public void setFacebookRequestListener(FacebookRequestsListener<J> facebookRequestsListener) {
		this.mFacebookRequestsListener = facebookRequestsListener;
	}
	
	/**
	 * This method returns the active session. The implementation must call the relevant Facebook SDK methods
	 * 
	 * @param type The Class of the Session object
	 * 
	 * @return T The Active Session object
	  */
	
	public abstract T getActiveSession();
	
	/**
	 * This method is used to close the current session. The implementation must call the relevant Facebook SDK methods
	 */
	
	public abstract void closeSession ();
	
	/**
	 * This method is used to open the Facebook Session. The implementation must call the relevant Facebook SDK methods
	 * 
	 * @param activity The Activity on which the OAuth flow must begin
	 * 
	 * @return allowLoginUI If {@code true}, this will create a new Session, make it active, and open it. 
	  */
	
	public abstract void openSession (Activity activity, boolean allowLoginUI);
	
	/**
	 * This method is used to logout from Facebook. The implementation must call the relevant Facebook SDK methods
	 */
	
	public abstract void logout ();
	
	/**
	 * This method must be called by the Activity that is opening the Facebook Session on its onActivityResult callback method. The implementation must call the relevant Facebook SDK methods
	 * 
	 * @param currentActivity The Activity that is opening the Facebook Session
	 */
	
	public abstract void onActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data);
	
	/**
	 * This method is used to get the Access token of the currently logged in user. The implementation must call the relevant Facebook SDK methods
	 */
	
	public abstract String getAccessToken ();
	
	/**
	 * This method is used to request for the logged in User Data.
	 * setFacebookRequestListener must be used to set the Request Listener before calling this method. 
	 * The implementation must call the relevant Facebook SDK methods 
	 * <br/><br/>Following is the code-snippet:<br/>
	 * {@code Request.executeMeRequestAsync (getActiveSession (), this);}
	 */
	
	public void getUserData () {
		notImplementedState();
	}
	
	/**
	 * This method is used to request for the logged in User's Friends List.
	 * setFacebookRequestListener must be used to set the Request Listener before calling this method. 
	 * The implementation must call the relevant Facebook SDK methods	 
	 * <br/><br/>Following is the code-snippet:<br/>
	 * {@code Request friendRequest = Request.newMyFriendsRequest (getActiveSession (), this);}
	 * <br/>{@code Bundle params = new Bundle ();}
	 * <br/>{@code params.putString ("fields", "id, name, first_name, last_name");}
	 * <br/>{@code friendRequest.setParameters (params);}
	 * <br/>{@code friendRequest.executeAsync ();)
	 * @param context The context object to be used to open Facebook Session from cache.
	 */
	
	public void getUserFriends (Context context) throws Exception {
		notImplementedState();
	}
	
	/**
	 * This method is used to ask for write permission before performing any write task.	 
	 * The implementation must call the relevant Facebook SDK methods
	 * <br/><br/>Following is the code-snippet:<br/>
	 * {@code Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest ((Activity) context, Arrays.asList ("publish_stream"));}
	 * <br/>{@code session.requestNewPublishPermissions (newPermissionsRequest);}
	 * <br/>{@code newPermissionsRequest.setCallback (this);}
	 * @param context The context object to be used to open Facebook Session from cache.
	 */
	
	public void askForWritePermissions (Context context) {
		notImplementedState();
	}
		
	/**
	 * This method is used to open Publish feed dialog.	 
	 * The implementation must call the relevant Facebook SDK methods
	 * <br/><br/>Following is the code-snippet:<br/>
	 * <br/>{@code Bundle params = new Bundle ();}
	 * <br/>{@code WebDialog feedDialog = (new WebDialog.FeedDialogBuilder (context, session, params))}
	 * <br/>{@code .setOnCompleteListener (new OnCompleteListener ()} {

	 * <br/>{@code  @Override}
	 * <br/>{@code  public void onComplete (Bundle values, FacebookException error)} {
	 * <br/>{@code  if (error == null) }{
	 * <br/>				// When the story is posted, echo the success
	 * <br/>				// and the post Id.
	 * <br/>		} {@code else if (error instanceof FacebookOperationCanceledException)} {
	 * <br/>				// User clicked the "x" button
	 * <br/>        } {@code else } {
	 * <br/> 				// Generic, ex: network error
	 * <br/>		}
	 * <br/>	}
	 * <br/>	}{@code).build ();}
	 * <br/>{@code feedDialog.show ();}
	 * @param name The name parameter for the bundle to be passed in FeedDialogBuilder
	 * @param caption The caption parameter for the bundle to be passed in FeedDialogBuilder
	 * @param description The description parameter for the bundle	to be passed in FeedDialogBuilder 
	 * @param picture The picture parameter for the bundle to be passed in FeedDialogBuilder
	 * @param context The context object to be used to open Facebook Session from cache.
	 */
	
	public void openPublishFeedDialog (String name, String caption, 
			String description, String picture, Context context) {
		notImplementedState();
	}
	
	/**
	 * This method is used to post an Image to the User's Wall
	 * The implementation must call the relevant Facebook SDK methods
	 * <br/><br/>Following is the code-snippet:<br/>
	 * <br/>{@code Bundle params = new Bundle ();}
	 * <br/>{@code params.putString ("name", name);}
	 * <br/>{@code params.putByteArray ("picture", Utils.getBytesFromBitmap (picture));}
	 * <br/>{@code Request request = new Request (session, "me/photos", params, HttpMethod.POST, new Request.Callback ()} {
	 * <br/>{@code @Override}
	 * <br/>{@code public void onCompleted (Response response) }{
	 * <br/>{@code if response.getError ()!= null) }{
	 * <br/>		//Display Error
	 * <br/> }
	 * <br/>{@code else }{
	 * <br/>		//No Error
	 * <br/> }
	 * <br/> }
	 * </br> }{@code );}
	 * @param name The name of the image to be posted
	 * @param picture The Bitmap object to be posted
	 * @param context The context object to be used to open Facebook Session from cache.
	 */
	
	public void postImageToWall (String name, Bitmap picture, Context context) {
		notImplementedState();
	}
	
	private void notImplementedState() {
		throw new IllegalStateException("NOT IMPLEMENTED");
	}
	
	/* -------------------Session.StatusCallback-------------------
	 * @Override
	 * public void call (Session session, SessionState state, Exception exception) {
	 *	
	 *	if (mFacebookSessionStatusChangeListener != null)
	 *		mFacebookSessionStatusChangeListener.onSessionStatusChange (session, exception);
	 * }	  
	*/
	
	/*  -------------------Request.GraphUserCallback-------------------
	 * @Override
	 * public void onCompleted (GraphUser user, Response response) {
	 * 
	 * if (mFacebookRequestsListener != null)
	 *    mFacebookRequestsListener.onGetUserCallCompleted (user);
	 * }
	*/

	/*  -------------------Request.GraphUserListCallback-------------------
	 * @Override
	 * public void onCompleted (List<GraphUser> users, Response response) {
	 *  
	 * 	if (facebookFriendsRequestListener != null)
	 *		facebookFriendsRequestListener.onGetFriendsCallCompleted (users);
	 * }
	*/
	
	

}
