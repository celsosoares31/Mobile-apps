package com.example.gestaoprojecto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaoprojecto.fragments.AddProjectFragment;
import com.example.gestaoprojecto.fragments.HomeFragment;
import com.example.gestaoprojecto.fragments.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        
        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);
        
        // check if the fields are not null then one current user is loggedIn
        if (email == null && password == null) {
            openLoginIntent();
        }
        
        replaceFragment(new HomeFragment(), true);
        
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
           
           int selectedItemId = item.getItemId();
           item.setChecked(true);
           if(selectedItemId == R.id.item_1){
               replaceFragment(new HomeFragment(), true);
           }
           if(selectedItemId == R.id.item_2){
               replaceFragment(new AddProjectFragment(), true);
           }
           if(selectedItemId == R.id.item_3){
               replaceFragment(new StatisticsFragment(), true);
           }
           if(selectedItemId == R.id.item_4){
               logout();
           }
           return false;
        });
    }
   
   private void logout() {
       new MaterialAlertDialogBuilder(this)
               .setMessage("Tem certeza que deseja sair")
               .setTitle("Confirme")
               .setPositiveButton("Sim", (dialog, which) -> {
                  SharedPreferences.Editor editor = sharedpreferences.edit();
                  
                  editor.clear();
                  editor.apply();
                  openLoginIntent();
               })
               .setNegativeButton("Nao", (dialog, which) -> {
               
               })
               .show();
   }
   
   private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
    private void openLoginIntent(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
