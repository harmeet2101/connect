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
package com.tenpearls.android.entities;

import com.androauth.oauth.OAuth20Token;

/**
 * An OAuth 2.0 Token class that encapsulates all the token fields.
 * It is basically a wrapper on OAuth20Token class with additional fields for refreshing the token.
 * 
 * @author 10Pearls
 */

public class OAuth20TokenExt extends OAuth20Token {

	/**
	 * The time till token expires in seconds
	 */
	
	private int    expiresIn;

	/**
	 * Constructor
	 * 
	 * @param accessToken The Access Token returned by the OAuth API
	 * @param refreshToken The Refresh Token returned by the OAuth API
	 */

	public OAuth20TokenExt (String accessToken, String refreshToken) {

		super (accessToken, refreshToken);
	}
	
	/**
	 * Constructor
	 * 
	 * @param accessToken The Access Token returned by the OAuth API
	 */

	public OAuth20TokenExt (String accessToken) {

		super (accessToken);
	}
	
	/**
	 * Constructor
	 * 
	 * @param accessToken The Access Token returned by the OAuth API
	 * @param refreshToken The Refresh Token returned by the OAuth API
	 * @param expiresIn The time till token expires in seconds
	 */

	public OAuth20TokenExt (String accessToken, String refreshToken, int expiresIn) {

		super (accessToken, refreshToken);
		this.expiresIn = expiresIn;
	}

	/**
	 * This method gets you to the time till token expires in seconds
	 * 
	 * @return int The time till token expires in seconds
	 */
	
	public int getExpiresIn () {

		return this.expiresIn;
	}

	/**
	 * Set the time till token expiration through this method
	 * 
	 * @param expiresIn The time till token expires in seconds
	 */
	
	public void setExpiresIn (int expiresIn) {

		this.expiresIn = expiresIn;
	}
}