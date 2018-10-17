package com.piktale.persistence;

import android.content.Context;
import android.util.Log;

import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.network.APIClient;
import com.piktale.network.APIService;
import com.piktale.utils.Constants;
import com.google.gson.Gson;

/**
 * Created by sayagodshala on 15/05/17.
 */

public class PreferenceUtils extends PreferenceHelper {

    public PreferenceUtils(Context context) {
        super(context);
    }

    public Profile getProfile() {
        Profile profile = null;
        String raw = getString(Constants.PreferenceKeys.PROFILE_DATA, "");
        if (!raw.equalsIgnoreCase(""))
            profile = new Gson().fromJson(raw, Profile.class);
        return profile;
    }

    public boolean isUserLoggedIn() {
        return getBoolean(Constants.PreferenceKeys.USER_LOGGED_IN, false);
    }

    public void removeUserSession() {
//        String raw = getGCMToken();
        super.clearSession();
//        setGcmToken(raw);
    }

    public String getUserToken() {
        String raw = getString(Constants.PreferenceKeys.USER_TOKEN, "");
        return raw;
    }

    public boolean isUserNameDialogShowed() {
        return getBoolean(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED, false);
    }

    public void setPreference(String... keys) {
        if (keys.length > 0) {
            String key = keys[0];
            if (key.equalsIgnoreCase(Constants.PreferenceKeys.USER_LOGGED_IN)) {
                addPreference(key, true);
            } else if (key.equalsIgnoreCase(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED)) {
                addPreference(key, true);
            } else if (key.equalsIgnoreCase(Constants.PreferenceKeys.USER_TOKEN)) {
                if (keys.length == 2)
                    addPreference(key, keys[1]);
                else
                    throw new IllegalArgumentException("User token required");
            } else if (key.equalsIgnoreCase(Constants.PreferenceKeys.USER_DATA)) {
                if (keys.length == 2) {
                    setPreference(Constants.PreferenceKeys.USER_LOGGED_IN);
                    addPreference(key, keys[1]);
                } else
                    throw new IllegalArgumentException("User data required");
            } else if (key.equalsIgnoreCase(Constants.PreferenceKeys.PROFILE_DATA)) {
                if (keys.length == 2) {
                    setPreference(Constants.PreferenceKeys.USER_LOGGED_IN);
                    addPreference(key, keys[1]);
                } else
                    throw new IllegalArgumentException("User data required");
            }
        }
    }

}
