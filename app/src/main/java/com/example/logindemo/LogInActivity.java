package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity {

    private EditText emailId;
    private EditText password;
    private Button logIn;
    private TextView toSignUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logIn);
        toSignUp = findViewById(R.id.goToSignUp);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(LogInActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LogInActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Please provide your email and password", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    emailId.setError("Please enter a valid email");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter the correct password");
                    password.requestFocus();
                } else if (!(email.isEmpty() && pwd.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LogInActivity.this, "Sign up unsuccessfully, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LogInActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
