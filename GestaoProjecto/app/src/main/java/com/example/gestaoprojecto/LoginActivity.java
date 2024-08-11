package com.example.gestaoprojecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
   
   // creating constant keys for shared preferences.
   public static final String SHARED_PREFS = "shared_prefs";
   
   // key for storing email.
   public static final String EMAIL_KEY = "email_key";
   
   // key for storing password.
   public static final String PASSWORD_KEY = "password_key";
   
   // variable for shared preferences.
   SharedPreferences sharedpreferences;
   String email, password;
   private EditText etUsername, etPassword;
   private Button btnLogin;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);
      
      etUsername = findViewById(R.id.etUsername);
      etPassword = findViewById(R.id.etPassword);
      btnLogin = findViewById(R.id.btnLogin);
      
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
      
      btnLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // to check if the user fields are empty or not.
            if(TextUtils.isEmpty(etUsername.getText()
                    .toString()) && TextUtils.isEmpty(etPassword.getText()
                    .toString())) {
               // this method will call when email and password fields are empty.
               Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT)
                       .show();
            }else {
               if(isValidLogin(etUsername.getText()
                       .toString(), etPassword.getText()
                       .toString())) {
                  SharedPreferences.Editor editor = sharedpreferences.edit();
                  
                  // below two lines will put values for
                  // email and password in shared preferences.
                  editor.putString(EMAIL_KEY, etUsername.getText()
                          .toString());
                  editor.putString(PASSWORD_KEY, etPassword.getText()
                          .toString());
                  
                  // to save our data with key and value.
                  editor.apply();
                  
                  // starting new activity.
                  goToMain();
               }else {
                  Toast.makeText(LoginActivity.this, "Dados invalidos", Toast.LENGTH_SHORT)
                          .show();
               }
            }
         }
      });
   }
   
   private void goToMain() {
      Intent i = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(i);
      finish();
   }
   
   // Método de simulação de validação de login
   private boolean isValidLogin(String username, String password) {
      // Aqui você pode implementar sua lógica real de validação de login
      // Por exemplo, comparando com dados armazenados em um banco de dados ou serviço
      return username.equals("admin") && password.equals("1234");
   }
}
