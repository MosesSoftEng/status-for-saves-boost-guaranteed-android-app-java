package status.four.saves.boost.guaranteed.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import status.four.saves.boost.guaranteed.shared.Config;

/**
 * Helper class for interacting with SharedPreferences to save and retrieve data.
 */
public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param context The application context.
     */
    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get the instance of SharedPreferencesHelper.
     *
     * @param context The application context.
     * @return The SharedPreferencesHelper instance.
     */
    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    /**
     * Save a string value in SharedPreferences.
     *
     * @param key   The key under which the value will be saved.
     * @param value The string value to be saved.
     */
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Retrieve a string value from SharedPreferences.
     *
     * @param key          The key under which the value is saved.
     * @param defaultValue The default value to be returned if the key is not found.
     * @return The string value associated with the key, or the default value if not found.
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Remove a string value from SharedPreferences.
     *
     * @param key The key of the value to be removed.
     */
    public void removeString(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
