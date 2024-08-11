package com.example.miniagenda;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddAgendaItem extends AppCompatActivity {
   private Button savePerson;
   private ImageView profilePhoto;
   private  String selectedImage = "";
   private final int SELECT_PICTURE = 200;
   private final String DBNAME = "persons";
   private TextInputEditText name, email, phone, address;
   private Person person = null;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_add_agenda_item);
      
      ;
      
//      person = new Person();
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
                     Person p = new Person();
                     p.setName(name.getText().toString());
                     p.setEmail(email.getText().toString());
                     p.setAddress(address.getText().toString());
                     p.setPhone(phone.getText().toString());
                     p.setPerson_id(person.getPerson_id());
                     
                     FirebaseDatabase.getInstance().getReference(DBNAME).child(person.getPerson_id()).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                           showSnack(R.string.person_update_status_success, null);
                           Intent intent = new Intent(AddAgendaItem.this,
                                   MainActivity.class);
                           startActivity(intent);
                           finish();
                        }
                     }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           showSnack(R.string.person_update_status_error, null);
                        }
                     });
                  }
               }
            }
            
         }
      });
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
            
            FirebaseDatabase.getInstance().getReference(DBNAME).child(person.getPerson_id()).setValue(person).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                  Toast.makeText(getApplicationContext(), "Registo efectuado com sucesso",
                                  Toast.LENGTH_SHORT)
                          .show();
                  Intent intent = new Intent(AddAgendaItem.this,
                          MainActivity.class);
                  startActivity(intent);
                  finish();
               }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(), "Falha ao registar " +
                                          "os dados ao banco de dados",
                                  Toast.LENGTH_SHORT)
                          .show();
               }
            });
            
           
          
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
      String prefix = phoneNumber.substring(0, 2);
      String[] prefixArray = {"82", "83", "84", "85", "86", "87"};
      List<String> prefixList = Arrays.asList(prefixArray);
      
      System.out.println(prefixList.contains(prefix));
      if(phoneNumber.length() < length || !prefixList.contains(prefix)){
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
         name.setError("Campo obrigatorio");
         return false;
      }else if(Objects.requireNonNull(address.getText())
              .toString()
              .isEmpty()) {
         address.setError("Campo obrigatorio");
         return false;
      }else if(Objects.requireNonNull(phone.getText())
              .toString()
              .isEmpty()) {
         phone.setError("Campo obrigatorio");
         return false;
      }else if(Objects.requireNonNull(email.getText())
              .toString()
              .isEmpty()) {
         email.setError("Campo obrigatorio");
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