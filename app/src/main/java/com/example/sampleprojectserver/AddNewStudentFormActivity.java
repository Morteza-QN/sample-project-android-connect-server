package com.example.sampleprojectserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNewStudentFormActivity extends AppCompatActivity {
    private static final String TAG                  = "AddNewStudentFormActivity";
    private static final String EXTRA_KEY_FIRST_NAME = "first_name";
    private static final String EXTRA_KEY_LAST_NAME  = "last_name";
    private static final String EXTRA_KEY_COURSE     = "course";
    private static final String EXTRA_KEY_SCORE      = "score";
    private static final String EXTRA_KEY_ID         = "id";
    TextInputEditText firstNameEt, lastNameEt, courseEt, scoreEt;
    View fabSaveBtn;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student_form);

        requestQueue = Volley.newRequestQueue(this);

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
                try {
                    sendRequestServer();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void sendRequestServer() throws JSONException {
        if (inputValidation()) {
            Log.i(TAG, "sendRequestServer: validate");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(EXTRA_KEY_FIRST_NAME, getTextInput(firstNameEt));
            jsonObject.put(EXTRA_KEY_LAST_NAME, getTextInput(lastNameEt));
            jsonObject.put(EXTRA_KEY_COURSE, getTextInput(courseEt));
            jsonObject.put(EXTRA_KEY_SCORE, getTextInput(scoreEt));

            JsonObjectRequest request =
                    new JsonObjectRequest(Request.Method.POST, getString(R.string.url_server), jsonObject,
                                          new Response.Listener<JSONObject>() {
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

                                                      Intent intent = new Intent();
                                                      intent.putExtra("student", Student);
                                                      setResult(Activity.RESULT_OK, intent);
                                                      finish();
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
            requestQueue.add(request);
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
