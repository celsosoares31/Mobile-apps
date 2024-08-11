package com.example.grocerylist.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivityViewModel extends ViewModel {
   private MutableLiveData<List<String>> shoppingList;
   private static final List<String> shoppingListSample = new ArrayList<>();
   
   public LiveData<List<String>> getShoppingList(){
      if(shoppingList == null){
         shoppingList = new MutableLiveData<>();
         loadShoppingList();
      }
      return  shoppingList;
   }
   public void addItemToList(String value){
      shoppingListSample.add(value);
//      loadShoppingList();
   }
   private void loadShoppingList() {
      Handler myHandler = new Handler();
      
      myHandler.postDelayed(()->{
         
         shoppingListSample.add("Bread");
         shoppingListSample.add("Bananas");
         shoppingListSample.add("Peanut Butter");
         shoppingListSample.add("Eggs");
         shoppingListSample.add("Chicken breasts");
         
         long seed = System.nanoTime();
         Collections.shuffle(shoppingListSample, new Random(seed));
         
         shoppingList.setValue(shoppingListSample);
      }, 5000);
      
   }
   
 
}

