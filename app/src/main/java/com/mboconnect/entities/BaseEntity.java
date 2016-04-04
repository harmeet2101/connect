package com.mboconnect.entities;

public abstract class BaseEntity {

	final String DATE_TIME_FORMAT = "yyyy-mm-ddThh:mm:ssZ";

	public abstract void set (String jsonString);
}
