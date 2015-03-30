package com.maker.contenttools.Constants;

/**
 * Created by Daniil on 13.02.2015.
 */
public class App {

    public interface Api{
        public static final String SCHEME = "http";
        public static final String AUTHORITY = "aostroumov.dev.positrace.com";
    }

    public interface Prefs{
        public static final String NAME = "transportation_prefs";
        public static final String IS_FIRST_START = "is_first_start";
        public static final String USER_IS_REGISTERED = "user_is_registered";
        public static final String PROPERTY_REG_ID = "registration_id";
        public static final String PROPERTY_APP_VERSION = "appVersion";
        public static final String USER_INFO = "user_info";
    }

    public interface Keys {
        public static final String EMAIL = "email_data";
        public static final String PASSWORD = "password_data";
    }

    public interface APIKeys {
        public static final String SENDER_ID = "613053213102";
    }
}
