package com.example.minishop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.minishop.adapter.CustomerAdapter;

public class MainActivity extends AppCompatActivity {
   private GridView gridView;
   String[] names, price, description, stock;
   int[] images = {R.drawable.black_fashion_backpack, R.drawable.blue_t_shirt, R.drawable.green_t_shirt, R.drawable.mens_business_fashion, R.drawable.simple_beige_mens_shirt, R.drawable.three_piece_suit, R.drawable.wrist_watches};;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      
      names = getResources().getStringArray(R.array.products);
      description = getResources().getStringArray(R.array.description);
      price = getResources().getStringArray(R.array.price);
      stock = getResources().getStringArray(R.array.stock);
      
      gridView = findViewById(R.id.gridView);
      
      CustomerAdapter customerAdapter = new CustomerAdapter();
      gridView.setAdapter(customerAdapter);
      
      gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(
                    MainActivity.this, ProductView.class
            );
            intent.putExtra("imageId", images[position]);
            intent.putExtra("productName", names[position]);
            intent.putExtra("productPrice", price[position]);
            intent.putExtra("productQuantity", stock[position]);
            intent.putExtra("productDescription", description[position]);
            
            startActivity(intent);
         }
      });
   }
   
   public class CustomerAdapter extends BaseAdapter {
      
      @Override
      public int getCount() {
         return images.length;
      }
      
      @Override
      public Object getItem(int position) {
         return null;
      }
      
      @Override
      public long getItemId(int position) {
         return 0;
      }
      
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         View myView = getLayoutInflater().inflate(R.layout.item_view, null);
         
         TextView productName = myView.findViewById(R.id.productName);
         ImageView productImg = myView.findViewById(R.id.productImg);
         
         productName.setText(names[position]);
         productImg.setImageResource(images[position]);
         return myView;
      }
   }
}