package com.example.control_gastos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.control_gastos.fragments.AddExpenseFragment;
import com.example.control_gastos.fragments.HomeFragment;
import com.example.control_gastos.fragments.ReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    SharedPreferences sharedpreferences;
    String email, password;
    private BottomNavigationView bottomNavigationView;
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
        
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            
            int selectedItemId = item.getItemId();
            item.setChecked(true);
            if(selectedItemId == R.id.item_1){
                replaceFragment(new AddExpenseFragment(), true);
            }
            if(selectedItemId == R.id.item_2){
                replaceFragment(new HomeFragment(), true);
            }
         
            if(selectedItemId == R.id.item_4){
                replaceFragment(new ReportsFragment(), true);
            }
            
            if(selectedItemId == R.id.item_3){
                logout();
            }
            return false;
        });
    }
    
    private void replaceFragment(Fragment fragment, boolean b) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        if(b) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
    
    private void openLoginIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    private boolean isLoggedIn() {
        // Aqui você pode implementar a lógica para verificar se o usuário está logado
        // Por exemplo, verificar se há uma sessão ativa, token válido, etc.
        // Neste exemplo básico, retorna sempre true para simular um usuário logado
        return true;
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
}
