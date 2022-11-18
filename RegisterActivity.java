package com.poc.eldotalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.poc.eldotalk.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatButton btnRegister;
    private EditText etName, etPhone, etAddress;
    private EditText etEmail, etPassword, etConfirmPwd;
    private RadioGroup rgGender;
    private RelativeLayout rlDob;
    private TextView tvDateValue;
    private ImageView ivCal;
    private String gender = "Male";
    private String username, dob, phoneNo, address, email, password, confirmPwd;
    private CoordinatorLayout clLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
    }

    private void initUi() {
        initWidgets();
        getGenderVal();
        getDate();

        btnRegister.setOnClickListener(view -> {
            storeUserdata();
        });
    }

    private void storeUserdata() {
        if (validateForm()) {
            SharedPreferences preferences = getSharedPreferences(Constants.ELDO_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.USERNAME, username);
            editor.putString(Constants.DOB, dob);
            editor.putString(Constants.PHONE_NO, phoneNo);
            editor.putString(Constants.GENDER, gender);
            editor.putString(Constants.ADDRESS, address);
            editor.putString(Constants.EMAIL, email);
            editor.putString(Constants.PASSWORD, password);
            editor.putString(Constants.CONFIRM_PWD, confirmPwd);
            editor.apply();

            navigateToDashBoard();
        } else {
            //Handle here for empty fields data
        }
    }

    private void navigateToDashBoard() {
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private boolean validateForm() {
        username = etName.getText().toString().trim();
        dob = tvDateValue.getText().toString();
        phoneNo = etPhone.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPwd = etConfirmPwd.getText().toString().trim();
        if (username.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter username",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dob.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please select date",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneNo.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter phone number",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter address",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter email address",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter valid email address",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Snackbar snackbar = Snackbar.make(clLayout, "Please enter password",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confirmPwd)) {
            Snackbar snackbar = Snackbar.make(clLayout, "Password and confirm password fields value must be matched",
                    Snackbar.LENGTH_LONG).setAction("UNDO",
                    view -> {
                    });
            snackbar.show();
            //Toast.makeText(this, "Password and confirm password fields value must be matched", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void getDate() {
        /** To get current date and set value*/
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);
        String formattedDate = simpleDateFormat.format(c);
        tvDateValue.setText(formattedDate);

        /** To get selected date and set value*/
        rlDob.setOnClickListener(view -> {
            final Calendar c1 = Calendar.getInstance();
            int year = c1.get(Calendar.YEAR);
            int month = c1.get(Calendar.MONTH);
            int day = c1.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    RegisterActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // on below line we are setting date to our text view.
                        tvDateValue.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void getGenderVal() {
        rgGender.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton selectedRB = (RadioButton) findViewById(checkedId);
            gender = selectedRB.getText().toString();
        });
    }

    private void initWidgets() {
        btnRegister = (AppCompatButton) findViewById(R.id.btn_register);
        etName = (EditText) findViewById(R.id.et_name);
        rlDob = (RelativeLayout) findViewById(R.id.rl_date_picker);
        etPhone = (EditText) findViewById(R.id.et_phone_no);
        etAddress = (EditText) findViewById(R.id.et_address);
        etEmail = (EditText) findViewById(R.id.et_mail_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPwd = (EditText) findViewById(R.id.et_confirm_pwd);
        rgGender = (RadioGroup) findViewById(R.id.rg_gender);
        tvDateValue = (TextView) findViewById(R.id.tv_date_value);
        ivCal = (ImageView) findViewById(R.id.iv_calendar);
        clLayout = (CoordinatorLayout) findViewById(R.id.cl_layout);
    }
}