package com.example.sosincendios.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosincendios.MainActivity;
import com.example.sosincendios.NetworkUtil;
import com.example.sosincendios.R;
import com.example.sosincendios.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
   private static final int RC_SIGN_IN = 400;
   private ActivityLoginBinding binding;
   private SignInButton signInButton;
   private GoogleSignInOptions googleSignInOptions;
   private GoogleSignInClient googleSignInClient;
   private CircularProgressIndicator circularProgressIndicator;
   private FirebaseAuth auth;
   private TextInputEditText loginEmail, loginPassword;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      
      //instanciacao do auth
      auth = FirebaseAuth.getInstance();
      
      circularProgressIndicator = binding.progressBar;
      loginEmail = binding.loginEmail;
      loginPassword = binding.loginPassword;
      
      googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
              .requestEmail()
              .build();
      googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
      signInButton = binding.signInButton;
      
      signInButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(NetworkUtil.isConnected(LoginActivity.this)) {
               signIn();
            }else {
               Toast.makeText(LoginActivity.this, "Voce nao esta conectado a " + "Internet", Toast.LENGTH_SHORT)
                       .show();
            }
         }
      });
      binding.loginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(NetworkUtil.isConnected(LoginActivity.this)) {
               signInWithEmailAndPassword(loginEmail.getText()
                       .toString()
                       .trim(), loginPassword.getText()
                       .toString()
                       .trim());
            }else {
               Toast.makeText(LoginActivity.this, "Voce nao esta conectado a " + "Internet", Toast.LENGTH_SHORT)
                       .show();
            }
            
         }
      });
      binding.loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
         }
      });
   }
   
   private boolean isAllFilled() {
      if(!TextUtils.isEmpty(loginEmail.getText()
              .toString()) && !TextUtils.isEmpty(loginPassword.getText()
              .toString())) {
         
      }else {
         Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT)
                 .show();
         return false;
      }
      return true;
   }
   
   private void signIn() {
      Intent intent = googleSignInClient.getSignInIntent();
      startActivityForResult(intent, RC_SIGN_IN);
   }
   
   private void signInWithEmailAndPassword(String email, String password) {
      if(isAllFilled()) {
         circularProgressIndicator.setVisibility(View.VISIBLE);
         auth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                          startActivity(intent);
                          circularProgressIndicator.setVisibility(View.GONE);
                          finish();
                       }
                    }
                 })
                 .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       
                       Toast.makeText(LoginActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT)
                               .show();
                       circularProgressIndicator.setVisibility(View.GONE);
                       
                    }
                    
                 });
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Log.d("Login onActivityResult", "resultCode: " + resultCode + ", requestCode: " + requestCode);
      
      if(requestCode == RC_SIGN_IN) {
         Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
         Log.d("Login onActivityResult", "Data = " + data.getExtras());
         
         try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account != null) {
               firebaseAuthWithGoogle(account);
            }else {
               Log.e("Login", "Google Sign-In account is null");
               Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT)
                       .show();
            }
         }catch(ApiException e) {
            Log.e("Login", "Google Sign-In failed", e);
            Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
         }
      }
   }
   //   @Override
   //   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   //      super.onActivityResult(requestCode, resultCode, data);
   //      Log.d("Login onActivityResult", "resultcode " + resultCode + "requestCode " + requestCode);
   //      if(requestCode == RC_SIGN_IN) {
   //         Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
   //         GoogleSignInAccount account = null;
   //         try {
   //            account = task.getResult(ApiException.class);
   //            firebaseAuthWithGoogle(account);
   //         }catch(ApiException e) {
   //            Toast.makeText(this, "Nao foi possivel fazer o login",
   //                            Toast.LENGTH_SHORT)
   //                    .show();
   //         }
   //      }
   //   }
   
   private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
      AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
      auth.signInWithCredential(credential)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                       // Sign-in success, update UI with the signed-in user's information
                       Log.d("firebaseAuthWithGoogle", "signInWithCredential:success");
                       FirebaseUser user = auth.getCurrentUser();
                       navigateToMainActivity();
                    }else {
                       Toast.makeText(LoginActivity.this, "A autenticacao " + "falhou", Toast.LENGTH_SHORT)
                               .show();
                    }
                 }
              });
   }
   
   private void navigateToMainActivity() {
      Intent intent = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(intent);
      circularProgressIndicator.setVisibility(View.INVISIBLE);
      finish();
   }
   
   //   private class LoadNewActivityTask extends AsyncTask<Void, Void, Void> {
   //
   //      @Override
   //      protected void onPreExecute() {
   //         super.onPreExecute();
   //         // Show the progress bar
   //         binding.progressBar.setVisibility(View.VISIBLE);
   //      }
   //
   //      @Override
   //      protected Void doInBackground(Void... voids) {
   //         // Perform any background operations
   //         // Simulate a delay to show the progress bar
   //         try {
   //            Thread.sleep(1000); // Simulate a long operation
   //            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
   //            startActivity(intent);
   //         }catch(InterruptedException e) {
   //            e.printStackTrace();
   //         }
   //         return null;
   //      }
   //
   //      @Override
   //      protected void onPostExecute(Void aVoid) {
   //         super.onPostExecute(aVoid);
   //      }
}
   