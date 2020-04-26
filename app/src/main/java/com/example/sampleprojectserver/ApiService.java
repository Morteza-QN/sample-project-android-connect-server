package com.example.sampleprojectserver;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApiService {
    private static final String       TAG                  = "ApiService";
    private static final String       EXTRA_KEY_FIRST_NAME = "first_name";
    private static final String       EXTRA_KEY_LAST_NAME  = "last_name";
    private static final String       EXTRA_KEY_COURSE     = "course";
    private static final String       EXTRA_KEY_SCORE      = "score";
    private static final String       EXTRA_KEY_ID         = "id";
    private static final String       URL_SERVER_API       = "http://expertdevelopers.ir/api/v1/experts/student";
    private static       RequestQueue requestQueue;
    private              String       requestTag;

    public ApiService(Context context, String requestTag) {
        this.requestTag = requestTag;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public void saveStudent(String firstName, String lastName, String course, int score, final SaveStudentCallback callBack) {
        JSONObject jsonObjectBody = new JSONObject();
        try {
            Log.i(TAG, "saveStudent: set body request");
            jsonObjectBody.put(EXTRA_KEY_FIRST_NAME, firstName);
            jsonObjectBody.put(EXTRA_KEY_LAST_NAME, lastName);
            jsonObjectBody.put(EXTRA_KEY_COURSE, course);
            jsonObjectBody.put(EXTRA_KEY_SCORE, score);
        }
        catch (JSONException e) { e.printStackTrace(); }
      /*  JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, URL_SERVER_API, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: \n" + response);
                        StudentObject Student = gson.fromJson(response.toString(), StudentObject.class);
                        callBack.onSuccess(Student);
                        //callBack.onSuccess(gson.fromJson(response.toString(),StudentObject.class));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse:\n " + error.toString());
                        callBack.onError(error);
                    }
                });*/
        GsonRequest<StudentObject> request =
                new GsonRequest<>(Request.Method.POST, StudentObject.class, URL_SERVER_API, jsonObjectBody,
                        new Response.Listener<StudentObject>() {
                            @Override
                            public void onResponse(StudentObject response) {
                                Log.i(TAG, "onResponse: post studentObj");
                                callBack.onSuccess(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: post studentObj");
                        callBack.onError(error);
                    }
                });
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public void getStudent(final StudentListCallback listCallback) {
    /*    StringRequest request = new StringRequest(Request.Method.GET, URL_SERVER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse:\n " + response.toString());

                List<StudentObject> students = gson.fromJson(response, new TypeToken<List<StudentObject>>() {}.getType());
                listCallback.onSuccess(students);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse:\n " + error.toString());
                listCallback.onError(error);
            }
        });*/
        GsonRequest<List<StudentObject>> request =
                new GsonRequest<>(Request.Method.GET, new TypeToken<List<StudentObject>>() {}.getType(), URL_SERVER_API,
                        new Response.Listener<List<StudentObject>>() {
                            @Override
                            public void onResponse(List<StudentObject> response) {
                                //type of response is TypeToken
                                Log.i(TAG, "onResponse: get gsonRequest");
                                listCallback.onSuccess(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: get gsonRequest");
                        listCallback.onError(error);
                    }
                });
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public void cancel() { requestQueue.cancelAll(requestTag); }


    public interface SaveStudentCallback {
        void onSuccess(StudentObject student);

        void onError(VolleyError error);
    }

    public interface StudentListCallback {
        void onSuccess(List<StudentObject> studentObjectList);

        void onError(VolleyError error);
    }
}
