package com.example.sosincendios;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FireViewModel extends ViewModel {
   private final MutableLiveData<ArrayList<Fire>> fireListLiveData = new MutableLiveData<>();
   
   public void setFireList(ArrayList<Fire> fireList) {
      fireListLiveData.setValue(fireList);
   }
   
   public LiveData<ArrayList<Fire>> getFireList() {
      return fireListLiveData;
   }
}
