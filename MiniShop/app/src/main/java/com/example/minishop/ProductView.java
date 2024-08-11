package com.example.minishop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductView extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_product_view);
      ImageView imageView = findViewById(R.id.gridItemImageView);
      TextView name = findViewById(R.id.productName);
      TextView price = findViewById(R.id.productPrice);
      TextView quantity = findViewById(R.id.productQuantity);
      TextView description = findViewById(R.id.productDescription);
      
      Intent intent = getIntent();
      imageView.setImageResource(intent.getIntExtra("imageId", 0));
      name.setText(intent.getStringExtra("productName"));
      price.setText(intent.getStringExtra("productPrice"));
      quantity.setText(intent.getStringExtra("productQuantity"));
      description.setText(intent.getStringExtra("productDescription"));
      
   }
}