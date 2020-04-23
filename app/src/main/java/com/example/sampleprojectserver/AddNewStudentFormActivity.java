package com.example.sampleprojectserver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class AddNewStudentFormActivity extends AppCompatActivity {
    private static final String TAG = "AddNewStudentFormActivity";
    TextInputEditText firstNameEt, lastNameEt, courseEt, scoreEt;
    View fabSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student_form);

        Toolbar toolbar = findViewById(R.id.toolbar_addNewStudent);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);//set icon back

        firstNameEt = findViewById(R.id.et_addNewStudent_fistName);
        lastNameEt  = findViewById(R.id.et_addNewStudent_lastName);
        scoreEt     = findViewById(R.id.et_addNewStudent_score);
        courseEt    = findViewById(R.id.et_addNewStudent_course);
        fabSaveBtn  = findViewById(R.id.fab_addNewStudent_save);

        fabSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestServer();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void sendRequestServer() {
        if (inputValidation()) {
            Log.i(TAG, "sendRequestServer: validate" + inputValidation());
            JSONObject jsonObject = new JSONObject();
        }
        else { Log.i(TAG, "sendRequestServer: not validate"); }
    }

    private boolean inputValidation() {
        return editTextNotEmpty(firstNameEt) && editTextNotEmpty(lastNameEt) && editTextNotEmpty(scoreEt) &&
               editTextNotEmpty(courseEt);
    }

    private boolean editTextNotEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().length() > 0;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //set action for back button toolbar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
