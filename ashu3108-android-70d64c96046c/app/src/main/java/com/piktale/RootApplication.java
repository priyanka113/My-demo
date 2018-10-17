package com.piktale;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.piktale.listeners.ConnectivityReceiver;
import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.network.APIClient;
import com.piktale.network.APIService;
import com.piktale.persistence.PreferenceUtils;
import com.squareup.picasso.Picasso;

public class RootApplication extends Application {
    private PreferenceUtils preferenceUtils;
    private APIService apiService;
    private Profile profile;

    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.get().setLoggingEnabled(true);
        Stetho.initializeWithDefaults(this);
        preferenceUtils = new PreferenceUtils(this);
        setUser();
        setApiService();
    }

    public PreferenceUtils getPreferences() {
        if (preferenceUtils == null)
            throw new NullPointerException("Preference Utils cannot be null");
        return preferenceUtils;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public APIService getAPIService() {
        return apiService;
    }

    public Profile getProfile() {
        if (profile == null)
            profile = getPreferences().getProfile();
        return profile;
    }

    public void setUser() {
        apiService = APIClient.getAdapterApiService(preferenceUtils.getUserToken());
    }

    public void setApiService() {
        apiService = APIClient.getAdapterApiService(preferenceUtils.getUserToken());
    }



}
