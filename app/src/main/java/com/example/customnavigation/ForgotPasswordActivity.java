package com.example.customnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.customnavigation.ui.login.StudentLoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText username,email;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initWidget();
        setListeners();
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentPassword();
                Intent i  = new Intent(ForgotPasswordActivity.this, StudentLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void sentPassword() {
    }

    private void initWidget() {
        username = findViewById(R.id.username_forgot_pass);
        email = findViewById(R.id.email_forgot_pass);
        submit = findViewById(R.id.submit_forgot_pass);
    }
}