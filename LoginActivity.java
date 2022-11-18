package com.poc.eldotalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.poc.eldotalk.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton btnLogin;
    private EditText etUserName, etPassword;
    private String username = null, password = null;
    private String intentUN, intentPwd;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
    }

    private void initUi() {
        initWidgets();
        getIntentData();
        btnLogin.setOnClickListener(view -> onLoginPress());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USERNAME) && intent.hasExtra(Constants.PASSWORD)) {
            intentUN = intent.getStringExtra(Constants.USERNAME);
            intentPwd = intent.getStringExtra(Constants.PASSWORD);

            if (!intentUN.isEmpty() && !intentPwd.isEmpty()) {
                preferences = getSharedPreferences(Constants.ELDO_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.USERNAME, intentUN);
                editor.putString(Constants.PASSWORD, intentPwd);
                editor.apply();
            }
        }
    }

    private void onLoginPress() {
        username = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            validateUser();
            //launchDashBoard();
        } else {
            Toast.makeText(this, "Please enter username & password to login", Toast.LENGTH_LONG).show();
        }
    }

    private void launchDashBoard() {
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void validateUser() {
        preferences = getSharedPreferences(Constants.ELDO_PREFS, Context.MODE_PRIVATE);
        String prefsUsername = preferences.getString(Constants.USERNAME, "");
        String prefsPassword = preferences.getString(Constants.PASSWORD, "");
        if (prefsUsername.isEmpty() && prefsPassword.isEmpty()) {
            Toast.makeText(this, "Register yourself before logging in.", Toast.LENGTH_SHORT).show();
        } else {
            if (prefsUsername.equals(username) && prefsPassword.equals(password)) {
                launchDashBoard();
            } else {
                Toast.makeText(this, "Username and password do not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initWidgets() {
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

}