package org.mesibo.messenger;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONArray;

// Add helper/sample API integration here.

public class SampleAPI {
    private Context context;
    private String backendBaseUrl; // e.g. https://your-backend-url/

    public SampleAPI(Context context, String backendBaseUrl) {
        this.context = context;
        this.backendBaseUrl = backendBaseUrl;
    }

    // PUBLIC_INTERFACE
    // Add a reaction to a message
    public void addReaction(int from, int messageId, String reaction, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = backendBaseUrl + "/?api=add_reaction&from=" + from + "&message_id=" + messageId + "&reaction=" + reaction;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener);
        queue.add(stringRequest);
    }

    // PUBLIC_INTERFACE
    // Remove a reaction from a message
    public void removeReaction(int from, int messageId, String reaction, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = backendBaseUrl + "/?api=remove_reaction&from=" + from + "&message_id=" + messageId + "&reaction=" + reaction;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener);
        queue.add(stringRequest);
    }

    // PUBLIC_INTERFACE
    // Get reactions for given message(s)
    public void getReactions(Object messageIds, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        // messageIds: int or List<Integer>
        String ids = "";
        if (messageIds instanceof Integer) {
            ids = String.valueOf((Integer) messageIds);
        } else if (messageIds instanceof java.util.List) {
            java.util.List<Integer> list = (java.util.List<Integer>) messageIds;
            ids = android.text.TextUtils.join(",", list);
        }
        String url = backendBaseUrl + "/?api=get_reactions&message_id=" + ids;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        queue.add(stringRequest);
    }
}
