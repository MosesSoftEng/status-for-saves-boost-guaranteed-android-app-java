package status.four.saves.boost.guaranteed.shared;

public class Config {
    public static final String APP_NAME_ID = "status.four.saves.boost.guaranteed";
    public static boolean SHOW_LOGS = true;

    // Network
    public static final String API_URL = "https://vo40if7yh5.execute-api.us-east-1.amazonaws.com/dev/v1";

    // Storage
    public static final String SHARED_PREFS_NAME = APP_NAME_ID;
    public static final String SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER = "SHARED_PREFS_kEY_USER_WHATSAPP_MOBILE_NUMBER";

    // Pagination
    public static int paginationCount = 20;
}
