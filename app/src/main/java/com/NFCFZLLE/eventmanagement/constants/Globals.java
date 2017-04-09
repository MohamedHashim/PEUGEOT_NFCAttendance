package com.NFCFZLLE.eventmanagement.constants;


public class Globals {

    public static final String KEY_CACHED_CONTACTS = "cache_contacts";
    public static boolean IS_DEBUG = true;

    public static class PreferenceKeys {
        public static final String USER_OBJ = "USER_OBJ";

    }

    public static class WebServices {

        //live url
        public static String ENDPOINT_BASE_URL = "http://ae.nfcvalet.com/mobile/";

        public static final String SERVICE_TYPE_REST = "REST";

    }

    public static class WebMethods {

        public static final String  LOGIN_USER="login";
        public static final String  LOGIN_NFC_USER="loginnfc";

        public static final String ATTENDEE_CHECK_IN = "checkIn";
        public static final String REGISTER_ATTENDEE = "createAttendee";
        public static final String ATTENDEE_CHECK_OUT = "checkOut";
        public static final String APP_DATA = "appdata";
        public static final String UPDATE_LOCATION = "locationUpdate";
        public static final String FORGOT_PASSWORD = "forgotPassword";



    }

}
