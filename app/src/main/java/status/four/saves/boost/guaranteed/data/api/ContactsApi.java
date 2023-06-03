package status.four.saves.boost.guaranteed.data.api;

import static status.four.saves.boost.guaranteed.shared.Config.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.VolleyHelper;

public class ContactsApi {
    private static ContactsApi instance;
    private VolleyHelper volleyHelper;

    public ContactsApi(Context context) {
        volleyHelper = VolleyHelper.getInstance(context);
    }

    public static synchronized ContactsApi getInstance(Context context) {
        if (instance == null) {
            instance = new ContactsApi(context);
        }
        return instance;
    }

    /**
     * Saves a contact by making a POST request to the API.
     *
     * @param loggedInUserPhoneNumber The phone number of the logged-in user.
     * @param userPhoneNumber         The phone number of the contact to save.
     * @param callback                The callback to handle the saveContact operation result.
     */
    public void saveContact(String loggedInUserPhoneNumber, long userPhoneNumber, ContactsApi.Callback callback) {
        try {
        JSONObject requestBody = new JSONObject();
        requestBody.put("user", loggedInUserPhoneNumber);
        requestBody.put("phone", userPhoneNumber);

        volleyHelper.makeRequest(
                API_URL + "/contacts",
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
            callback.onError(error);
        }
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
