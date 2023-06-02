package status.four.saves.boost.guaranteed.shared;

import static status.four.saves.boost.guaranteed.shared.Config.APP_NAME_ID;
import static status.four.saves.boost.guaranteed.shared.Config.SHOW_LOGS;

import android.util.Log;


public class Logger {
    private static final String TAG = APP_NAME_ID + ".logs";

    public static void d(String... messages) {
        if (SHOW_LOGS) {
            for (String message : messages) {
                Log.d(TAG, message);
            }
        }
    }

    public static void e(String message) {
        if(SHOW_LOGS) Log.e(TAG, message);
    }

    public static void i(String message) {
        if(SHOW_LOGS) Log.i(TAG, message);
    }

    public static void w(String message) {
        if(SHOW_LOGS) Log.w(TAG, message);
    }

    public static void i(String location, String message) {
        if(SHOW_LOGS) Log.i(TAG, location + ": " +message);
    }
}
