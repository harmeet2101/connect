package com.mboconnect.config;

public class AppConfig {

	static AppConfig instance;

	public static AppConfig getInstance () {

		if (instance == null)
			instance = new AppConfig ();

		return instance;
	}

}
