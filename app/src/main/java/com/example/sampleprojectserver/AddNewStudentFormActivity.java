package com.example.sampleprojectserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

public class AddNewStudentFormActivity extends AppCompatActivity {
    private static final String            TAG = "AddStudentFormActivity";
    private              TextInputEditText firstNameEt, lastNameEt, courseEt, scoreEt;
    private View       fabSaveBtn;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student_form);

        apiService = new ApiService(this, TAG);

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
                Log.i(TAG, "onClick: fab save");
                sendRequestServer();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiService.cancel();
    }

    private void sendRequestServer() {
        if (inputValidation()) {
            Log.i(TAG, "\nsendRequestServer: validate");
            apiService.saveStudent(getTextInput(firstNameEt), getTextInput(lastNameEt), getTextInput(courseEt),
                    Integer.parseInt(getTextInput(scoreEt)), new ApiService.SaveStudentCallback() {
                        @Override
                        public void onSuccess(StudentObject student) {
                            Intent intent = new Intent();
                            intent.putExtra(MainActivity.EXTRA_KEY_INTENT_ADD_STUDENT, student);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.i(TAG, "onError: add student" + error);
                        }
                    });
        }
        else { Log.i(TAG, "sendRequestServer: not validate"); }
    }

    @NotNull
    private String getTextInput(@NotNull TextInputEditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean inputValidation() {
        return editTextNotEmpty(firstNameEt) && editTextNotEmpty(lastNameEt) && editTextNotEmpty(scoreEt) &&
               editTextNotEmpty(courseEt);
    }

    private boolean editTextNotEmpty(@NotNull TextInputEditText editText) {
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
