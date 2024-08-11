package com.example.timepickerapp;

import static com.example.timepickerapp.R.id.act1StartBtn;
import static com.example.timepickerapp.R.id.act2StartBtn;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.StrongBoxUnavailableException;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   public Button act1StartBtn, act2StartBtn, act3StartBtn,
           act1EndBtn,
           act2EndBtn, act3EndBtn;
   public static long totalTime;
   private  int hour, minute;
   private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
   
   private Date timeStart1, timeStart2, timeStart3, timeEnd1, timeEnd2,
           timeEnd3;
   
   @SuppressLint("SimpleDateFormat")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      
      act1StartBtn = findViewById(R.id.act1StartBtn);
      act2StartBtn = findViewById(R.id.act2StartBtn);
      act3StartBtn = findViewById(R.id.act3StartBtn);
      
      act1EndBtn = findViewById(R.id.act1EndBtn);
      act2EndBtn = findViewById(R.id.act2EndBtn);
      act3EndBtn = findViewById(R.id.act3EndBtn);
      
      dateFormat.setLenient(false);
   }
   
   /**
    *
    * @param mbutton
    */
   @SuppressLint("ResourceType")
   public void setStartTimeBtn(View mbutton) {
      int value = mbutton.getId();
      TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
         @SuppressLint("NonConstantResourceId")
         @Override
         public void onTimeSet(TimePicker timePicker, int selectedHour,
                               int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
           
            if(value == R.id.act1StartBtn){
                  act1StartBtn.setText(String.format(Locale.getDefault(), "%02d" +
                          ":%02d",hour, minute));
            }else if(value == R.id.act2StartBtn) {
               act2StartBtn.setText(String.format(Locale.getDefault(), "%02d" + ":%02d", hour, minute));
            }
            else if(value == R.id.act3StartBtn) {
               act3StartBtn.setText(String.format(Locale.getDefault(),
                       "%02d" + ":%02d", hour, minute));
            }else if(value == R.id.act1EndBtn){
               act1EndBtn.setText(String.format(Locale.getDefault(), "%02d" +
                       ":%02d",hour, minute));
            }else if(value == R.id.act2EndBtn) {
               act2EndBtn.setText(String.format(Locale.getDefault(), "%02d" + ":%02d", hour, minute));
            }
            else if(value == R.id.act3EndBtn) {
               act3EndBtn.setText(String.format(Locale.getDefault(),
                       "%02d" + ":%02d", hour, minute));
            }
         }
      };
      int style = AlertDialog.THEME_HOLO_DARK;
      TimePickerDialog timePickerDialog = new TimePickerDialog(this, style,
              onTimeSetListener, hour, minute, true);
      if(value == R.id.act1EndBtn || value == R.id.act2EndBtn || value == R.id.act3EndBtn){
         timePickerDialog.setTitle("Hora de Fim");
      }else{
         timePickerDialog.setTitle("Hora de inicio");
      }
      
      timePickerDialog.show();
   }
   
   /**
    *
    * @return String
    * @throws ParseException
    */
   private String totalDuration() throws ParseException {
      long hr, min;
      String timeTotalStr;
      
      hr = (totalTime/(60*60*1000))%24;
      min = (totalTime/(60*1000))%60;
      
      timeTotalStr = String.format(Locale.getDefault(), "%02d" + ":%02d", hr,
              min);
      return  timeTotalStr;
   }
   
   /**
    *
    * @param startTime
    * @param endTime
    * @return String
    */
   private String calculateDiff(Date startTime, Date endTime){
      long timeDiff, hours, minutes;
      String timeDiffStr;
      timeDiff = Math.abs(startTime.getTime() - endTime.getTime());
      Date date = new Date(timeDiff);
      
      totalTime += timeDiff;
      
      hours = (timeDiff/(60*60*1000))%24;
      minutes = (timeDiff/(60*1000))%60;
      
      timeDiffStr = String.format(Locale.getDefault(), "%02d"+":%02d", hours,
              minutes);
      
      return timeDiffStr;
   }
   
   /**
    *
    * @param view
    * @throws ParseException
    */
   public void calculateTime(View view) throws ParseException {
      String time1, time2, time3, totalDuration;
      if(!isAllFilled()){
         Toast.makeText(this, "Insira os tempos de duracao de cada actividade",
                 Toast.LENGTH_SHORT).show();
      }else if(isValidTime()){
         Intent intent = new Intent(MainActivity.this,
                 CalculationResultView.class);
         
         time1 = calculateDiff(timeStart1, timeEnd1);
         time2 = calculateDiff(timeStart2, timeEnd2);
         time3 = calculateDiff(timeStart3, timeEnd3);
         
         intent.putExtra("time1", time1);
         intent.putExtra("time2", time2);
         intent.putExtra("time3", time3);
         totalDuration = totalDuration();
         intent.putExtra("timeTotal", totalDuration);
         
         startActivity(intent);
      };
   }
   
   /**
    * @description O metodo abaixo verifica se todos as horas foram
    * preenchidos, caso algum campo nao esteja preenchido ele retorna falso
    * @return boolean
    */
   private boolean isAllFilled(){
      return !act1StartBtn.getText()
              .toString()
              .equals("00:00") && !act2StartBtn.getText()
              .toString()
              .equals("00:00") && !act3StartBtn.getText()
              .toString()
              .equals("00:00") && !act1EndBtn.getText()
              .toString()
              .equals("00:00") && !act2EndBtn.getText()
              .toString()
              .equals("00:00") && !act3EndBtn.getText()
              .toString()
              .equals("00:00");
   }
   
   /**
    * @description O Metodo abaixo verifica a validade das horas inseridas,
    * veririfcando se a hora final é maior que a hora de inicio, caso isto
    * nao seja verdadeiro um toast é emitido informando ao usuario qual é a
    * actividade que tem o referido erro.
    * @return boolean
    */
   private boolean isValidTime(){
      try {
         timeStart1 = dateFormat.parse(act1StartBtn.getText().toString());
         timeStart2 = dateFormat.parse(act2StartBtn.getText().toString());
         timeStart3 = dateFormat.parse(act3StartBtn.getText().toString());
         
         timeEnd1 = dateFormat.parse(act1EndBtn.getText().toString());
         timeEnd2 = dateFormat.parse(act2EndBtn.getText().toString());
         timeEnd3 = dateFormat.parse(act3EndBtn.getText().toString());
         
      }catch(ParseException e) {
         throw new RuntimeException(e);
      }
      
      if(timeStart1.after(timeEnd1) ){
         if(timeStart1.equals(timeEnd1) ){
            Toast.makeText(this, "O tempo de inicio da actividade 1 é " +
                            "igua ao tempo de termino",
                    Toast.LENGTH_SHORT).show();
            return false;
         }
         Toast.makeText(this, "O tempo de inicio da actividade 1 é maior " +
                            "que o tempo de termino",
                    Toast.LENGTH_SHORT).show();
         return false;
         }
      else if(timeStart2.after(timeEnd2) ){
         if(timeStart2.equals(timeEnd2) ){
            Toast.makeText(this, "O tempo de inicio da actividade 2 é " +
                            "igua ao tempo de termino",
                    Toast.LENGTH_SHORT).show();
            return false;
         }
         Toast.makeText(this, "O tempo de inicio da actividade 2 é maior " +
                         "que o tempo de termino",
                 Toast.LENGTH_SHORT).show();
         return  false;
      }
      else if(timeStart3.after(timeEnd3) ){
         if(timeStart3.equals(timeEnd3) ){
            Toast.makeText(this, "O tempo de inicio da actividade 3 é " +
                            "igua ao tempo de termino",
                    Toast.LENGTH_SHORT).show();
            return false;
         }
         Toast.makeText(this, "O tempo de inicio da actividade 3 é maior " +
                         "que o tempo de termino",
                 Toast.LENGTH_SHORT).show();
         return false;
      }
    return true;
   }
}