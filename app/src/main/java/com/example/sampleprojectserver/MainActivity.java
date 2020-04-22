package com.example.sampleprojectserver;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringRequest request =
                new StringRequest(Request.Method.GET, getString(R.string.url_server), new Response.Listener<String>() {
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
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse:\n " + error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
