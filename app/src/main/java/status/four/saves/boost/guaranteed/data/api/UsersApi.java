package status.four.saves.boost.guaranteed.data.api;

import static status.four.saves.boost.guaranteed.shared.Config.API_URL;
import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.VolleyHelper;


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

    public static void getUsers(int lastIndex, int limit, Callback callback) {
        JSONObject requestBody = new JSONObject();
        Map<String, Integer> params = new HashMap<>();
        params.put("lastIndex", lastIndex);
        params.put("limit", limit);

        // TODO: Create UserAPI class
        volleyHelper.makeRequest(
            API_URL + "/users",
            Request.Method.GET,
                params,
            requestBody,
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
