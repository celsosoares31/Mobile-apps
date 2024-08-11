package com.example.miniquestionario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultadoActivity extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_resultado);
      Intent intent = getIntent();
      int score = intent.getIntExtra("score", 0);
      TextView scoreText = findViewById(R.id.score_text);
      TextView finalScore = findViewById(R.id.final_score);
      scoreText.setText("Você acertou " + score + " de 4 perguntas.");
      finalScore.setText("Nota Final: " + score + "/4");
   }
}