package com.example.sampleprojectserver;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    /*T is List students or one student*/
    private Response.Listener<T> listener;
    private Gson                 gson = new Gson();
    private Type                 type;    /*for detect type of result need instance variable Type*/
    private JSONObject           jsonObjectBody;

    public GsonRequest(int method, Type type, String url, @Nullable JSONObject jsonObjectBody, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        /*deleted 2 section of json object request (body,listener for get result)*/
        super(method, url, errorListener);
        this.type           = type;
        this.listener       = listener;
        this.jsonObjectBody = jsonObjectBody;
    }

    public GsonRequest(int method, Type type, String url, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        this(method, type, url, null, listener, errorListener);//get
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        /*sent headers this project optional*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/json"; //set content type post request is json
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return jsonObjectBody == null ? super.getBody() : jsonObjectBody.toString().getBytes();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        /*convert networkResponse to data class (studentObject)* when get result of request call this method * type of networkResponse is array byte*/
        try {
            String responseInString = new String(networkResponse.data);//first step convert byte to string
            T      responseResult   = gson.fromJson(responseInString, type);//convert response string to java class type
            return Response.success(responseResult, HttpHeaderParser.parseCacheHeaders(networkResponse));
            //HttpHeaderParser.parseCacheHeaders auto parse header networkResponse
        }
        catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        /*callback response created to client*/
        listener.onResponse(response);
    }
}
