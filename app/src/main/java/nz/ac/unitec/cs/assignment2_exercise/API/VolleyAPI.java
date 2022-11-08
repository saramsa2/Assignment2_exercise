package nz.ac.unitec.cs.assignment2_exercise.API;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyAPI {
    String url;
    String response;
    RequestQueue queue;

    public VolleyAPI(Context context, String url) {
        this.url = url;
        queue = Volley.newRequestQueue(context);
    }

    public void getAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setResponse(response);
                apiListener.readAPISucceed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiListener.readAPIFailed();
            }
        });
        queue.add(stringRequest);
    }


    private void setResponse(String response) {
        this.response = response;
    }


    public String getResponse() {
        return this.response;
    }


    public interface readAPIListener {
        void readAPISucceed(String response);
        void readAPIFailed();
    }


    readAPIListener apiListener = null;


    public void setReadAPIListener(readAPIListener apiListener) {
        this.apiListener = apiListener;
    }
}
