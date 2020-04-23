package com.example.sampleprojectserver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_main_students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        View fabAddNewStudentBtn = findViewById(R.id.fab_main_addNewStudent);
        fabAddNewStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNewStudentFormActivity.class));
            }
        });

        getRequestServer();
    }

    private void getRequestServer() {
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
                                recyclerView.setAdapter(new StudentAdapter(students));
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
