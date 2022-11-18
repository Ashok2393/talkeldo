package com.poc.eldotalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.poc.eldotalk.utils.Constants;

public class LaunchActivity extends AppCompatActivity {

    private AppCompatButton btnLogin;
    private AppCompatButton btnSignUp;
    private String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initUi();
    }

    private void initUi() {
        initWidgets();
        getIntentData();
        btnLogin.setOnClickListener(view -> navigateToLogin());
        btnSignUp.setOnClickListener(view -> navigateToRegister());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USERNAME) && intent.hasExtra(Constants.PASSWORD)) {
            username = intent.getStringExtra(Constants.USERNAME);
            password = intent.getStringExtra(Constants.PASSWORD);
        }
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LaunchActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
        intent.putExtra(Constants.USERNAME, username);
        intent.putExtra(Constants.PASSWORD, password);
        startActivity(intent);
    }

    private void initWidgets() {
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        btnSignUp = (AppCompatButton) findViewById(R.id.btn_signup);
    }
}