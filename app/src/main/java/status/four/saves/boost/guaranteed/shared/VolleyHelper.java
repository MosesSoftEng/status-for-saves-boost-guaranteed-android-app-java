package status.four.saves.boost.guaranteed.shared;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Helper class for making HTTP requests using Volley library.
 */
public class VolleyHelper {
    private RequestQueue requestQueue;
    private static VolleyHelper instance;

    /**
     * Private constructor to enforce singleton pattern and initialize the RequestQueue.
     *
     * @param context the application context
     */
    private VolleyHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * Returns the instance of VolleyHelper.
     *
     * @param context the application context
     * @return the instance of VolleyHelper
     */
    public static synchronized VolleyHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHelper(context);
        }
        return instance;
    }

    /**
     * Makes an HTTP request using the specified URL, method, parameters, request body, success listener, and error listener.
     *
     * @param url             the URL of the request
     * @param method          the HTTP method (e.g., Request.Method.GET, Request.Method.POST)
     * @param params          the query parameters for the request
     * @param requestBody    the request body in JSON format
     * @param successListener the listener to be called on successful response
     * @param errorListener   the listener to be called on error response
     */
    public <T> void makeRequest(String url, int method, Map<String, T> params, JSONObject requestBody, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        if (params != null && !params.isEmpty()) {
            StringBuilder queryStringBuilder = new StringBuilder();
            for (Map.Entry<String, T> entry : params.entrySet()) {
                String key = entry.getKey();
                T value = entry.getValue();

                try {
                    if (queryStringBuilder.length() > 0) {
                        queryStringBuilder.append("&");
                    }
                    queryStringBuilder.append(URLEncoder.encode(key, "UTF-8"))
                            .append("=")
                            .append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO: Handle error.
                    throw new RuntimeException(e);
                }
            }
            url += "?" + queryStringBuilder.toString();
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, requestBody, successListener, errorListener);
        requestQueue.add(request);
    }

    // TODO: Create makeRequest for specific types.
}
