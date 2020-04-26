package com.example.sampleprojectserver;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonRequest<T> extends Request<T> {
    /*T is List students or one student*/
    private Gson                 gson = new Gson();
    private Type                 type;
    /*for detect type of result need instance variable Type*/
    private Response.Listener<T> listener;

    public GsonRequest(int method, Type type, String url, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        /*deleted 2 section of json object request (body,listener for get result)*/
        super(method, url, errorListener);
        this.type     = type;
        this.listener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        /*convert networkResponse to data class (studentObject)* when get result of request call this method * type of networkResponse is array byte*/
        String responseInString = new String(networkResponse.data);              //first step convert byte to string
        T      responseResult   = gson.fromJson(responseInString, type);        //convert response string to java class type
        return Response.success(responseResult, HttpHeaderParser.parseCacheHeaders(networkResponse));
        //HttpHeaderParser.parseCacheHeaders auto parse header networkResponse
    }

    @Override
    protected void deliverResponse(T response) {
        /*callback response created to client*/
        listener.onResponse(response);
    }
}
