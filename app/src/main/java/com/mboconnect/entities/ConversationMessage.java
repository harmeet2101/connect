package com.mboconnect.entities;

import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.constants.ServiceConstants;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by tahir on 28/07/15.
 */
public class ConversationMessage extends BaseEntity {

	String  id;
	String  body;
	String  dateCreated;
	String  imgUrl;
	String  userName;
	Boolean isRead;
	Boolean isMine;

	@Override
	public void set (String jsonString) {

		JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();
		this.id = JsonUtility.getString (jsonObject, KeyConstants.KEY_ID);
		this.body = JsonUtility.getString (jsonObject, KeyConstants.KEY_BODY);
		this.dateCreated = JsonUtility.getString (jsonObject, KeyConstants.KEY_CREATED_AT);
		this.imgUrl = ServiceConstants.SERVICE_IMAGE + JsonUtility.getString (jsonObject, KeyConstants.KEY_IMAGE_URL);
		this.imgUrl=this.imgUrl.replace("/v1","");
		this.userName = JsonUtility.getString (jsonObject, KeyConstants.KEY_USER);
		this.isRead = JsonUtility.getBoolean (jsonObject, KeyConstants.KEY_IS_READ);
		this.isMine = JsonUtility.getBoolean (jsonObject, KeyConstants.KEY_IS_MINE);

	}

	@Override
	public boolean equals (Object obj) {

		if (!(obj instanceof ConversationMessage))
			return false;
		if (obj == this)
			return true;
		return this.id.equals (((ConversationMessage) obj).id);
	}

	@Override
	public int hashCode () {

		return this.id.length ();
	}

	public String getBody () {

		return body;
	}

	public void setBody (String body) {

		this.body = body;
	}

	public String getImgUrl () {

		return imgUrl;
	}

	public void setImgUrl (String imgUrl) {

		this.imgUrl = imgUrl;
	}

	public String getUserName () {

		return userName;
	}

	public void setUserName (String userName) {

		this.userName = userName;
	}

	public Boolean getIsRead () {

		return isRead;
	}

	public void setIsRead (Boolean isRead) {

		this.isRead = isRead;
	}

	public Boolean getIsMine () {

		return isMine;
	}

	public void setIsMine (Boolean isMine) {

		this.isMine = isMine;
	}

	public String getDateCreated () {

		return dateCreated;
	}

	public void setDateCreated (String dateCreated) {

		this.dateCreated = dateCreated;
	}

	public String getConversationMessageId () {

		return id;
	}

	public void setId (String id) {

		this.id = id;
	}

}
