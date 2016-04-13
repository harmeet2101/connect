package com.mboconnect.managers;

import com.artisan.powerhooks.PowerHookManager;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by vishal.chhatwani on 10/8/2015.
 */
public class ArtisanHooksManager {

    public static final String HOOK_PRE_PROD_API_HOSTNAME = "HOOK_PRE_PROD_API_HOSTNAME";
    public static final String HOOK_PROD_API_HOSTNAME = "HOOK_PROD_API_HOSTNAME";
    public static final String HOOK_PROD_AUTH_URL = "HOOK_PROD_AUTH_URL";
    public static final String HOOK_PRE_PROD_LOGOUT_URL = "HOOK_PRE_PROD_LOGOUT_URL";
    public static final String HOOK_PRE_PROD_CLIENT_ID = "HOOK_PRE_PROD_CLIENT_ID";
    public static final String HOOK_PRE_PROD_AUTH_URL = "HOOK_PRE_PROD_AUTH_URL";
    public static final String HOOK_PROD_CLIENT_ID = "HOOK_PROD_CLIENT_ID";
    public static final String HOOK_PROD_LOGOUT_URL = "HOOK_PROD_LOGOUT_URL";
    public static final String HOOK_PROD_FORGOT_PASSWORD = "HOOK_PROD_FORGET_PASSWORD";
    public static final String HOOK_PRE_PROD_FORGOT_PASSWORD = "HOOK_PRE_PROD_FORGET_PASSWORD";
    public static final String HOOK_TERMS_AND_CONDITIONS = "HOOK_TERMS_AND_CONDITIONS";
    public static final String HOOK_PRIVACY_POLICY = "HOOK_PRIVACY_POLICY";
    public static final String HOOK_DEMO_API_HOSTNAME= "HOOK_DEMO_API_HOSTNAME";
    public static final String HOOK_DEMO_AUTH_URL = "HOOK_DEMO_AUTH_URL";
    public static final String HOOK_DEMO_LOGOUT_URL = "HOOK_DEMO_LOGOUT_URL";
    public static final String HOOK_DEMO_CLIENT_ID = "HOOK_DEMO_CLIENT_ID";
    public static final String HOOK_DEMO_FORGOT_PASSWORD = "HOOK_DEMO_FORGOT_PASSWORD";

    //public static final String DEFAULT_PRE_PROD_API_HOSTNAME = "http://ec2-54-224-3-44.compute-1.amazonaws.com/api/v1";
    public static final String DEFAULT_PRE_PROD_API_HOSTNAME = "http://ec2-54-167-148-78.compute-1.amazonaws.com/api/v1";
    public static final String DEFAULT_PROD_API_HOSTNAME = "https://api.connect.mbopartners.com/v1";
    public static final String DEFAULT_PROD_AUTH_URL = "https://login.mbopartners.com/auth/realms/mobile/protocol/openid-connect/token";
    public static final String DEFAULT_PRE_PROD_LOGOUT_URL = "http://sso2-dev.mbopartners.com/auth/realms/dev/tokens/logout";
    public static final String DEFAULT_PRE_PROD_CLIENT_ID = "Y29ubmVjdC13ZWI=";
    public static final String DEFAULT_PRE_PROD_AUTH_URL = "http://sso2-dev.mbopartners.com/auth/realms/dev/protocol/openid-connect/token";
    public static final String DEFAULT_PROD_CLIENT_ID = "Y29ubmVjdC1tb2JpbGU=";
    public static final String DEFAULT_PROD_LOGOUT_URL = "https://login.mbopartners.com/auth/realms/mobile/tokens/logout";
    public static final String DEFAULT_PROD_FORGOT_PASSWORD = "https://login.mbopartners.com/auth/realms/mobile/login-actions/password-reset";
    public static final String DEFAULT_PRE_PROD_FORGOT_PASSWORD = "https://sso2-dev.mbopartners.com/auth/realms/dev/login-actions/password-reset";
    public static final String DEFAULT_TERMS_AND_CONDITIONS = "https://www.mbopartners.com/terms-and-conditions";
    public static final String DEFAULT_PRIVACY_POLICY = "https://www.mbopartners.com/privacy-policy";
    public static final String DEFAULT_DEMO_API_HOSTNAME = "http://connect.demo.mbopartners.com/api/v1";
    public static final String DEFAULT_DEMO_AUTH_URL = "http://sso2-dev.mbopartners.com/auth/realms/dev/protocol/openid-connect/token";
    public static final String DEFAULT_DEMO_LOGOUT_URL = "http://sso2-dev.mbopartners.com/auth/realms/dev/tokens/logout";
    public static final String DEFAULT_DEMO_CLIENT_ID = "Y29ubmVjdC13ZWI=";
    public static final String DEFAULT_DEMO_FORGOT_PASSWORD = "https://sso2-dev.mbopartners.com/auth/realms/dev/login-actions/password-reset";


