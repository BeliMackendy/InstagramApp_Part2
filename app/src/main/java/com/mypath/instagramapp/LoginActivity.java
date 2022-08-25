package com.mypath.instagramapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser()!=null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btLogin);
        TextView tvSignup = findViewById(R.id.tvSignup);

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

        btLogin.setOnClickListener(view -> {
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

            LoginUser(username, password);
        });
    }

    private void LoginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                // Hooray! The user is logged in.
                Log.i(TAG, "Login Success: "+ParseUser.getCurrentUser().getUsername());
                goMainActivity();
            } else {
                Log.e(TAG, "Login Failed: ",e );
            }
        });
    }

    private void SignupUser(String username, String password) {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e == null) {
                Log.i(TAG, "User Sign Up Successful: " + ParseUser.getCurrentUser().getUsername());
                goMainActivity();
            } else {
                Log.e(TAG, "Sign up Failed: ", e);
            }
        });
    }

    public void goMainActivity(){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }
}