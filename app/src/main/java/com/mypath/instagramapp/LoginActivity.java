package com.mypath.instagramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.parse.ParseException;
import com.parse.ParseUser;
//import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        tvSignup = findViewById(R.id.tvSignup);

        tvSignup.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Username Required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                return;
            }


            SignupUser(username, password);
        });
    }

    private void SignupUser(String username, String password) {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e == null) {
                Log.i(TAG, "User Sign Up Successful: " + user.getUsername());
            } else {
                Log.e(TAG, "Fail: ", e);
            }
        });
    }
}