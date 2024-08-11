package com.example.timepickerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalculationResultView extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_calculation_result_view);
      
      TextView timeAct1 = findViewById(R.id.timeAct1);
      TextView timeAct2 = findViewById(R.id.timeAct2);
      TextView timeAct3 = findViewById(R.id.timeAct3);
      TextView totalDuration = findViewById(R.id.totalDuration);
      
      Intent intent = getIntent();
      timeAct1.setText(intent.getStringExtra("time1"));
      timeAct2.setText(intent.getStringExtra("time2"));
      timeAct3.setText(intent.getStringExtra("time3"));
      totalDuration.setText(intent.getStringExtra("timeTotal"));
   }

   public void backToMain(View view) {
      MainActivity.totalTime = 0;
      CalculationResultView.this.finish();
   }
}