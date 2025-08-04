package com.v2v.login1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {

    TextView welcomeText;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        welcomeText = findViewById(R.id.welcomeText);
        logoutBtn = findViewById(R.id.logoutBtn);

        SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "User");
        welcomeText.setText("Hello " + username);

        logoutBtn.setOnClickListener(v -> {
            // Clear shared preferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            // Go to login screen
            Intent intent = new Intent(User.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
