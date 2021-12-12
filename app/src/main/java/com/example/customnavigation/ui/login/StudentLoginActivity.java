package com.example.customnavigation.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.customnavigation.ForgotPasswordActivity;
import com.example.customnavigation.MainActivity;
import com.example.customnavigation.ProfessorActivity;
import com.example.customnavigation.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.Constant;

public class StudentLoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    String login_url = Constant.BASE_URL + "student_login.php";
    String luser, lpass;
    Button loginButton;
    String is_signed_in = "";
    private TextView forgotPassword;
    int userType;
    SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;

    EditText usernameEditText;
    EditText passwordEditText;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        getSupportActionBar().hide();

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }

        initWidget();
        isSignedIn();
        setListeners();
    }

    private void isSignedIn() {
        if (is_signed_in.equals("true")) {
            Intent i;
            if (userType == 2) {
                i = new Intent(StudentLoginActivity.this, ProfessorActivity.class);
            } else if (userType == 3) {
                i = new Intent(StudentLoginActivity.this, MainActivity.class);
            } else if (userType == 4) {
                i = new Intent(StudentLoginActivity.this, MainActivity.class);
            } else {
                i = new Intent(StudentLoginActivity.this, ProfessorActivity.class);
            }
            startActivity(i);
            finish();
        }
    }

    private void initWidget() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        forgotPassword = findViewById(R.id.forgot_password);
        mPreferences = getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        is_signed_in = mPreferences.getString("issignedin", "false");
        userType = mPreferences.getInt("userType", 0);
    }

    private void setListeners() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                luser = usernameEditText.getText().toString();
                lpass = passwordEditText.getText().toString();

                if (luser.isEmpty() || lpass.isEmpty()) {
                    Toast.makeText(StudentLoginActivity.this, "please enter valid data", Toast.LENGTH_SHORT).show();
                } else {
                    Login();
                }
            }
        });
    }

    private void Login() {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("test123", "onResponse: " + response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("id");
                            int userType = jsonObject.getInt("user_type");
                            String name = jsonObject.getString("name");
                            String username = jsonObject.getString("username");
                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Logged In  Success", Toast.LENGTH_LONG).show();

                                preferencesEditor.putString("issignedin", "true");
                                preferencesEditor.putString("SignedInUserID", id);
                                preferencesEditor.putInt("userType", userType);
                                preferencesEditor.putString("SignedInName", name);
                                preferencesEditor.apply();
                                Intent i;
                                if (userType == 2) {
                                    i = new Intent(StudentLoginActivity.this, ProfessorActivity.class);
                                } else if (userType == 3) {
                                    i = new Intent(StudentLoginActivity.this, MainActivity.class);
                                } else if (userType == 4) {
                                    i = new Intent(StudentLoginActivity.this, MainActivity.class);
                                } else {
                                    i = new Intent(StudentLoginActivity.this, ProfessorActivity.class);
                                }
                                startActivity(i);
                                finish();
                            }
                            if (success.equals("0")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                            if (success.equals("3")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Please Enter valid Username and password", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Please check your connection" , Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", luser);
                params.put("password", lpass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}