package com.poc.eldotalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.poc.eldotalk.utils.Constants;

public class DashBoardActivity extends AppCompatActivity {

    private TextView tvCall;
    private Button btnDone;
    private ImageView ivLogout;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        initUi();
    }

    private void initUi() {
        initWidgets();
        checkUserState();
        setPhoneNum();
        tvCall.setOnClickListener(view -> {
            checkCallPermission(Manifest.permission.CALL_PHONE, Constants.CALL_PERMISSION_CODE);
        });
        btnDone.setOnClickListener(view -> {
            checkCallPermission(Manifest.permission.CALL_PHONE, Constants.CALL_PERMISSION_CODE);
        });

    }

    private void setPhoneNum() {
        SharedPreferences preferences = getSharedPreferences(Constants.ELDO_PREFS, MODE_PRIVATE);
        String username = preferences.getString(Constants.PHONE_NO, "9999999999");
        tvCall.setText(username);
    }

    private void initWidgets() {
        tvCall = (AppCompatTextView) findViewById(R.id.atv_call_box);
        btnDone = (AppCompatButton) findViewById(R.id.btn_done);
        ivLogout = (ImageView) findViewById(R.id.iv_toolbar_logout);
    }

    private void checkCallPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(DashBoardActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{permission}, requestCode);
        } else {
            performCall();
        }
    }

    private void checkUserState() {
        SharedPreferences preferences = getSharedPreferences("EldoPrefs", MODE_PRIVATE);
        String username = preferences.getString(Constants.USERNAME, "");
        if (username.isEmpty()) {
            navigateLaunchActivity();
        } else {
            getRelationArray();
            getSlots();
            showUserLogout();
        }
    }

    private void navigateLaunchActivity() {
        Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.USERNAME, username);
        intent.putExtra(Constants.PASSWORD, password);
        startActivity(intent);
        finish();
    }

    private void showUserLogout() {
        ivLogout.setVisibility(View.VISIBLE);
        ivLogout.setOnClickListener(view -> {
            showLogoutPopUp();
        });
    }

    private void showLogoutPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setMessage("Do you want to logout?");
        builder.setTitle("Alert!");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
            logoutUser();
        });

        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences(Constants.ELDO_PREFS, MODE_PRIVATE);

        username = preferences.getString(Constants.USERNAME, "");
        password = preferences.getString(Constants.PASSWORD, "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        navigateLaunchActivity();
    }

    private void getSlots() {
        String[] slots = getResources().getStringArray(R.array.slots);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, slots);
        AutoCompleteTextView autocompleteTV = (AutoCompleteTextView) findViewById(R.id.actv_prefer_time);
        autocompleteTV.setAdapter(arrayAdapter);
    }

    private void getRelationArray() {
        String[] relationships = getResources().getStringArray(R.array.relationships);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, relationships);
        AutoCompleteTextView autocompleteTV = (AutoCompleteTextView) findViewById(R.id.actv_choose_rel);
        autocompleteTV.setAdapter(arrayAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performCall();
            } else {
                Toast.makeText(this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performCall() {
        try {
            String number = tvCall.getText().toString().trim();
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            startActivity(callIntent);
        } catch (NumberFormatException e) {
            Log.e("TAG", "performCall: Number Format Exception " + e.getMessage());
        }
    }
}