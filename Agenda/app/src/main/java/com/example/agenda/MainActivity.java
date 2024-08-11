package com.example.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private ListView personListView;
   private PersonDAO dao;
   private List<Person> personList;
   private PersonAdapter adapter;
   private FloatingActionButton fbtAddItem;
   private List<Person> filteredPersonList = new ArrayList<>();
   CoordinatorLayout searchContainer;
   com.google.android.material.search.SearchView searchView;
   SearchBar searchBar;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
         if(ActivityCompat.checkSelfPermission(this,
                 Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
         };
      }
      
      searchContainer = findViewById(R.id.searchContainer);
      searchView = findViewById(R.id.searchView);
      searchBar = findViewById(R.id.search_bar);
      fbtAddItem = findViewById(R.id.fbtAddItem);
      personListView = findViewById(R.id.personListView);
      
      personList = new ArrayList<>();
      reference = FirebaseDatabase.getInstance().getReference("persons");
      adapter = new PersonAdapter(MainActivity.this, filteredPersonList);
      personListView.setAdapter(adapter);
      reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            personList.clear();
            snapshot.getChildren().forEach(personSnapshot ->{
               Person person = personSnapshot.getValue(Person.class);
               personList.add(person);
            } );
            if(!personList.isEmpty()){
               filteredPersonList.clear();
               filteredPersonList.addAll(personList);
               adapter.notifyDataSetChanged();
            }else {
               filteredPersonList = null;
            }
         }
         @Override
         public void onCancelled(@NonNull DatabaseError error) {
         
         }
      });
      if(filteredPersonList == null){
         searchView.setVisibility(View.INVISIBLE);
         searchBar.setVisibility(View.INVISIBLE);
      }else{
         searchView.setVisibility(View.VISIBLE);
         searchBar.setVisibility(View.VISIBLE);
      }
      searchView.getEditText().setOnEditorActionListener(
              (v, actionId, event) -> {
                 searchBar.setText(searchView.getText());
                 searchView.hide();
                 return false;
              });
      searchView.getEditText().addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }
         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchBar(s);
         }
         
         @Override
         public void afterTextChanged(Editable s) {
         
         }
      });
      
      registerForContextMenu(personListView);
      
      fbtAddItem.setOnClickListener(v -> {
         Intent it = new Intent(MainActivity.this,AddAgendaItem.class);
         startActivity(it);
      });
   }
   public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
      super.onCreateContextMenu(menu, view,  menuInfo);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.context_menu, menu);
   }
   private void searchBar(CharSequence s) {
      if(filteredPersonList != null){
         filteredPersonList.clear();
         for(Person person : personList){
            if(person.getName().toLowerCase().contains(s.toString().toLowerCase())){
               filteredPersonList.add(person);
            }
            personListView.invalidateViews();
         }
      }
   }
   public void edit(MenuItem item) {
      AdapterView.AdapterContextMenuInfo menuInfo =
              (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      if(menuInfo != null) {
         Person personToUpdate = filteredPersonList.get(menuInfo.position);
         Intent intent = new Intent(MainActivity.this, AddAgendaItem.class);
         intent.putExtra("person", personToUpdate);
         startActivity(intent);
      }
   }
   public void delete(MenuItem item) {
      AdapterView.AdapterContextMenuInfo menuInfo =
              (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      if(menuInfo != null){
         Person personToDelete = filteredPersonList.get(menuInfo.position);
         
         new MaterialAlertDialogBuilder(this,
                 com.google.android.material.R.style.ThemeOverlay_AppCompat_Dark)
                 .setTitle("Atencao")
                 .setBackground(new ColorDrawable(getColor(R.color.blue)))
                 .setMessage("Tem certeza que deseja apagar este registo?")
                 .setNegativeButton("Nao", null)
                 .setPositiveButton("Sim", (dialog, which) -> {
                    filteredPersonList.remove(personToDelete);
                    personList.remove(personToDelete);
                    reference.child(personToDelete.getPerson_id()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                          Toast.makeText(MainActivity.this,"Registo apagado com " +
                                          "sucesso",
                                  Toast.LENGTH_SHORT).show();
                          personListView.invalidateViews();
                       }
                    }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                          Toast.makeText(MainActivity.this,"Falha ao deletar " +
                                          "o registo.",
                                  Toast.LENGTH_SHORT).show();
                       }
                    });
                    
                 }).show();
         
      }
      
   }
}