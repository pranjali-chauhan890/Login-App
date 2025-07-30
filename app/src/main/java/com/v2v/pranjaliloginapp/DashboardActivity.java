package com.v2v.pranjaliloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        String name = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");

        if (name != null) {
            welcomeText.setText("Welcome " + name);
        } else if (username != null) {
            welcomeText.setText("Welcome " + username);
        } else {
            welcomeText.setText("Welcome!");
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
