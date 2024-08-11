package com.example.sosincendios;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sosincendios.activities.LoginActivity;
import com.example.sosincendios.databinding.ActivityMainBinding;
import com.example.sosincendios.fragments.AboutFragment;
import com.example.sosincendios.fragments.FireDetailsFragment;
import com.example.sosincendios.fragments.HomeFragment;
import com.example.sosincendios.fragments.NewsFragment;
import com.example.sosincendios.fragments.StatisticsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
   private static final String DBNAME = "fires";
   private ActivityMainBinding binding;
   private NavigationView navigationView;
   private DrawerLayout drawerLayout;
   private ActionBarDrawerToggle actionBarDrawerToggle;
   private Toolbar toolbar;
   private GoogleSignInOptions googleSignInOptions;
   private GoogleSignInClient googleSignInClient;
   private TextView userName;
   private TextView userEmail;
   private CircleImageView userPhoto;
   private MenuItem logoutBtn;
   private FireViewModel fireViewModel;
   FloatingActionButton floatingActionButton;
   private FirebaseAuth auth;
   private ArrayList<Fire> fireList;
   private FireAdapter fireAdapter;
   private CircularProgressIndicator progressIndicator;
   private DatabaseReference reference;
   
   // Declare the launcher at the top of your Activity/Fragment:
   private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
      if(isGranted) {
         // FCM SDK (and your app) can post notifications.
      }else {
         Toast.makeText(this, "Nao sera ", Toast.LENGTH_SHORT)
                 .show();
      }
   });
   
   private void askNotificationPermission() {
      // This is only necessary for API level >= 33 (TIRAMISU)
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.
         }else if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
         }else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
         }
      }
   }
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      
      FirebaseApp.initializeApp(this);
      FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
      firebaseAppCheck.installAppCheckProviderFactory(
              PlayIntegrityAppCheckProviderFactory.getInstance());
      
      String languageToLoad = "pt-br";
      Locale locale = new Locale(languageToLoad);
      Locale.setDefault(locale);
      
      fireViewModel = new ViewModelProvider(this).get(FireViewModel.class);
      
      fireList = new ArrayList<>();
      toolbar = binding.mainToolbar;
      setSupportActionBar(toolbar);
      drawerLayout = binding.drawerLayout;
      navigationView = binding.navigationView;
      actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close);
      drawerLayout.addDrawerListener(actionBarDrawerToggle);
      actionBarDrawerToggle.syncState();
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      View header = navigationView.getHeaderView(0);
      
      checkIfConnected();
      askNotificationPermission();
      
      auth = FirebaseAuth.getInstance();
      FirebaseUser user = auth.getCurrentUser();
      
      if(user == null) {
         goToLogin();
      }
      
      configureUserProfile(user, header);
//      getUserTokenANdSaveToFirebase(user.getUid());
      
      reference = FirebaseDatabase.getInstance()
              .getReference(DBNAME);
      
      getAllFires(reference);
      
      if(savedInstanceState == null){
         HomeFragment homeFragment =HomeFragment.newInstance();
         replaceFragment(homeFragment, false);
      }
      
      navigationView.setNavigationItemSelectedListener(item -> {
         item.setChecked(true);
         int selectedItem = item.getItemId();
         if(selectedItem == R.id.menuItemHome) {
            HomeFragment homeFragment = HomeFragment.newInstance();
            replaceFragment(homeFragment, false);
         }else if(selectedItem == R.id.menuItemAbout) {
            replaceFragment(new AboutFragment(), true);
         }else if(selectedItem == R.id.menuItemLogout) {
            signOut();
         }else if(selectedItem == R.id.menuItemNews) {
            replaceFragment(new NewsFragment(), true);
         }else if(selectedItem == R.id.menuItemStatistics) {
            StatisticsFragment statisticsFragment = StatisticsFragment.newInstance();
            replaceFragment(statisticsFragment, true);
         }
         return true;
      });
   }
   
   private void configureUserProfile(FirebaseUser user, View header)
   {
      userName = header.findViewById(R.id.loggedUserName);
      userEmail = header.findViewById(R.id.loggedUserEmail);
      userPhoto = header.findViewById(R.id.loggedUserPhoto);
      
      if(user != null) {
         String personName = user.getDisplayName();
         String personEmail = user.getEmail();
         Uri profilePic = user.getPhotoUrl();
         
         if(personName == "") {
            personName = personEmail.substring(0, personEmail.indexOf("@"));
         }
         
         userName.setText(personName);
         userEmail.setText(personEmail);
         
         if(profilePic != null) {
            Glide.with(MainActivity.this)
                    .load(profilePic)
                    .into(userPhoto);
         }
         
      }
      
   }
   
   private void getUserTokenANdSaveToFirebase(String uid) {
      FirebaseMessaging.getInstance()
              .getToken()
              .addOnCompleteListener(task -> {
                 if(!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                 }
                 
                 // Get new FCM registration token
                 String token = task.getResult();
                 Log.w(TAG, "Fetching FCM registration token " + token, task.getException());
                 FirebaseDatabase.getInstance()
                         .getReference("users")
                         .child(uid)
                         .child("token")
                         .setValue(token)
                         .addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Token saved", Toast.LENGTH_SHORT)
                                 .show())
                         .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                                 .show());
              });
   }
   
   private void checkIfConnected() {
      if(NetworkUtil.isConnected(this)) {
         googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
      }else {
         Toast.makeText(this, "Nao esta conectado a Internet", Toast.LENGTH_LONG)
                 .show();
      }
   }
   
   private void replaceFragment(Fragment fragment, boolean addToBackStack) {
      drawerLayout.closeDrawer(GravityCompat.START);
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.frameLayout, fragment);
      if(addToBackStack) {
         fragmentTransaction.addToBackStack(null);
      }
      fragmentTransaction.commit();
      
      // Enable the Up button in the ActionBar if not home fragment
      getSupportActionBar().setDisplayHomeAsUpEnabled(!(fragment instanceof FireDetailsFragment));
   }
   
   private void getAllFires(DatabaseReference reference) {
      reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            fireList.clear();
            snapshot.getChildren()
                    .forEach(fireSnapshot -> {
                       Fire fire = fireSnapshot.getValue(Fire.class);
                       if(fire != null) {
                          fireList.add(fire);
                       }
                    });
            fireViewModel.setFireList(fireList);
         }
         @Override
         public void onCancelled(@NonNull DatabaseError error) {
         
         }
      });
      
   }
   
   private void signOut() {
      drawerLayout.closeDrawer(GravityCompat.START);
      auth.signOut();
      goToLogin();
   }
   
   private void goToLogin() {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
   }
   
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
         return true;
      }
      return super.onOptionsItemSelected(item);
   }
   
   @Override
   public void onBackPressed() {
      
      Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
      if(currentFragment instanceof onBackPressedListener) {
         boolean handled = ((onBackPressedListener) currentFragment).onBackPressed();
         if(!handled) {
            super.onBackPressed();
         }
      }else {
         super.onBackPressed();
      }
      
   }
   
   @Override
   protected void onStart() {
      super.onStart();
      reference = FirebaseDatabase.getInstance()
              .getReference(DBNAME);
      getAllFires(reference);
   }
}