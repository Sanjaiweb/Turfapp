package com.example.turfapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    // Duration for splash screen display (3 seconds)
    private static final int SPLASH_SCREEN_TIMEOUT = 3000;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();  // Initialize Firebase Auth

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));

                finish();  // Close SplashScreenActivity
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
