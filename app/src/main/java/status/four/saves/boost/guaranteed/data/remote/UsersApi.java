package status.four.saves.boost.guaranteed.data.remote;

import static status.four.saves.boost.guaranteed.shared.Config.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.VolleyHelper;

// TODO: Remove auth endpoints to AuthAPI
public class UsersApi {
    private static UsersApi instance;
    private static VolleyHelper volleyHelper;

    public UsersApi(Context context) {
        volleyHelper = VolleyHelper.getInstance(context);
    }

    public static synchronized UsersApi getInstance(Context context) {
        if (instance == null) {
            instance = new UsersApi(context);
        }
        return instance;
    }

    public static void getUsers(String loggedInUserPhone, long lastIndex, int limit, Callback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("lastIndex", lastIndex);
        params.put("limit", limit);

        // TODO: Create GET method.
        volleyHelper.makeRequest(
            API_URL + "/users/not-contact/" + loggedInUserPhone,
            Request.Method.GET,
                params,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Logger.d(response.toString());

                    callback.onSuccess(response.optString("data"));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.d(error.getMessage());

                    callback.onError(error);
                }
        });
    }

    /**
     * Fetches the list of users saved by the logged-in user.
     *
     * @param loggedInUserPhone The phone number of the logged-in user.
     * @param lastIndex         The index of the last item received in the previous request.
     * @param limit             The maximum number of items to retrieve.
     * @param callback          The callback to handle the API response.
     */
    public void getUsersSavedMe(String loggedInUserPhone, long lastIndex, int limit, Callback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("lastIndex", lastIndex);
        params.put("limit", limit);

        volleyHelper.makeRequest(
                API_URL + "/users/saved/" + loggedInUserPhone,
                Request.Method.GET,
                params,
                null,
                response -> {
                    Logger.d(response.toString());
                    callback.onSuccess(response.optString("data"));
                },
                error -> {
                    Logger.d(error.getMessage());
                    callback.onError(error);
                });
    }

    /**
     * Retrieves the users who are contacts of the logged-in user from the API.
     *
     * @param loggedInUserPhone The phone number of the logged-in user.
     * @param lastIndex The last index of the fetched data.
     * @param limit The maximum number of results to fetch.
     * @param callback The callback to handle the API response.
     */
    public void getUsersAreContacts(String loggedInUserPhone, long lastIndex, int limit, Callback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("lastIndex", lastIndex);
        params.put("limit", limit);

        volleyHelper.makeRequest(
                API_URL + "/users/saved/" + loggedInUserPhone,
                Request.Method.GET,
                params,
                null,
                response -> {
                    Logger.d(response.toString());
                    callback.onSuccess(response.optString("data"));
                },
                error -> {
                    Logger.d(error.getMessage());
                    callback.onError(error);
                });
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
