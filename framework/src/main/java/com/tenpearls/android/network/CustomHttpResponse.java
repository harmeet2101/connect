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
package com.tenpearls.android.network;

import java.util.Date;

import org.apache.http.client.methods.HttpRequestBase;

import com.tenpearls.android.interfaces.HttpResponseListener;

/**
 * Used to represent object returned from the network. This class is used in
 * conjunction with {@link HttpResponseListener}.
 * 
 * @author 10Pearls
 * 
 */
public class CustomHttpResponse {

	/** The method name. */
	String          methodName;

	/** The response. */
	Object          response;

	/** The status code. */
	int             statusCode;

	/** The request type. */
	HttpRequestBase requestType;

	/** The response time. */
	Date            responseTime;

	/**
	 * Creates a new instance with the provided arguments.
	 * 
	 * @param response The object that needs to be passed to the implementor of
	 * @param methodName The API method that was accessed.
	 * @param statusCode The Status code returned from the server.
	 */
	public CustomHttpResponse (Object response, String methodName, int statusCode) {

		this.response = response;
		this.methodName = methodName;
		this.statusCode = statusCode;
		this.responseTime = new Date ();
	}

	/**
	 * Creates a new instance with the provided arguments.
	 * 
	 * @param response The object that needs to be passed to the implementor of
	 * @param methodName The API method that was accessed.
	 * @param statusCode The Status code returned from the server.
	 * @param requestType HTTP method type HttpGet/HttpPost/HttpDelete/HttpPut
	 */
	public CustomHttpResponse (Object response, String methodName, int statusCode, HttpRequestBase requestType) {

		this.response = response;
		this.methodName = methodName;
		this.statusCode = statusCode;
		this.requestType = requestType;
		this.responseTime = new Date ();
	}

	/**
	 * Creates a new instance with the provided arguments. To be used only for
	 * scenarios where you want to represent an error.
	 * 
	 * @param response The error message encountered during the lifetime of the
	 *            network call.
	 * @param methodName The API method that was accessed.
	 * @param statusCode The Status code returned from the server.
	 */
	public CustomHttpResponse (String errorMessage, String methodName, int statusCode) {

		this.response = errorMessage;
		this.methodName = methodName;
		this.statusCode = statusCode;
		this.responseTime = new Date ();
	}

	/**
	 * Creates a new instance with the provided arguments. To be used only for
	 * scenarios where you want to represent an error.
	 * 
	 * @param response The error message encountered during the lifetime of the
	 *            network call.
	 * @param methodName The API method that was accessed.
	 */
	public CustomHttpResponse (String errorMessage, String methodName) {

		this.response = errorMessage;
		this.methodName = methodName;
		this.responseTime = new Date ();
	}

	/**
	 * Getter for request type that was used to access the network resource.
	 * 
	 * @return HTTP method used to access the network resource.
	 */
	public HttpRequestBase getRequestType () {

		return requestType;
	}

	/**
	 * Getter for status code that was returned from the server.
	 * 
	 * @return HTTP status code
	 */
	public int getStatusCode () {

		return statusCode;
	}

	/**
	 * Getter for the API method that was accessed.
	 * 
	 * @return API method
	 */
	public String getMethodName () {

		return methodName;
	}

	/**
	 * Getter for the object that was prepared after processing server's
	 * response.
	 * 
	 * @return Object created from server's response.
	 */
	public Object getResponse () {

		return response;
	}

	/**
	 * Getter for the time at which the respons arrived.
	 * 
	 * @return Response arrival time
	 */
	public Date getResponseTime () {

		return responseTime;
	}

	/**
	 * Used this method when the current instance is used to represent an error.
	 * 
	 * @return String error message.
	 */
	public String getErrorMessage () {

		return response.toString ();
	}
}
