package com.v2v.login1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName, editUsername, editPassword;
    Button btnRegister;
    TextView loginSwitch;
    SharedPreferences sharedPreferences;
    UserDBHelper db;

    public static final String PREF_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.registerName);
        editUsername = findViewById(R.id.registerUsername);
        editPassword = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.registerButton); // Register button
        loginSwitch = findViewById(R.id.loginSwitch);

        db = new UserDBHelper(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        btnRegister.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            db.registerUser(name, username, password);

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

            // Save username to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();

            // Go to User screen
            Intent intent = new Intent(MainActivity.this, User.class);
            startActivity(intent);
            finish();
        });

        loginSwitch.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}