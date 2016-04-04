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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tenpearls.android.R;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

/**
 * <p>
 * 
 * Provides a scalable and easy to manage mechanism to manage your application
 * wide configuration values or constants. Comes in really handy when the
 * application needs to run in different environments. For example:<br />
 * </br />
 * 
 * Base API URL for application running in Staging mode will different as
 * compared to Production. Configuration Manager provides the flexibility of
 * defining multiple configuration environments (if required) with the option of
 * specifying a default environment.
 * 
 * </p>
 * 
 * @author 10Pearls
 * 
 */
public class ConfigurationManager {

	private static ConfigurationManager              sInstance;
	private String                                   mCurrentConfiguration;
	private HashMap<String, HashMap<String, String>> mConfigurationsMap = new HashMap<String, HashMap<String, String>> ();
	private HashMap<String, String>                  mGlobalSettingsMap = new HashMap<String, String> ();
	private Context                                  mContext;

	/**
	 * Single, global point of access to get instance of the Configuration
	 * manager.
	 * 
	 * @return Singleton instance
	 */
	public static ConfigurationManager getInstance () {

		if (sInstance == null) {
			sInstance = new ConfigurationManager ();
		}

		return sInstance;
	}

	/**
	 * Call this method after setting context ({@code setContext}) on the
	 * Configuration manager instance. It will read the
	 * 'application_configurations.json' file in assets directory.
	 */
	public void initialize () {

		readAndSetupApplicationConfigurations ();
	}

	/**
	 * <p>
	 * Gets value for the provided key from 'application_configurations.json' as
	 * per current configuration environment. This method also throws
	 * {@link IllegalStateException}s
	 * 
	 * @param key The configuration key.
	 * @return Value of the key {@link IllegalStateException} for following
	 *         scenarios:
	 *         <ul>
	 *         <li>File 'application_configurations.json' was not properly
	 *         parsed or setup in memory.</li> <li>Current configuration not
	 *         provided</li> <li>A null or empty key was provided</li> <li>
	 *         Provided key does not exist for current configuration.</li>
	 *         </ul>
	 *         </p>
	 */
	public String getValueForKey (String key) {

		String valueForConfigurationKey = null;

		if (mConfigurationsMap.isEmpty () || mGlobalSettingsMap.isEmpty ()) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Did you call initialize?");
		}
		else if (StringUtility.isEmptyOrNull (mCurrentConfiguration)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Current configuration is not set");
		}
		else if (StringUtility.isEmptyOrNull (key)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Cannot find value of empty key");
		}
		else if (!mConfigurationsMap.containsKey (mCurrentConfiguration)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Configuration not found for " + mCurrentConfiguration);
		}
		else if (mConfigurationsMap.get (mCurrentConfiguration).containsKey (key)) {
			valueForConfigurationKey = mConfigurationsMap.get (mCurrentConfiguration).get (key);
		}
		else if (mGlobalSettingsMap.containsKey (key)) {
			valueForConfigurationKey = mGlobalSettingsMap.get (key);
		}
		else {
			throw new IllegalStateException (this.getClass ().getName () + " >> Configuration not found for key " + key);
		}

		return valueForConfigurationKey;
	}

	/**
	 * Read and setup application configurations.
	 */
	private void readAndSetupApplicationConfigurations () {

		String applicationFeaturesJson = null;

		try {
			applicationFeaturesJson = getConfigurationsJson ();
		}
		catch (Exception ex) {
			UIUtility.showAlert ("Error", ex.getMessage (), "OK", mContext);
			return;
		}

		if (!StringUtility.isEmptyOrNull (applicationFeaturesJson)) {
			JsonParser jsonParser = new JsonParser ();
			JsonObject jsonContainerObject = (JsonObject) jsonParser.parse (applicationFeaturesJson);

			for (Map.Entry<String, JsonElement> entry : jsonContainerObject.entrySet ()) {

				String type = entry.getKey ().toLowerCase (Locale.getDefault ());

				if (type.equals ("globals")) {
					setupGlobalSettingsMap (entry.getValue ().getAsJsonObject ());
					continue;
				}
				else if (type.equals ("current_configuration")) {
					mCurrentConfiguration = entry.getValue ().getAsString ();
					continue;
				}

				if (type.equals ("configurations")) {

					JsonObject configurationsJsonObject = entry.getValue ().getAsJsonObject ();

					for (Map.Entry<String, JsonElement> configurationItem : configurationsJsonObject.entrySet ()) {
						String configurationName = configurationItem.getKey ();
						JsonObject configurationJsonObject = (JsonObject) configurationItem.getValue ();
						HashMap<String, String> configurations = new HashMap<String, String> ();

						for (Map.Entry<String, JsonElement> configurationParameter : configurationJsonObject.entrySet ()) {
							String configurationKey = configurationParameter.getKey ();
							String configurationValue = configurationParameter.getValue ().getAsString ();
							configurations.put (configurationKey, configurationValue);
						}

						mConfigurationsMap.put (configurationName, configurations);
					}
				}
			}
		}
	}

	private void setupGlobalSettingsMap (JsonObject jsonObject) {

		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet ()) {

			mGlobalSettingsMap.put (entry.getKey (), entry.getValue ().getAsString ());
		}
	}

	/**
	 * Gets the configurations json.
	 * 
	 * @return the configurations json
	 */
	private String getConfigurationsJson () throws Exception {

		if (mContext == null) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Context not set.");
		}

		InputStream is = mContext.getResources ().openRawResource (R.raw.application_configurations);
		Writer writer = new StringWriter ();
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader (new InputStreamReader (is, "UTF-8"));
			int n;
			while ((n = reader.read (buffer)) != -1) {
				writer.write (buffer, 0, n);
			}
		}
		finally {
			is.close ();
		}

		return writer.toString ();
	}

	/**
	 * Setter for context. You must set the context before calling
	 * 
	 * @param context A valid context {@code initialize} on this object.
	 */
	public void setContext (Context context) {

		mContext = context;
	}
}
