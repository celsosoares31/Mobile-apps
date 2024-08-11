package com.example.simplecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
   
   private EditText num1, num2;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      
      num1 = (EditText) (findViewById(R.id.num1));
      num2 = (EditText) (findViewById(R.id.num2));
   }
   
   private boolean checkIfFilled(){
      if(this.num1.getText().toString().isEmpty() || this.num2.getText().toString().isEmpty()){
        Toast.makeText(this, "Deve preencher ambos os campos primeiro",
                Toast.LENGTH_SHORT).show();
        return false;
      }
      return true;
      
   }
   private boolean checkIfIsDivisible(){
      float num2Float = Float.valueOf(num2.getText().toString());
      if(num2Float == 0){
         Toast.makeText(this, "O denominador deve ser diferente de " +
                 "\"zero\"", Toast.LENGTH_SHORT).show();
         return false;
      }
      return true;
   }
   private void openResultView(String value, String op){
      String[] arrayRes = new String[]{value,op};
      
      Intent intent = new Intent(MainActivity.this, ResultView.class);
      intent.putExtra("result", arrayRes);
      startActivity(intent);
   }
   public void addTwoNumbers(View v){
      float calcRes;
      if(checkIfFilled()){
         calcRes =
                 Float.parseFloat(num1.getText().toString()) + Float.parseFloat(num2.getText().toString());
         openResultView(String.valueOf(calcRes), "Soma");
         
      }
      
   }
   public void subtractTwoNumbers(View v){
      float calcRes;
      if(checkIfFilled()){
         calcRes =
                 Float.parseFloat(num1.getText().toString()) - Float.parseFloat(num2.getText().toString());
         openResultView(String.valueOf(calcRes), "Subtracao");
         
      }
   }
   public void dividTwoNumbers(View v){
      float calcRes;
      if(checkIfFilled()){
         if(checkIfIsDivisible()){
            calcRes =
                    Float.parseFloat(num1.getText().toString()) / Float.parseFloat(num2.getText().toString());
            openResultView(String.valueOf(calcRes), "Divisao");
         }
      }
   }
   public void multiplyTwoNumbers(View v){
      float calcRes;
      if(checkIfFilled()){
            calcRes = Float.parseFloat(num1.getText().toString()) * Float.parseFloat(num2.getText().toString());
         openResultView(String.valueOf(calcRes), "Multiplicacao");
         
      }
   }
   
   public void powerTwoNumbers(View view) {
      double calcRes;
      if(checkIfFilled()){
         double base = Float.parseFloat(num1.getText().toString());
         double exp = Float.parseFloat(num2.getText().toString());
         
         calcRes = Math.pow(base, exp);
         openResultView(String.valueOf(calcRes), "Potencia");
      }
   }
   
   public void factorial(View view) {
      num2.setText("");
      num2.setEnabled(false);
      
      if(this.num1.getText().toString().isEmpty()){
         Toast.makeText(this, "Preencha o campo acima primeiro",
                 Toast.LENGTH_SHORT).show();
      }else{
         int inputValue = Integer.parseInt(num1.getText().toString());
         int fact = 1;
         
         for(int i = 1; i <= inputValue; i++) {
            fact *= i;
         }
         openResultView(String.valueOf(fact), "factorial");
         num2.setEnabled(true);
      }
   }
   
   public void naturalLog(View view) {
      num2.setText("");
      num2.setEnabled(false);
      
      if(this.num1.getText().toString().isEmpty()){
         Toast.makeText(this, "Preencha o campo acima primeiro",
                 Toast.LENGTH_SHORT).show();
      }else{
         int inputValue = Integer.parseInt(num1.getText().toString());
         
         openResultView(String.valueOf(Math.log(inputValue)), "Log Natural");
         num2.setEnabled(true);
      }
   }
}
