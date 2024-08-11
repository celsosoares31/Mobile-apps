package com.example.control_gastos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;
    
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    
    // key for storing email.
    public static final String EMAIL_KEY = "email_key";
    
    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";
    
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        
        
        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        
        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);
        
        // check if the fields are not null then one current user is loggedIn
        if(email != null && password != null) {
            goToMain();
        }
        
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
                // to check if the user fields are empty or not.
                if(!validateLogin(username, password)) {
                    // this method will call when email and password fields are empty.
                    Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    if(isValidLogin(username, password)) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        
                        // below two lines will put values for
                        // email and password in shared preferences.
                        editor.putString(EMAIL_KEY, username);
                        editor.putString(PASSWORD_KEY, password);
                        
                        // to save our data with key and value.
                        editor.apply();
                        
                        // starting new activity.
                        goToMain();
                    }else {
                        Toast.makeText(LoginActivity.this, "Dados invalidos", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        });
    }
    
    private boolean isValidLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }
    
    private void goToMain() {
    Intent i = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(i);
    finish();
}
    private boolean validateLogin(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }
}
