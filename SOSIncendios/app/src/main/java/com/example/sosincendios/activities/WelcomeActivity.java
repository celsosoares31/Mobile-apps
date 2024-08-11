package com.example.sosincendios.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosincendios.MainActivity;
import com.example.sosincendios.databinding.ActivityWelcomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.xml.transform.Result;

public class WelcomeActivity extends AppCompatActivity {
   ActivityWelcomeBinding binding;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
      
      setContentView(binding.getRoot());
      
      binding.welcomeGetStartedBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Log.d("WELCOME_ACTIVITY", "Clicou");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            Log.d("WELCOME_ACTIVITY", "Yes is working");
            finish();
         }
      });
   }
}
