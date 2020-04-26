package com.example.sampleprojectserver;

import android.util.Log;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String TAG                  = "ApiService";
    private static final String EXTRA_KEY_FIRST_NAME = "first_name";
    private static final String EXTRA_KEY_LAST_NAME  = "last_name";
    private static final String EXTRA_KEY_COURSE     = "course";
    private static final String EXTRA_KEY_SCORE      = "score";
    private static final String EXTRA_KEY_ID         = "id";
    private static final String BASE_URL_SERVER_API  = "http://expertdevelopers.ir/api/v1/";

    private RetrofitApiService retrofitApiService;

    public ApiService() {
        /*for initialize header*/
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request         oldRequest        = chain.request();
                Request.Builder newRequestBuilder = oldRequest.newBuilder();
                newRequestBuilder.addHeader("Accept", "application/json");
                return chain.proceed(newRequestBuilder.build());
            }
        }).build();
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(BASE_URL_SERVER_API).addConverterFactory(GsonConverterFactory.create())
                                      .client(okHttpClient).build();
        retrofitApiService = retrofit.create(RetrofitApiService.class);
    }

    public void saveStudent(String firstName, String lastName, String course, int score, final SaveStudentCallback callBack) {
        JsonObject jsonObjectBody = new JsonObject();
        jsonObjectBody.addProperty(EXTRA_KEY_FIRST_NAME, firstName);
        jsonObjectBody.addProperty(EXTRA_KEY_LAST_NAME, lastName);
        jsonObjectBody.addProperty(EXTRA_KEY_COURSE, course);
        jsonObjectBody.addProperty(EXTRA_KEY_SCORE, score);

        retrofitApiService.saveStudent(jsonObjectBody).enqueue(new Callback<StudentObject>() {
            @Override
            public void onResponse(@NotNull Call<StudentObject> call, @NotNull Response<StudentObject> response) {
                Log.i(TAG, "onResponse: post retrofit");
                callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<StudentObject> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: post retrofit");
                callBack.onError(new Exception(t));
            }
        });
    }

    public void getStudent(final StudentListCallback listCallback) {
        retrofitApiService.getStudents().enqueue(new Callback<List<StudentObject>>() {
            @Override
            public void onResponse(@NotNull Call<List<StudentObject>> call, @NotNull Response<List<StudentObject>> response) {
                Log.i(TAG, "onResponse: get retrofit");
                listCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<StudentObject>> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: get retrofit");
                listCallback.onError(new Exception(t));
            }
        });
    }

    public void cancel() {

    }

    public interface SaveStudentCallback {
        void onSuccess(StudentObject student);

        void onError(Exception error);
    }

    public interface StudentListCallback {
        void onSuccess(List<StudentObject> studentObjectList);

        void onError(Exception error);
    }
}
