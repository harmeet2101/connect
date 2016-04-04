package com.mboconnect.entities;

/**
 * Created by tahir on 29/07/15.
 */
public class MessageDate extends BaseEntity {

	String date;

	@Override
	public void set (String jsonString) {

	}

	public String getDate () {

		return date;
	}

	public void setDate (String date) {

		this.date = date;
	}
}
