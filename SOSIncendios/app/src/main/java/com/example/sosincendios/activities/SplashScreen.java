package com.example.sosincendios.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosincendios.MainActivity;
import com.example.sosincendios.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_splash_screen);
      
      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            if(FirebaseAuth.getInstance().getCurrentUser() != null || GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null){
               Intent intent = new Intent(SplashScreen.this, MainActivity.class);
               startActivity(intent);
               finish();
            }else{
               Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
               startActivity(intent);
               finish();
            }
            
         }
      }, 3000);
   }
}