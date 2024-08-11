package com.example.miniagenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private ListView personListView;
   private PersonDAO dao;
   private List<Person> personList;
   private final List<Person> filteredPersonList = new ArrayList<>();
   CoordinatorLayout searchContainer;
   com.google.android.material.search.SearchView searchView;
   SearchBar searchBar;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      dao = new PersonDAO(this);
      personList = dao.getAllPersons();
      filteredPersonList.addAll(personList);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      searchContainer = findViewById(R.id.searchContainer);
      searchView = findViewById(R.id.searchView);
      searchBar = findViewById(R.id.search_bar);
      searchView
              .getEditText()
              .setOnEditorActionListener(
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
      
//      if(personList.isEmpty()){
//         searchContainer.setVisibility(View.INVISIBLE);
//      }else{
//         searchContainer.setVisibility(View.VISIBLE);
//      }
      FloatingActionButton fbtAddItem = findViewById(R.id.fbtAddItem);
      personListView = findViewById(R.id.personListView);
      
      PersonAdapter adapter = new PersonAdapter(this, filteredPersonList);
      personListView.setAdapter(adapter);
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
      filteredPersonList.clear();
      for(Person person : personList){
         if(person.getName().toLowerCase().contains(s.toString().toLowerCase())){
            filteredPersonList.add(person);
         }
         personListView.invalidateViews();
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
                    dao.deletePerson(personToDelete);
                    personListView.invalidateViews();
                 }).show();
         
      }
      
   }
}