package com.example.calcularparesimpares;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private TextInputEditText firstNum, secondNum;
   private TextView somaPares, viewPares;
   private TextView somaImpares, viewImpares;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
//      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      firstNum = findViewById(R.id.firstNum);
      secondNum = findViewById(R.id.secNum);
      viewPares = findViewById(R.id.viewPares);
      somaPares = findViewById(R.id.somaPares);
      viewImpares = findViewById(R.id.viewImpares);
      somaImpares = findViewById(R.id.somaImpares);
      
   }
   
   public void calculate(View view) {
     if(validateAllFields()){
        calculateSum();
     }
   }
   
   private void calculateSum() {
      int num1 = Integer.parseInt(firstNum.getText().toString().trim());
      int num2 = Integer.parseInt(secondNum.getText().toString().trim());
      
      List<Integer> numerosPares = new ArrayList<>();
      List<Integer> numerosImpares = new ArrayList<>();
      
      for(int i = num1+1; i<num2; i++){
         if(i%2 == 0){
            numerosPares.add(i);
         }else {
            numerosImpares.add(i);
         }
      }
      String sumPares = sumAll(numerosPares);
      String sumImpares = sumAll(numerosImpares);
      
      viewPares.setText(numerosPares.toString());
      viewImpares.setText(numerosImpares.toString());
      
      somaImpares.setText(sumImpares);
      somaPares.setText(sumPares);
   }
   
   private String sumAll(List<Integer> numeros) {
      int tot = 0;
      for(int i = 0; i < numeros.size(); i++) {
         tot += numeros.get(i);
      }
      return String.valueOf(tot);
   }
   
   private boolean validateAllFields() {
      
      if(!TextUtils.isEmpty(firstNum.getText()) && !TextUtils.isEmpty(secondNum.getText())) {
         Integer num1 = Integer.parseInt(firstNum.getText()
                 .toString()
                 .trim());
         Integer num2 = Integer.parseInt(secondNum.getText()
                 .toString()
                 .trim());
         
         if(num2 < num1) {
            Toast.makeText(this, "O segundo numero deve ser maior que o " + "primeiro", Toast.LENGTH_SHORT)
                    .show();
            return false;
         }
         return true;
      }else {
         Toast.makeText(this, "Ambos os campos devem ser preenchidos", Toast.LENGTH_SHORT)
                 .show();
         return  false;
      }
   }
}