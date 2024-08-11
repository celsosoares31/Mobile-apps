package com.example.sosincendios.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosincendios.MainActivity;
import com.example.sosincendios.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
   ActivityRegisterBinding binding;
   private TextInputEditText inputTextEmail_reg, inputTextPassword_reg, inputTextPasswordConfirm_reg;
   private FirebaseAuth mAuth;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      binding = ActivityRegisterBinding.inflate(getLayoutInflater());
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(binding.getRoot());
      
      mAuth = FirebaseAuth.getInstance();
      
      inputTextEmail_reg = binding.inputTextEmailReg;
      inputTextPassword_reg = binding.inputTextPasswordReg;
      inputTextPasswordConfirm_reg = binding.inputTextPasswordConfirmReg;
      
      binding.goToLoginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
         }
      });
      binding.registerUserBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            boolean allFilled = isAllValid();
            if(allFilled) {
               if(TextUtils.equals(inputTextPassword_reg.getText()
                       .toString()
                       .trim(), inputTextPasswordConfirm_reg.getText()
                       .toString()
                       .trim())) {
                  createUser(inputTextEmail_reg, inputTextPassword_reg);
                  
               }else {
                  Toast.makeText(RegisterActivity.this, "As senhas inseridas " + "sao diferentes", Toast.LENGTH_SHORT)
                          .show();
               }
            }
            
         }
      });
      
   }
   
   private void createUser(TextInputEditText inputTextEmailReg, TextInputEditText inputTextPasswordReg) {
      String email = inputTextEmailReg.getText()
              .toString()
              .trim();
      String password = inputTextPasswordReg.getText()
              .toString()
              .trim();
      
      mAuth.createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                       Intent intent = new Intent(RegisterActivity.this,
                               MainActivity.class);
                       startActivity(intent);
                       Toast.makeText(RegisterActivity.this, "Registado com " +
                                       "sucesso", Toast.LENGTH_SHORT)
                               .show();
                       finish();
                    }
                 }
              }).addOnFailureListener(this, new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                    inputTextPassword_reg.setError(e.getMessage());
                    inputTextPasswordConfirm_reg.setError(e.getMessage());
                 }
              });
   }
   
   
   private boolean isAllValid() {
      String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     if(TextUtils.isEmpty(inputTextEmail_reg.getText())) {
         inputTextEmail_reg.setError("Campo obrigatorio");
         return false;
      }else if(TextUtils.isEmpty(inputTextPassword_reg.getText())) {
         inputTextPassword_reg.setError("Campo obrigatorio");
         return false;
      }else if(TextUtils.isEmpty(inputTextPasswordConfirm_reg.getText())) {
         inputTextPasswordConfirm_reg.setError("Campo obrigatorio");
         return false;
      }else if(!inputTextEmail_reg.getText()
              .toString()
              .trim()
              .matches(emailPattern)) {
         inputTextEmail_reg.setError("Email Invalido");
      }
      return true;
   }
   
}