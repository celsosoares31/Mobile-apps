package com.example.miniquestionario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
   
   private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4;
   private int[] correctAnswers = {R.id.option1_1, R.id.option2_3, R.id.option3_4, R.id.option4_2};
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      radioGroup1 = findViewById(R.id.radioGroup1);
      radioGroup2 = findViewById(R.id.radioGroup2);
      radioGroup3 = findViewById(R.id.radioGroup3);
      radioGroup4 = findViewById(R.id.radioGroup4);
      findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int score = calculateScore();
            Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
         }
      });
   }
   
   private int calculateScore() {
      int score = 0;
      if(radioGroup1.getCheckedRadioButtonId() == correctAnswers[0]) {
         score++;
      }
      if(radioGroup2.getCheckedRadioButtonId() == correctAnswers[1]) {
         score++;
      }
      if(radioGroup3.getCheckedRadioButtonId() == correctAnswers[2]) {
         score++;
      }
      if(radioGroup4.getCheckedRadioButtonId() == correctAnswers[3]) {
         score++;
      }
      return score;
   }
}