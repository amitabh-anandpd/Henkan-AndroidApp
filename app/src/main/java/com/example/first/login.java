package com.example.first;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class login extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Initialize Retrofit for backend API calls
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amitabhanandpd.pythonanywhere.com/") // Use 10.0.2.2 for localhost in Android emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    // Redirect to Signup Page
    public void signupPage(View view) {
        Intent i = new Intent(this, signup.class);
        startActivity(i);
    }

    // Handle Login Check
    public void loginCheck(View view) {
        EditText userView = findViewById(R.id.usernameInput);
        EditText passView = findViewById(R.id.passInput);
        String email = userView.getText().toString();
        String password = passView.getText().toString();

        if (email.isEmpty()) {
            userView.setError("Email cannot be empty!");
            return;
        } else if (password.isEmpty()) {
            passView.setError("Password cannot be empty!");
            return;
        }

        Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code()==200 && response.body() != null) {
                    User user = response.body().getUser();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getId());
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Toast.makeText(login.this, "Login successful! User ID: " + user.getId(), Toast.LENGTH_SHORT).show();

                    // Navigate to homepage
                    startActivity(new Intent(login.this, homepage.class));
                    finish();
                } else {
                    Toast.makeText(login.this, "Error: "+response.code()+" "+ response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(login.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Optionally redirect directly to homepage
    public void gotoHome(View view) {
        startActivity(new Intent(this, homepage.class));
        finish();
    }
}
