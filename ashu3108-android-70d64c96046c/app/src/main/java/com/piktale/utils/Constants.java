package com.piktale.utils;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.text.SimpleDateFormat;

public class Constants {

    public interface PreferenceKeys {
        String USER_LOGGED_IN = "user_logged_in";
        String USER_DATA = "user_data";
        String PROFILE_DATA = "profile_data";
        String USER_ID = "user_id";
        String FCM_TOKEN = "fcm_token";
        String USER_TOKEN = "user_token";
        String USERNAME_DIALOG_SHOWED = "username_dialog_showed";
    }

    public interface HTTPStatusCodes {
        int OK = 200;
        int EMPTY = 204;
        int UNAUTHORIZED = 401;
        int NOT_FOUND = 404;
    }

    public interface FRAGMENT_TAG {
        String HOME = "home";
        String EXPLORE = "explore";
        String NOTIFICATIONS = "notifications";
        String PROFILE = "profile";
    }

    public static final String DOMAIN = "https://social-latform.herokuapp.com";
    public static final String PATH = "/api";

    public interface DB {
        String NAME = "striing";
        String TABLE_NOTIFICATION = "notification";
    }

    public interface DATE_FORMATS {
        String MMM_YYYY = "MMM yyyy";
        String DAY_MMM_D_YY = "EEE, MMM d, ''yy";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String D_MMMM = "d MMM";
        String YYYY_MM_DDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    }

    public static Filter[] filtersMap = new Filter[]{null, SampleFilters.getStarLitFilter(),
            SampleFilters.getBlueMessFilter(),
            SampleFilters.getAweStruckVibeFilter(),
            SampleFilters.getLimeStutterFilter(),
            SampleFilters.getNightWhisperFilter()};


}
