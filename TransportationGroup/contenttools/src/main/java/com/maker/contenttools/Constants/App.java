package com.maker.contenttools.Constants;

/**
 * Created by Daniil on 13.02.2015.
 */
public class App {

    public interface Api{
        String SCHEME = "http";
        String AUTHORITY = "46.101.15.191:80";
    }

    public interface Prefs{
        String NAME = "transportation_prefs";
        String IS_FIRST_START = "is_first_start";
        String USER_IS_REGISTERED = "user_is_registered";
        String PROPERTY_REG_ID = "registration_id";
        String PROPERTY_APP_VERSION = "appVersion";
        String USER_INFO = "user_info";
        String IS_DEVICE_REGISTERED = "is_device_registered";
        String REG_DEVICE_DATA = "reg_device_data";
    }

    public interface Keys {
        String EMAIL = "email_data";
        String NAME = "name_data";
        String PASSWORD = "password_data";
        String PASSWORD_CONFIGURED = "password_confirmed_data";
        String ID = "id_data";
    }

    public interface APIKeys {
        String SENDER_ID = "613053213102";
    }

    public interface Buddy {
        String APP_ID = "bbbbbc.qJjbbdrxfrgB";
        String APP_KEY = "17A184F9-43F3-43CF-B1AF-43C58D38EE97";
    }
}
