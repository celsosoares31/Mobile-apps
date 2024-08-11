package com.example.notificationapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notificationapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
   private static final int PERMISSION_REQUEST_CODE = 200;
   private ActivityMainBinding binding;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
         if(ActivityCompat.checkSelfPermission(this,
                 Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            };
         }
      }
      
   }
  
   