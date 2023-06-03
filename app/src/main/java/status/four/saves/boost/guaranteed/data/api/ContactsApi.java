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
    private static VolleyHelper volleyHelper;

    public ContactsApi(Context context) {
        volleyHelper = VolleyHelper.getInstance(context);
    }

    public static synchronized ContactsApi getInstance(Context context) {
        if (instance == null) {
            instance = new ContactsApi(context);
        }
        return instance;
    }

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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(response.toString());

                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(error.getMessage());

                        callback.onError(error);
                    }
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
