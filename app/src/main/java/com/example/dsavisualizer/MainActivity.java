package com.example.dsavisualizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private Handler mainHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainHandler.postDelayed(runnable, 3000);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), homePage.class));
            finish();
        }
    };

}