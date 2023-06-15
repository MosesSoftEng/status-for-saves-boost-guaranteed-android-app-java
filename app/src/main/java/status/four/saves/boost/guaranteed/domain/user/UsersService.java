package status.four.saves.boost.guaranteed.domain.user;

import static status.four.saves.boost.guaranteed.shared.Config.API_URL;
import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import status.four.saves.boost.guaranteed.data.local.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.shared.VolleyHelper;

public class UsersService {
    private static UsersService instance;
    private static SharedPreferencesHelper sharedPreferencesHelper;
    private static VolleyHelper volleyHelper;

    /**
     * Constructs a new instance of UsersService.
     *
     * @param context The application context.
     */
    public UsersService(Context context) {
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        volleyHelper = VolleyHelper.getInstance(context);
    }

    /**
     * Gets the singleton instance of UsersService.
     *
     * @param context The application context.
     * @return The instance of UsersService.
     */
    public static synchronized UsersService getInstance(Context context) {
        if (instance == null) {
            instance = new UsersService(context);
        }
        return instance;
    }

    /**
     * Performs user login with the provided WhatsApp phone number.
     *
     * @param whatsAppPhoneNumber The WhatsApp phone number to log in with.
     * @param callback            The callback to handle the login result.
     */
    public static void loginUser(String whatsAppPhoneNumber, Callback callback) {
        callback.onSuccess("Number saved");

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("whatsAppPhoneNumber", whatsAppPhoneNumber);

            // TODO: Create AuthApi class
            volleyHelper.makeRequest(
                    API_URL + "/login",
                    Request.Method.POST,
                    null,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            sharedPreferencesHelper.saveString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, whatsAppPhoneNumber);

                            callback.onSuccess(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isUserLoggedIn() {
        return sharedPreferencesHelper.hasString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER);
    }

    /**
     * The callback interface for handling login results.
     */
    public interface Callback {
        /**
         * Called when the login operation is successful.
         *
         * @param message The success message.
         */
        void onSuccess(String message);

        /**
         * Called when an error occurs during the login operation.
         *
         * @param throwable The error that occurred.
         */
        void onError(Throwable throwable);
    }
}
