package com.mboconnect.services.response;

import java.util.ArrayList;

public abstract class BaseResponse<T> {

	protected ArrayList<T> list           = new ArrayList<T> ();

	protected String       key;
	protected String       value;
	protected final String KEY_META       = "meta";
	protected final String KEY_DATA       = "data";
	protected final String KEY_ATTRIBUTES = "attributes";
	protected final String KEY_ITEMS      = "items";

	public abstract void set (String input);

	public ArrayList<T> getList () {

		return list;
	}

	public String getString () {

		if (this.value == null)
			return "";

		return this.value;
	}
}
