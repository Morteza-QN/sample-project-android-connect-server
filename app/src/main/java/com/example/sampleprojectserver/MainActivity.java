package com.example.sampleprojectserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
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
    private static final String TAG                    = "MainActivity";
    private static final int    ADD_STUDENT_REQUEST_ID = 122;
    RecyclerView   recyclerView;
    StudentAdapter adapter;

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
                startActivityForResult(new Intent(MainActivity.this, AddNewStudentFormActivity.class),
                                       ADD_STUDENT_REQUEST_ID);
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

                                adapter = new StudentAdapter(students);
                                recyclerView.setAdapter(adapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_STUDENT_REQUEST_ID && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (adapter != null) {
                    StudentObject object = data.getParcelableExtra("student");
                    adapter.addStudent(object);
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
