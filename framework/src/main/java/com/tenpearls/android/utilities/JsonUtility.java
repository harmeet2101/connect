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
package com.tenpearls.android.utilities;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * <p>
 * This class is responsible for conversion of Json strings into Json objects
 * and vice versa. It internally uses <a
 * href="https://code.google.com/p/google-gson/">Google's GSON library</a> to
 * parse Json strings into Json objects.
 * 
 * @author 10Pearls
 * 
 */
public class JsonUtility {

	/**
	 * Parses a Json string into Json object.
	 * 
	 * @param jsonString The Json string
	 * @return {@link JsonObject} instance created from the Json string
	 */
	public static JsonObject parseToJsonObject (String jsonString) {

		JsonParser parser = new JsonParser ();
		return (JsonObject) parser.parse (jsonString);
	}



	/**
	 * Parses a Json string into Json array.
	 * 
	 * @param jsonString The Json string
	 * @return {@link JsonArray} instance created from the Json string
	 */

	public static JsonArray parseToJsonArray (String jsonString) {

		JsonParser parser = new JsonParser ();
		return (JsonArray) parser.parse (jsonString);
	}

	/**
	 * Get string value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @return String value of the Json key
	 */
	public static String getString (JsonObject jsonObject, String key) {

		return getString (jsonObject, key, "");
	}

	/**
	 * Get string value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @param defaultValue Default value
	 * @return String value of the Json key. Returns defaultValue if Json object
	 *         is null OR the provided Json key does not exist
	 */
	public static String getString (JsonObject jsonObject, String key, String defaultValue) {

		if (jsonObject == null)
			return defaultValue;
		if (!jsonObject.has (key))
			return defaultValue;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return defaultValue;
		}
		return StringUtility.validateEmptyString (jsonElement.getAsString (), defaultValue);
	}

	/**
	 * Get integer value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @return Integer value of the Json key
	 */
	public static int getInt (JsonObject jsonObject, String key) {

		return getInt (jsonObject, key, 0);
	}

	/**
	 * Get integer value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @param defaultValue Default value
	 * @return Integer value of the Json key. Returns defaultValue if Json
	 *         object is null OR the provided Json key does not exist
	 */
	public static int getInt (JsonObject jsonObject, String key, int defaultValue) {

		if (jsonObject == null)
			return defaultValue;
		if (!jsonObject.has (key))
			return defaultValue;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return defaultValue;
		}
		return jsonElement.getAsInt ();
	}

	public static long getLong (JsonObject jsonObject, String key, int defaultValue) {

		if (jsonObject == null)
			return defaultValue;
		if (!jsonObject.has (key))
			return defaultValue;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return defaultValue;
		}
		return jsonElement.getAsLong ();
	}

	public static long getLong (JsonObject jsonObject, String key) {

		return getLong (jsonObject, key, 0);
	}

	/**
	 * Get boolean value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @return Boolean value of the Json key. Returns defaultValue if Json
	 *         object is null OR the provided Json key does not exist
	 */
	public static boolean getBoolean (JsonObject jsonObject, String key) {

		return getBoolean (jsonObject, key, false);
	}

	/**
	 * Get boolean value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @param defaultValue Default value
	 * @return Boolean value of the Json key. Returns defaultValue if Json
	 *         object is null OR the provided Json key does not exist
	 */
	public static boolean getBoolean (JsonObject jsonObject, String key, boolean defaultValue) {

		if (jsonObject == null)
			return defaultValue;
		if (!jsonObject.has (key))
			return defaultValue;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return defaultValue;
		}
		return jsonElement.getAsBoolean ();
	}

	/**
	 * Get double value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @return Double value of the Json key. Returns defaultValue if Json object
	 *         is null OR the provided Json key does not exist
	 */
	public static double getDouble (JsonObject jsonObject, String key) {

		return getDouble (jsonObject, key, 0.0);
	}

	/**
	 * Get double value for a key on a Json object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose values needs to be returned
	 * @param defaultValue Default value
	 * @return Double value of the Json key. Returns defaultValue if Json object
	 *         is null OR the provided Json key does not exist
	 */
	public static double getDouble (JsonObject jsonObject, String key, double defaultValue) {

		if (jsonObject == null)
			return defaultValue;
		if (!jsonObject.has (key))
			return defaultValue;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return defaultValue;
		}
		return jsonElement.getAsDouble ();
	}

	/**
	 * Get nested Json Object.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose object needs to be returned
	 * @return {@link JsonObject} instance contained by the provided key.
	 *         Returns {@code null} if jsonObject is null OR the provided Json
	 *         key does not exist
	 */
	public static JsonObject getJsonObject (JsonObject jsonObject, String key) {

		JsonObject parsedObject = null;

		if (jsonObject == null)
			return null;
		if (!jsonObject.has (key))
			return null;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return null;
		}

		try {
			parsedObject = jsonElement.getAsJsonObject ();
		}
		catch (Exception e) {
			LogUtility.log (Log.ERROR, "JsonUtility", e.getMessage ());
		}

		return parsedObject;
	}

	/**
	 * Get nested Json Array.
	 * 
	 * @param jsonObject The Json object which has the provided key
	 * @param key Json key, whose object needs to be returned
	 * @return {@link JsonArray} instance contained by the provided key. Returns
	 *         {@code null} if jsonObject is null OR the provided Json key does
	 *         not exist
	 */
	public static JsonArray getJsonArray (JsonObject jsonObject, String key) {

		JsonArray parsedArray = null;

		if (jsonObject == null)
			return null;
		if (!jsonObject.has (key))
			return null;
		JsonElement jsonElement = jsonObject.get (key);
		if (jsonElement.isJsonNull ()) {
			return null;
		}

		try {
			parsedArray = jsonElement.getAsJsonArray ();
		}
		catch (Exception e) {
			LogUtility.log (Log.ERROR, "JsonUtility", e.getMessage ());
		}

		return parsedArray;
	}
}
