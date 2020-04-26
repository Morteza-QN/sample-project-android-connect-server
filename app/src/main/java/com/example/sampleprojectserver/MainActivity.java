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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final  String         EXTRA_KEY_INTENT_ADD_STUDENT = "student";
    private static final String         TAG                          = "MainActivity";
    private static final int            ADD_STUDENT_REQUEST_ID       = 122;
    private              RecyclerView   recyclerView;
    private              StudentAdapter adapter;
    private              ApiService     apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = new ApiService();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_main_students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        View fabAddNewStudentBtn = findViewById(R.id.fab_main_addNewStudent);
        fabAddNewStudentBtn.setOnClickListener(v -> {
            Log.i(TAG, "onClick: fab add new student");
            startActivityForResult(new Intent(MainActivity.this, AddNewStudentFormActivity.class), ADD_STUDENT_REQUEST_ID);
        });

        apiService.getStudent(new ApiService.StudentListCallback() {
            @Override
            public void onSuccess(List<StudentObject> studentObjectList) {
                adapter = new StudentAdapter(studentObjectList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception error) {
                Log.i(TAG, "onError: get student");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiService.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_STUDENT_REQUEST_ID && resultCode == Activity.RESULT_OK) {
            if (data != null && adapter != null) {
                StudentObject object = data.getParcelableExtra(EXTRA_KEY_INTENT_ADD_STUDENT);
                adapter.addStudent(object);
                recyclerView.smoothScrollToPosition(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
