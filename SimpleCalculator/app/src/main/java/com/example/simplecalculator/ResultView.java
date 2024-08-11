package com.example.simplecalculator;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultView extends AppCompatActivity {
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_result_view);
      
      TextView resultOutput = (TextView) (findViewById(R.id.resultOutput));
      TextView txtOperation = (TextView) (findViewById(R.id.txtOperation));
      
      Button backButton = (Button) (findViewById(R.id.backBtn));
      Intent intent = getIntent();
      String[] res = intent.getStringArrayExtra("result");
      
      assert res != null;
      txtOperation.setText(res[1]);
      resultOutput.setText(res[0]);
      
      backButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            ResultView.this.finish();
         }
      });
   }
}