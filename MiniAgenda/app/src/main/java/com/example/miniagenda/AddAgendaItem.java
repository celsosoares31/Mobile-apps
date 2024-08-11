package com.example.miniagenda;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddAgendaItem extends AppCompatActivity {
   private Button savePerson;
   private ImageView profilePhoto;
   private  String selectedImage = "";
   private final int SELECT_PICTURE = 200;
   private TextInputEditText name, email, phone, address;
   private PersonDAO dao;
   private Person person = null;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_add_agenda_item);
      
      dao = new PersonDAO(getApplicationContext());
      savePerson = findViewById(R.id.btnSave);
      name = findViewById(R.id.txtNome);
      email = findViewById(R.id.txtEmail);
      phone = findViewById(R.id.txtTelefone);
      address = findViewById(R.id.txtMorada);
      Intent intent = getIntent();
      
      if(intent.hasExtra("person")){
         person = (Person) intent.getSerializableExtra("person");
         name.setText(person.getName());
         email.setText(person.getEmail());
         phone.setText(person.getPhone());
         address.setText(person.getAddress());
         savePerson.setText("Actualizar");
      }
      
      name.setBackground(new ColorDrawable(getColor(R.color.lightBlue)));
      email.setBackground(new ColorDrawable(getColor(R.color.lightBlue)));
      phone.setBackground(new ColorDrawable(getColor(R.color.lightBlue)));
      address.setBackground(new ColorDrawable(getColor(R.color.lightBlue)));
      
      savePerson.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(person == null){
               addPerson();
            }else{
               boolean allFilled = isALlFilled();
               if(allFilled){
                  boolean emailValidation = isValidEmail(Objects.requireNonNull(email.getText())
                          .toString());
                  boolean phoneValidation = isValidPhoneNumber(Objects.requireNonNull(phone.getText())
                          .toString());
                  if(emailValidation && phoneValidation){
                     person.setName(name.getText().toString());
                     person.setEmail(email.getText().toString());
                     person.setAddress(address.getText().toString());
                     person.setPhone(phone.getText().toString());
                     
                     dao.updatePerson(person);
                     showSnack(R.string.person_update_status_success, null);
                     goToMain();
                  }
               }
            }
            
         }
      });
   }
   private void goToMain(){
      finish();
      Intent intent = new Intent(AddAgendaItem.this, MainActivity.class);
      startActivity(intent);
   }
   private void addPerson() {
      boolean allFilled = isALlFilled();
      if(allFilled){
         boolean emailValidation = isValidEmail(Objects.requireNonNull(email.getText())
                 .toString());
         boolean phoneValidation = isValidPhoneNumber(Objects.requireNonNull(phone.getText())
                 .toString());
         if(emailValidation && phoneValidation){
            Person person = new Person();
            person.setName(Objects.requireNonNull(name.getText())
                    .toString().trim());
            person.setEmail(email.getText().toString().trim());
            person.setAddress(Objects.requireNonNull(address.getText())
                    .toString().trim());
            person.setPhone(Objects.requireNonNull(phone.getText())
                    .toString().trim());
            person.setProfilePhoto(selectedImage);
            long insertedId = dao.insertPerson(person);
            if(insertedId == -1){
               showSnack(R.string.person_status_error, null);
            }else {
               showSnack(R.string.person_status_success, null);
               goToMain();
            }
         }
      }
   }
   private boolean isValidEmail(String email){
      String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
      if(!email.matches(emailPattern)){
         showSnack(R.string.invalid_email_error, this.email);
         return false;
      }
      return true;
   }
   private boolean isValidPhoneNumber(String phoneNumber){
      int length = 9;
      if(phoneNumber.length() < length){
         showSnack(R.string.invalid_phone_error, this.phone);
         return false;
      }
      return true;
   }
   private void showSnack(int rsId, @Nullable TextInputEditText edtName){
      Snackbar.make(savePerson,rsId,
              Snackbar.LENGTH_SHORT).setAction("OK",
              new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    if(edtName != null){
//                       edtName.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                    }
                 }
              }).show();
      if(edtName != null){
         edtName.setBackgroundColor(getResources().getColor(R.color.red));
      }
      
   }
   private boolean isALlFilled(){
      if(Objects.requireNonNull(name.getText())
              .toString()
              .isEmpty()){
        showSnack(R.string.empty_name_error, name);
         return false;
      }else if(Objects.requireNonNull(address.getText())
              .toString()
              .isEmpty()) {
         showSnack(R.string.empty_address_error, address);
         return false;
      }else if(Objects.requireNonNull(phone.getText())
              .toString()
              .isEmpty()) {
         showSnack(R.string.empty_phone_error, phone);
         return false;
      }else if(Objects.requireNonNull(email.getText())
              .toString()
              .isEmpty()) {
         showSnack(R.string.empty_email_error, email);
         return false;
      }
      return true;
   }
   @Override
   public void onBackPressed() {
      super.onBackPressed();
      Intent intent = new Intent(AddAgendaItem.this, MainActivity.class);
      startActivity(intent);
      AddAgendaItem.this.finish();
   }
}