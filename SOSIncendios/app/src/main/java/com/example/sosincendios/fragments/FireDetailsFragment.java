package com.example.sosincendios.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sosincendios.Fire;
import com.example.sosincendios.ImageCarouselAdapter;
import com.example.sosincendios.R;
import com.example.sosincendios.databinding.FragmentFireDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FireDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FireDetailsFragment extends Fragment {
   FragmentFireDetailsBinding binding;
   private static final String ARG_FIRE = "fire";
   private Fire mFire;
   private RecyclerView recyclerView;
   private ImageCarouselAdapter imageCarouselAdapter;
   private MaterialButton editFireBtn, deleteFireBtn;
   private LinearLayout actionBtnLayout;
   private FirebaseUser activeUser;
   private DatabaseReference reference;
   private final String DBNAME = "fires";
   private FirebaseStorage storage;
   private LinearProgressIndicator linerProgressBar;
   
   
   public FireDetailsFragment() {
      // Required empty public constructor
   }
   
   /**
    * @param mFire
    * @return
    */
   // TODO: Rename and change types and number of parameters
   public static FireDetailsFragment newInstance(Fire mFire) {
      FireDetailsFragment fragment = new FireDetailsFragment();
      Bundle args = new Bundle();
      args.putParcelable(ARG_FIRE,  mFire);
      fragment.setArguments(args);
      return fragment;
   }
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if(getArguments() != null) {
         mFire = (Fire) getArguments().getParcelable(ARG_FIRE);
      }
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      reference = FirebaseDatabase.getInstance()
              .getReference(DBNAME);
      
      TextView fireDate = binding.fireDate;
      TextView reportedBy = binding.reportedBy;
      ImageView imageView = binding.fireImage;
      TextView fireCity = binding.fireCity;
      TextView firePlace = binding.firePlace;
      TextView affectedArea = binding.affecteArea;
      TextView diedAnimals = binding.diedAnimals;
      TextView fireEndDate = binding.fireEndDate;
      TextView strategy = binding.strategy;
      TextView areaClass = binding.areaClass;
      TextView reason = binding.reason;
      TextView detectedBy = binding.detectedBy;
      linerProgressBar = binding.linerProgressBar;
      
      deleteFireBtn = binding.deleFireBtn;
      actionBtnLayout = binding.actionBtnLayout;
      
      if(!currentUserIsOwner()) {
         actionBtnLayout.setVisibility(View.GONE);
      }else {
         actionBtnLayout.setVisibility(View.VISIBLE);
      }
      
      deleteFireBtn.setOnClickListener(v -> {
         final String fireId = mFire.getFire_id();
         new MaterialAlertDialogBuilder(requireContext()).setTitle("Atencao")
                 .setMessage("Tem certeza que deseja apagar esta " + "ocorrencia?")
                 .setBackground(new ColorDrawable(getContext().getColor(R.color.darkGreen)))
                 .setPositiveButton(R.string.positiveOption, (dialog, which) -> {
                    linerProgressBar.setVisibility(View.VISIBLE);
                    reference.child(mFire.getFire_id())
                            .removeValue()
                            .addOnSuccessListener(unused -> {
                               Toast.makeText(requireContext(), "Registo " + "removido com sucesso!", Toast.LENGTH_SHORT)
                                       .show();
                               String path = "images/" + fireId + "/";
                               deleteAllFilesInFolder(path);
                               
                            })
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Falha ao " + "remover o registo", Toast.LENGTH_SHORT)
                                    .show());
                 })
                 .setNegativeButton(R.string.negativeOption, (dialog, which) -> {
                 
                 })
                 .show();
      });
      
      TextView details = binding.details;
      recyclerView = binding.carouselRecyclerView;
      recyclerView.setLayoutManager(new CarouselLayoutManager(new HeroCarouselStrategy()));
      MaterialCardView cardView = binding.CardView;
      
      reason.setText(mFire.getReason());
      areaClass.setText(mFire.getAreaClass());
      strategy.setText(mFire.getFightingStrategy());
      detectedBy.setText(mFire.getDetectedBy());
      
      reportedBy.setText(mFire.getFireReportedBy());
      fireDate.setText(mFire.getDateNoticed());
      fireCity.setText(mFire.getCity());
      firePlace.setText(mFire.getPlace());
      affectedArea.setText(mFire.getAffectedArea() != "" ? mFire.getAffectedArea() : "N/A");
      fireEndDate.setText(mFire.getDateFinished() != "" ? mFire.getDateFinished() : "N/A");
      details.setText(mFire.getDetails() != "" ? mFire.getDetails() : "Sem " + "comentarios......");
      diedAnimals.setText(mFire.getDiedAnimal() != "" ? mFire.getDiedAnimal() : "N/A");
      
      List<String> images = mFire.getImages();
      
      ImageCarouselAdapter adapter = new ImageCarouselAdapter(requireContext(), images);
      recyclerView.setAdapter(adapter);
      if(images == null) {
         cardView.setVisibility(View.GONE);
      }else {
         cardView.setVisibility(View.VISIBLE);
         CarouselSnapHelper snapHelper = new CarouselSnapHelper();
         snapHelper.attachToRecyclerView(recyclerView);
      }
      
      if(images != null) {
         Glide.with(requireContext())
                 .asBitmap()
                 .load(images.get(0))
                 .into(imageView);
      }
   }
   
   private void deleteAllFilesInFolder(String folderPath) {
      StorageReference storageRef = FirebaseStorage.getInstance()
              .getReference()
              .child(folderPath);
      
      storageRef.listAll()
              .addOnSuccessListener(listResult -> {
                 for(StorageReference fileRef : listResult.getItems()) {
                    // Delete each file
                    fileRef.delete()
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(exception -> {
                            
                            });
                 }
                 Toast.makeText(requireContext(), "Imagens " + "apagadas " + "com sucesso", Toast.LENGTH_SHORT)
                         .show();
                 linerProgressBar.setVisibility(View.GONE);
                 replaceFragment(new HomeFragment(), false);
              })
              .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                    // An error occurred while listing files
                    Log.d("Delete", "Failed to list files: " + e.getMessage());
                 }
              });
   }
   
   private void replaceFragment(Fragment fireDetailsFragment, boolean b) {
      FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.frameLayout, fireDetailsFragment);
      boolean addToBackStack;
      if(b) {
         fragmentTransaction.addToBackStack(null);
      }
      fragmentTransaction.commit();
   }
   
   private boolean currentUserIsOwner() {
      activeUser = FirebaseAuth.getInstance()
              .getCurrentUser();
      
      String usrName = activeUser.getDisplayName();
      String usrNameFromEmail = activeUser.getEmail()
              .substring(0, activeUser.getEmail()
                      .indexOf("@"));
      
      if(Objects.equals(mFire.getFireReportedBy(), usrName) || Objects.equals(mFire.getFireReportedBy(), usrNameFromEmail)) {
         Log.d("currentUserIsOwner", "TRUE");
         return true;
      }
      Log.d("currentUserIsOwner", "FALSE");
      Log.d("currentUserIsOwner", "usrName - " + usrName);
      Log.d("currentUserIsOwner", "userNameFromEmail - " + usrNameFromEmail);
      Log.d("currentUserIsOwner", "from the object - " + mFire.getFireReportedBy());
      return false;
      
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      
      binding = FragmentFireDetailsBinding.inflate(inflater, container, false);
      
      return binding.getRoot();
   }
}