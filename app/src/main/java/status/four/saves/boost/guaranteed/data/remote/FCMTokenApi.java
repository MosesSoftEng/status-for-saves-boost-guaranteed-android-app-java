package status.four.saves.boost.guaranteed.data.remote;

import static status.four.saves.boost.guaranteed.shared.Config.API_URL;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.VolleyHelper;

public class FCMTokenApi {
    private static FCMTokenApi instance;
    private static VolleyHelper volleyHelper;

    public FCMTokenApi(Context context) {
        volleyHelper = VolleyHelper.getInstance(context);
    }

    public static synchronized FCMTokenApi getInstance(Context context) {
        if (instance == null) {
            instance = new FCMTokenApi(context);
        }
        return instance;
    }

    public void saveFCMToken(String loggedInUserPhoneNumber, String token, Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("user", loggedInUserPhoneNumber);
            requestBody.put("token", token);

            volleyHelper.makeRequest(
                    API_URL + "/fcm-token",
                    Request.Method.POST,
                    null,
                    requestBody,
                    response -> {
                        Logger.d(response.toString());
                        callback.onSuccess(response.toString());
                    },
                    error -> {
                        Logger.d(error.getMessage());
                        callback.onError(error);
                    });
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }

    /**
     * The callback interface for handling FCM token results.
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
