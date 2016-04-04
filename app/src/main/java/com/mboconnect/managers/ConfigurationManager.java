package com.mboconnect.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.Application;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.helpers.FillingHelper;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.PreferenceUtility;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by vishal.chhatwani on 10/7/2015.
 */
public class ConfigurationManager {

    private static ConfigurationManager instance = null;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {

        if (instance == null) {

            instance = new ConfigurationManager();
        }

        return instance;
    }

    public void loadConfigs() {

        String json = null;

        try {

            File file = new File(FillingHelper.getStorageDir(Application.getInstance(), "test"), "configurationfile.txt");
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");


            JsonObject jsonObject = JsonUtility.parseToJsonObject(json);

            JsonArray jsonArray = JsonUtility.getJsonArray(jsonObject, "configuration");

            int position = PreferenceUtility.getInteger(Application.getInstance(), "environmentPosition", 0);

                JsonObject jb1 = jsonArray.get(position).getAsJsonObject();

                    EnvironmentConstants.CLIENT_ID = JsonUtility.getString(jb1, KeyConstants.KEY_CLIENT_ID);
                if(position==0){
                    EnvironmentConstants.URL_WEB_PROD=JsonUtility.getString(jb1,KeyConstants.KEY_API_HOSTNAME);
                    EnvironmentConstants.SERVICE_BASE_URL = EnvironmentConstants.URL_WEB_PROD;
                }
                else if(position==1){

                    EnvironmentConstants.URL_WEB_STAGGING=JsonUtility.getString(jb1,KeyConstants.KEY_API_HOSTNAME);
                    EnvironmentConstants.SERVICE_BASE_URL = EnvironmentConstants.URL_WEB_STAGGING;
                }
                else{
                    EnvironmentConstants.URL_WEB_DEMO=JsonUtility.getString(jb1,KeyConstants.KEY_API_HOSTNAME);
                    EnvironmentConstants.SERVICE_BASE_URL = EnvironmentConstants.URL_WEB_DEMO;
                }

                    EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL=JsonUtility.getString(jb1,KeyConstants.KEY_AUTH_URL);
                    EnvironmentConstants.SERVICE_RESET_PASSWORD_URL=JsonUtility.getString(jb1,KeyConstants.KEY_FORGOT_PASSWORD);

        } catch (Exception e) {

        }
    }


    public void updateConfigsFromArtisan() {

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < 3; i++) {

            if (i == 0) {
                JsonObject innerObject = new JsonObject();
                innerObject.addProperty(KeyConstants.KEY_AUTH_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_AUTH_URL,ArtisanHooksManager.DEFAULT_PROD_AUTH_URL));
                innerObject.addProperty(KeyConstants.KEY_LOGOUT_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_LOGOUT_URL, ArtisanHooksManager.DEFAULT_PROD_LOGOUT_URL));
                innerObject.addProperty(KeyConstants.KEY_API_HOSTNAME, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_API_HOSTNAME, ArtisanHooksManager.DEFAULT_PROD_API_HOSTNAME));
                innerObject.addProperty(KeyConstants.KEY_CLIENT_ID, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_CLIENT_ID, ArtisanHooksManager.DEFAULT_PROD_CLIENT_ID));
                innerObject.addProperty(KeyConstants.KEY_FORGOT_PASSWORD, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_FORGOT_PASSWORD, ArtisanHooksManager.DEFAULT_PROD_FORGOT_PASSWORD));
                innerObject.addProperty(KeyConstants.KEY_TERMS_AND_CONDITIONS, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_TERMS_AND_CONDITIONS, ArtisanHooksManager.DEFAULT_TERMS_AND_CONDITIONS));
                innerObject.addProperty(KeyConstants.KEY_PRIVACY_POLICY, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRIVACY_POLICY, ArtisanHooksManager.DEFAULT_PRIVACY_POLICY));
                jsonArray.add(innerObject);
            } else if(i == 1) {

                JsonObject innerObject = new JsonObject();
                innerObject.addProperty(KeyConstants.KEY_AUTH_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_AUTH_URL,ArtisanHooksManager.DEFAULT_PRE_PROD_AUTH_URL));
                innerObject.addProperty(KeyConstants.KEY_LOGOUT_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_LOGOUT_URL, ArtisanHooksManager.DEFAULT_PRE_PROD_LOGOUT_URL));
                innerObject.addProperty(KeyConstants.KEY_API_HOSTNAME, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_API_HOSTNAME, ArtisanHooksManager.DEFAULT_PRE_PROD_API_HOSTNAME));
                innerObject.addProperty(KeyConstants.KEY_CLIENT_ID, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_CLIENT_ID, ArtisanHooksManager.DEFAULT_PRE_PROD_CLIENT_ID));
                innerObject.addProperty(KeyConstants.KEY_FORGOT_PASSWORD, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_FORGOT_PASSWORD, ArtisanHooksManager.DEFAULT_PRE_PROD_FORGOT_PASSWORD));
                innerObject.addProperty(KeyConstants.KEY_TERMS_AND_CONDITIONS, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_TERMS_AND_CONDITIONS, ArtisanHooksManager.DEFAULT_TERMS_AND_CONDITIONS));
                innerObject.addProperty(KeyConstants.KEY_PRIVACY_POLICY, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRIVACY_POLICY, ArtisanHooksManager.DEFAULT_PRIVACY_POLICY));

                jsonArray.add(innerObject);

            }

            else{

                JsonObject innerObject = new JsonObject();
                innerObject.addProperty(KeyConstants.KEY_AUTH_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_DEMO_AUTH_URL,ArtisanHooksManager.DEFAULT_DEMO_AUTH_URL));
                innerObject.addProperty(KeyConstants.KEY_LOGOUT_URL, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_DEMO_LOGOUT_URL, ArtisanHooksManager.DEFAULT_DEMO_LOGOUT_URL));
                innerObject.addProperty(KeyConstants.KEY_API_HOSTNAME, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_DEMO_API_HOSTNAME, ArtisanHooksManager.DEFAULT_DEMO_API_HOSTNAME));
                innerObject.addProperty(KeyConstants.KEY_CLIENT_ID, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_DEMO_CLIENT_ID, ArtisanHooksManager.DEFAULT_DEMO_CLIENT_ID));
                innerObject.addProperty(KeyConstants.KEY_FORGOT_PASSWORD, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_DEMO_FORGOT_PASSWORD, ArtisanHooksManager.DEFAULT_DEMO_FORGOT_PASSWORD));
                innerObject.addProperty(KeyConstants.KEY_TERMS_AND_CONDITIONS, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_TERMS_AND_CONDITIONS, ArtisanHooksManager.DEFAULT_TERMS_AND_CONDITIONS));
                innerObject.addProperty(KeyConstants.KEY_PRIVACY_POLICY, ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRIVACY_POLICY, ArtisanHooksManager.DEFAULT_PRIVACY_POLICY));

                jsonArray.add(innerObject);

            }

        }
        jsonObject.add("configuration", jsonArray);
        String data = jsonObject.toString();
        FillingHelper.writeToFile(data, "configurationfile.txt", Application.getInstance());

       loadConfigs();
    }
}



