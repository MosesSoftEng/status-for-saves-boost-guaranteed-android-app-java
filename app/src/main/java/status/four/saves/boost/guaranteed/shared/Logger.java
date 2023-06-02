package status.four.saves.boost.guaranteed.shared;

import static status.four.saves.boost.guaranteed.shared.Config.APP_NAME_ID;
import static status.four.saves.boost.guaranteed.shared.Config.SHOW_LOGS;

import android.util.Log;


public class Logger {
    private static final String TAG = APP_NAME_ID + ".logs";

    public static void d(Object... messages) {
        if (SHOW_LOGS) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement caller = stackTrace[4];
            String methodName = caller.getMethodName();
            String className = caller.getClassName();
            String logMessage = "[" + className + "." + methodName + "] ";

            StringBuilder finalMessage = new StringBuilder();

            for (Object message : messages) {
                finalMessage.append(" ").append(String.valueOf(message));
            }

            Log.d(TAG, logMessage + finalMessage.toString());
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