    public static void registerPowerHooks() {

        PowerHookManager.registerVariable(HOOK_PRE_PROD_API_HOSTNAME, "Pre-Prod API hostname", DEFAULT_PRE_PROD_API_HOSTNAME);
        PowerHookManager.registerVariable(HOOK_PROD_API_HOSTNAME, "Prod API hostname", DEFAULT_PROD_API_HOSTNAME);
        PowerHookManager.registerVariable(HOOK_PROD_AUTH_URL, "Prod authorization url", DEFAULT_PROD_AUTH_URL);
        PowerHookManager.registerVariable(HOOK_PRE_PROD_LOGOUT_URL, "Pre-Prod logout url", DEFAULT_PRE_PROD_LOGOUT_URL);
        PowerHookManager.registerVariable(HOOK_PRE_PROD_CLIENT_ID, "Pre-Prod client id", DEFAULT_PRE_PROD_CLIENT_ID);
        PowerHookManager.registerVariable(HOOK_PRE_PROD_AUTH_URL, "Pre-Prod authorization url", DEFAULT_PRE_PROD_AUTH_URL);
        PowerHookManager.registerVariable(HOOK_PROD_CLIENT_ID, "Prod client id", DEFAULT_PROD_CLIENT_ID);
        PowerHookManager.registerVariable(HOOK_PROD_LOGOUT_URL, "Prod logout url", DEFAULT_PROD_LOGOUT_URL);
        PowerHookManager.registerVariable(HOOK_PROD_FORGOT_PASSWORD, "Prod Forgot Password url", DEFAULT_PROD_FORGOT_PASSWORD);
        PowerHookManager.registerVariable(HOOK_PRE_PROD_FORGOT_PASSWORD, "Pre-Prod Forgot Password url", DEFAULT_PRE_PROD_FORGOT_PASSWORD);
        PowerHookManager.registerVariable(HOOK_TERMS_AND_CONDITIONS, "Terms and Conditions url", DEFAULT_TERMS_AND_CONDITIONS);
        PowerHookManager.registerVariable(HOOK_PRIVACY_POLICY, "Privacy Policy url", DEFAULT_PRIVACY_POLICY);
        PowerHookManager.registerVariable(HOOK_DEMO_API_HOSTNAME,"Demo API Hostname",DEFAULT_DEMO_API_HOSTNAME);
        PowerHookManager.registerVariable(HOOK_DEMO_AUTH_URL,"Demo authorization url",DEFAULT_DEMO_AUTH_URL);
        PowerHookManager.registerVariable(HOOK_DEMO_LOGOUT_URL,"Demo logout url",DEFAULT_DEMO_LOGOUT_URL);
        PowerHookManager.registerVariable(HOOK_DEMO_CLIENT_ID,"Demo client id",DEFAULT_DEMO_CLIENT_ID);
        PowerHookManager.registerVariable(HOOK_DEMO_FORGOT_PASSWORD,"Demo forgot password",DEFAULT_DEMO_FORGOT_PASSWORD);

    }

    public static String getHookForKeyWithDefaultValue(String key, String defaultValue) {

        String hookValue = PowerHookManager.getVariableValue(key);
        return StringUtility.isEmptyOrNull(hookValue) ? defaultValue : hookValue;
    }
}
