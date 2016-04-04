package com.mboconnect.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.constants.KeyConstants;
import com.tenpearls.android.utilities.JsonUtility;

import java.util.ArrayList;

/**
 * Created by tahir on 24/07/15.
 */
public class Conversation extends BaseEntity {

	String            id;
	String            status;

	ArrayList<Member> memberList = new ArrayList<> ();

	@Override
	public void set (String jsonString) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();
			JsonArray jsonDataArray = jsonObject.getAsJsonArray (KeyConstants.KEY_DATA);
			JsonObject jsonConversationObject = jsonDataArray.get (0).getAsJsonObject ();
			JsonArray jsonMemberArray = jsonConversationObject.getAsJsonArray (KeyConstants.KEY_MEMBERS);
			this.id = JsonUtility.getString (jsonConversationObject, KeyConstants.KEY_ID);
			this.status = JsonUtility.getString (jsonConversationObject, KeyConstants.KEY_STATUS);

			for (int i = 0; i < jsonMemberArray.size (); i++) {

				Member member = new Member ();
				JsonObject jsonMemberObject = jsonMemberArray.get (i).getAsJsonObject ();
				JsonObject jsonUserObject = jsonMemberObject.getAsJsonObject (KeyConstants.KEY_USER);
				if (jsonUserObject != null) {
					JsonObject nameObject = jsonUserObject.getAsJsonObject (KeyConstants.KEY_NAME);
					setMemberData (jsonUserObject, member, nameObject);
					memberList.add (member);
				}

			}
		}
		catch (Exception e) {
			e.printStackTrace ();
		}

	}

	private void setMemberData (JsonObject jsonUserObject, Member member, JsonObject nameObject) {

		setMemberType (jsonUserObject, member);
		String firstName = JsonUtility.getString (nameObject, KeyConstants.KEY_FIRST);
		String middleName = JsonUtility.getString (nameObject, KeyConstants.KEY_MIDDLE);
		String lastName = JsonUtility.getString (nameObject, KeyConstants.KEY_LAST);
		member.setName (firstName + " " + middleName + " " + lastName);
	}

	private void setMemberType (JsonObject jsonConversationObject, Member member) {

		if (JsonUtility.getString (jsonConversationObject, KeyConstants.KEY_ROLE_IN_CONVERSATION).equalsIgnoreCase (KeyConstants.KEY_MANAGER)) {

			member.setMemberType (EnumConstants.MemberType.MANAGER);

		}
		else if (JsonUtility.getString (jsonConversationObject, KeyConstants.KEY_ROLE_IN_CONVERSATION).equalsIgnoreCase (KeyConstants.KEY_ASSOCIATE)) {

			member.setMemberType (EnumConstants.MemberType.ASSOCIATE);
		}
		else {

			member.setMemberType (EnumConstants.MemberType.AUTHOR);
		}
	}

	public ArrayList<Member> getMemberList () {

		return memberList;
	}

	public void setMemberList (ArrayList<Member> memberList) {

		this.memberList = memberList;
	}

	public String getStatus () {

		return status;
	}

	public void setStatus (String status) {

		this.status = status;
	}

	public String getConversationId () {

		return id;
	}

	public void setConversationId (String id) {

		this.id = id;
	}

	public class Member {

		String                   name;
		EnumConstants.MemberType memberType;

		public String getName () {

			return name;
		}

		public void setName (String name) {

			this.name = name;
		}

		public EnumConstants.MemberType getMemberType () {

			return memberType;
		}

		public void setMemberType (EnumConstants.MemberType memberType) {

			this.memberType = memberType;
		}
	}
}
