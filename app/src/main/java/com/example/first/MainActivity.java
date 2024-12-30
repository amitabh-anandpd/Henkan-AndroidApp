package com.example.first;

import android.content.*;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private List<User> users = new ArrayList<>();
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amitabhanandpd.pythonanywhere.com/") // Use 10.0.2.2 for localhost in Android emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        if (CheckInternet.isConnected(this)) {
            InternetPopup.showNoInternetDialog(this, this::retryConnection);
        }
        fetchAllUsers();
        // Check login state
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        int userId = sharedPreferences.getInt("userId", -1);

        if (isLoggedIn) {
            if (userId != -1) {
                startActivity(new Intent(this, homepage.class));
                finish();
            }
            else{
                Toast.makeText(this, "Error: User ID not found"+userId, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, login.class));
                finish();
            }
        } else {
            // If not logged in, go to login page
            startActivity(new Intent(this, login.class));
            finish();
        }
    }

    private void retryConnection() {
        if (CheckInternet.isConnected(this)) {
            InternetPopup.showNoInternetDialog(this, this::retryConnection); // Show the dialog again
        } else {
            return;
        }
    }

    private void fetchAllUsers() {
        Call<UserListResponse> call = apiService.getAllUsers("VEX96Amitabh");
        call.enqueue(new retrofit2.Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, retrofit2.Response<UserListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users = response.body().getUsers(); // Extract the users list
                } else {
                    // Handle the error (e.g., show a message)
                    Toast.makeText(MainActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                // Handle the failure (e.g., no network, server error)
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
