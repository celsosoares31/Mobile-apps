package com.example.grocerylist.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.grocerylist.R;
import com.example.grocerylist.ViewModel.MainActivityViewModel;

public class AddActivityView extends AppCompatActivity {
   private TextView input;
   @SuppressLint("MissingInflatedId")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_add_view);
      input = findViewById(R.id.editTextText);
   }
   
   public void addItem(View view) {
      String txt = input.getText().toString();
      
      MainActivityViewModel viewModel = new MainActivityViewModel();
      viewModel.addItemToList(txt);
      AddActivityView.this.finish();
   }
}