package com.example.androidcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
   Button btnSecondScreen;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
    
      btnSecondScreen = findViewById(R.id.btnOpenSecondScreen);
      
      btnSecondScreen.setOnClickListener(v -> {
         
         Intent intent = new Intent(MainActivity.this, SecondScreen.class);
         startActivity(intent);
         
      });
   }
}