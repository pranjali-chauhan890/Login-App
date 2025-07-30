package com.v2v.pranjaliloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText editTextName, editTextUsername, editTextPassword;
    private Button btnRegister;
    private TextView loginSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
        loginSwitch = findViewById(R.id.loginSwitch);

        loginSwitch.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("username", username); // manual registration
            startActivity(intent);
            finish();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.signInButton).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                saveToFireStore(user);
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("name", user.getDisplayName()); // Google Sign-In
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToFireStore(FirebaseUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", user.getDisplayName());
        map.put("email", user.getEmail());
        map.put("uid", user.getUid());

        db.collection("Users").document(user.getUid()).set(map);
    }
}