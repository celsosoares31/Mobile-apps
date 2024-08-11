package com.example.grocerylist.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.grocerylist.R;
import com.example.grocerylist.ViewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
   private FloatingActionButton floatBtn;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      floatBtn = findViewById(R.id.floatBtn);
      
      floatBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,
                    AddActivityView.class);
            startActivity(intent);
         }
      });
      ListView listView = (ListView) findViewById(R.id.list);
      ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
      progressBar.setVisibility(View.VISIBLE);
      
      MainActivityViewModel viewModel =
              new ViewModelProvider(this).get(MainActivityViewModel.class);
      viewModel.getShoppingList().observe(this, shoppingList ->{
         ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                 R.layout.simple_list_item_1, R.id.text1, shoppingList);
         listView.setAdapter(adapter);
          progressBar.setVisibility(View.GONE);
      });
      
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
         Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
         return insets;
      });
   }
}