package com.example.sampleprojectserver;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        Log.i(TAG, "sendRequestServer: validate");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(EXTRA_KEY_FIRST_NAME, firstName);
            jsonObject.put(EXTRA_KEY_LAST_NAME, lastName);
            jsonObject.put(EXTRA_KEY_COURSE, course);
            jsonObject.put(EXTRA_KEY_SCORE, score);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, URL_SERVER_API, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: \n" + response);

                        StudentObject Student = new StudentObject();
                        try {
                            Student.setId(response.getInt(EXTRA_KEY_ID));
                            Student.setFirstName(response.getString(EXTRA_KEY_FIRST_NAME));
                            Student.setLastName(response.getString(EXTRA_KEY_LAST_NAME));
                            Student.setCourse(response.getString(EXTRA_KEY_COURSE));
                            Student.setScore(response.getInt(EXTRA_KEY_SCORE));
                            callBack.onSuccess(Student);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse:\n " + error.toString());
                        callBack.onError(error);
                    }
                });
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public void getStudent(final StudentListCallback listCallback) {
        StringRequest request = new StringRequest(Request.Method.GET, URL_SERVER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse:\n " + response.toString());

                List<StudentObject> students = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        StudentObject Student           = new StudentObject();
                        JSONObject    studentJsonObject = jsonArray.getJSONObject(i);
                        Student.setId(studentJsonObject.getInt("id"));
                        Student.setFirstName(studentJsonObject.getString("first_name"));
                        Student.setLastName(studentJsonObject.getString("last_name"));
                        Student.setCourse(studentJsonObject.getString("course"));
                        Student.setScore(studentJsonObject.getInt("score"));
                        students.add(Student);

                    }
                    Log.i(TAG, "onResponse: students size = " + students.size());
                    listCallback.onSuccess(students);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse:\n " + error.toString());
                listCallback.onError(error);
            }
        });
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public void cancel() {
        requestQueue.cancelAll(requestTag);
    }


    public interface SaveStudentCallback {
        void onSuccess(StudentObject student);

        void onError(VolleyError error);
    }

    public interface StudentListCallback {
        void onSuccess(List<StudentObject> studentObjectList);

        void onError(VolleyError error);
    }
}
