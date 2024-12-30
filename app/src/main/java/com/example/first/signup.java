package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class signup extends AppCompatActivity {
    private static final String REGISTER_URL = "https://amitabhanandpd.pythonanywhere.com/api/users/register"; // Replace <server_ip> with your Flask server's IP address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
    }
    // Navigate to the login page
    public void loginPage(View view) {
        startActivity(new Intent(this, login.class));
    }

    // Validate user inputs and register a new user
    public void validateUser(View v) {
        // Get input fields
        EditText usernameField = findViewById(R.id.newUsername);
        EditText emailField = findViewById(R.id.newEmail);
        EditText passwordField1 = findViewById(R.id.newPassword);
        EditText passwordField2 = findViewById(R.id.newPassword2);

        // Extract input values
        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password1 = passwordField1.getText().toString();
        String password2 = passwordField2.getText().toString();

        // Validate inputs
        if (username.isEmpty()) {
            usernameField.setError("Username cannot be empty!");
            return;
        }
        if (email.isEmpty()) {
            emailField.setError("Email cannot be empty!");
            return;
        }
        if (password1.isEmpty()) {
            passwordField1.setError("Password cannot be empty!");
            return;
        }
        if (password2.isEmpty()) {
            passwordField2.setError("Password cannot be empty!");
            return;
        }
        if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Make a request to the Flask server
        registerUser(username, email, password1);
    }

    private void registerUser(String name, String email, String password) {
        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("name", name);
            payload.put("address", ""); // Address is optional, use an empty string for now
            payload.put("email", email);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating registration data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send request to Flask server
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                payload,
                response -> {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(signup.this, message, Toast.LENGTH_SHORT).show();
                        // Redirect to login page
                        startActivity(new Intent(signup.this, login.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(signup.this, "Error processing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMessage = "Registration failed";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            JSONObject errorObj = new JSONObject(new String(error.networkResponse.data));
                            errorMessage = errorObj.optString("error", errorMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(signup.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
