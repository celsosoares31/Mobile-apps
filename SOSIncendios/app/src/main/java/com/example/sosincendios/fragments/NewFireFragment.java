package com.example.sosincendios.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.sosincendios.Fire;
import com.example.sosincendios.R;
import com.example.sosincendios.activities.OnImageUploadListener;
import com.example.sosincendios.databinding.FragmentNewFireBinding;
import com.example.sosincendios.onBackPressedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewFireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewFireFragment extends Fragment implements onBackPressedListener {
   private FragmentNewFireBinding binding;
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   private static final int REQUEST_IMAGE_CAPTURE = 1;
   private static final int REQUEST_PERMISSIONS = 1000;
   private Uri photoUri, firePlaceImageUri, fireImage1Uri, fireImage2Uri, fireImage3Uri, fireImage4Uri;
   private List<Uri> imagesUri;
   private final String[] permissions = {Manifest.permission.CAMERA};
   private TextInputEditText fireStartDate, fireEndDate, fireAnimalsDied, fireAffectedArea, fireCity, fireDetais, fireLocality;
   private ImageView firePlaceImage, fireImage1, fireImage2, fireImage3, fireImage4;
   private RadioGroup radioGroup, radioGroup1, radioGroup2, radioGroup3;
   private MaterialButton firePlaceImageBtn, fireImage1Btn, fireImage2Btn, fireImage3Btn, fireImage4Btn, fireSubmitBtn;
   private boolean placeImageBtnClicked = false, fireImage1BtnClicked = false, fireImage2BtnClicked = false, fireImage3BtnClicked = false, fireImage4BtnClicked = false;
   private CircularProgressIndicator progressBar;
   private final String DBNAME = "fires";
   private int qtdImages = 0;
   private int uploadedImages = 0;
   int count = 0;
   private ScrollView scrool;
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public NewFireFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment NewFireFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static NewFireFragment newInstance(String param1, String param2) {
      NewFireFragment fragment = new NewFireFragment();
      Bundle args = new Bundle();
      args.putString(ARG_PARAM1, param1);
      args.putString(ARG_PARAM2, param2);
      fragment.setArguments(args);
      return fragment;
   }
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if(getArguments() != null) {
         mParam1 = getArguments().getString(ARG_PARAM1);
         mParam2 = getArguments().getString(ARG_PARAM2);
      }
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentNewFireBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      //Text edits
      fireStartDate = binding.fireStartDate;
      fireEndDate = binding.fireEndDate;
      fireAnimalsDied = binding.fireAnimalsDied;
      fireAffectedArea = binding.fireAffectedArea;
      fireDetais = binding.fireDetails;
      fireCity = binding.fireCity;
      fireLocality = binding.fireLocality;
      
      //Butoes
      firePlaceImageBtn = binding.firePlaceImageBtn;
      fireImage1Btn = binding.fireImage1Btn;
      fireImage2Btn = binding.fireImage2Btn;
      fireImage3Btn = binding.fireImage3Btn;
      fireImage4Btn = binding.fireImage4Btn;
      fireSubmitBtn = binding.fireSubmitBtn;
      
      //IMagens
      firePlaceImage = binding.firePlaceImage;
      fireImage1 = binding.fireImage1;
      fireImage2 = binding.fireImage2;
      fireImage3 = binding.fireImage3;
      fireImage4 = binding.fireImage4;
      
      //Radio button groups
      radioGroup = binding.radioGroup;
      radioGroup1 = binding.radioGroup1;
      radioGroup2 = binding.radioGroup2;
      radioGroup3 = binding.radioGroup3;
      
      //      progressBar
      progressBar = binding.progressBar;
      
      scrool = binding.scrool;
      
      if(savedInstanceState != null) {
         String uriString = savedInstanceState.getString("photoUri");
         if(uriString != null) {
            photoUri = Uri.parse(uriString);
         }
      }
      
      firePlaceImageBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(checkAndRequestPermissions()) {
               placeImageBtnClicked = true;
               btnClicked(placeImageBtnClicked);
               captureImage();
            }
         }
      });
      fireImage1Btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(checkAndRequestPermissions()) {
               fireImage1BtnClicked = true;
               btnClicked(fireImage1BtnClicked);
               captureImage();
            }
         }
      });
      fireImage2Btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(checkAndRequestPermissions()) {
               fireImage2BtnClicked = true;
               btnClicked(fireImage2BtnClicked);
               captureImage();
            }
         }
      });
      fireImage3Btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(checkAndRequestPermissions()) {
               fireImage3BtnClicked = true;
               btnClicked(fireImage3BtnClicked);
               captureImage();
            }
         }
      });
      
      fireImage4Btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(checkAndRequestPermissions()) {
               fireImage4BtnClicked = true;
               btnClicked(fireImage4BtnClicked);
               captureImage();
            }
         }
      });
      
      selectStartDate(fireStartDate);
      selectStartDate(fireEndDate);
      
      fireSubmitBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            submitFire();
         }
      });
   }
   
   private void submitFire() {
      progressBar.setVisibility(View.VISIBLE);
      String city = fireCity.getText()
              .toString()
              .trim();
      String place = fireLocality.getText()
              .toString()
              .trim();
      String areaClass = checkedRadioButton(radioGroup);
      String detectedBy = checkedRadioButton(radioGroup1);
      String dateNoticed = fireStartDate.getText()
              .toString()
              .trim();
      String dateFinished = fireEndDate.getText()
              .toString()
              .trim();
      String fightingStrategy = checkedRadioButton(radioGroup2);
      String reason = checkedRadioButton(radioGroup3);
      String affectedArea = fireAffectedArea.getText()
              .toString()
              .trim();
      String diedAnimal = fireAnimalsDied.getText()
              .toString()
              .trim();
      
      
      Uri firePlaceUri = firePlaceImageUri;
      Uri damage1 = fireImage1Uri;
      Uri damage2 = fireImage2Uri;
      Uri damage3 = fireImage3Uri;
      Uri damage4 = fireImage4Uri;
      
      String details = fireDetais.getText()
              .toString()
              .trim();
      String fireReportedBy;
      
      boolean allValid = isAllValid(city, place, dateNoticed, dateFinished);
      
      if(allValid) {
         Fire mFire = new Fire();
         FirebaseUser user = FirebaseAuth.getInstance()
                 .getCurrentUser();
         
         String usrName = user.getDisplayName();
         String usrNameFromEmail = user.getEmail()
                 .substring(0, user.getEmail()
                         .indexOf("@"));
         
         String validUserName = usrName != null ? usrName : usrNameFromEmail;
         
         mFire.setCity(city);
         mFire.setPlace(place);
         mFire.setAreaClass(areaClass);
         mFire.setDetectedBy(detectedBy);
         mFire.setDateNoticed(dateNoticed);
         mFire.setDateFinished(dateFinished);
         mFire.setFightingStrategy(fightingStrategy);
         mFire.setReason(reason);
         mFire.setAffectedArea(affectedArea);
         mFire.setDiedAnimal(diedAnimal);
         mFire.setDetails(details);
         mFire.setFireReportedBy(validUserName);
         
         mFire.setFireReportDay(getCurrentDate());
         
         imagesUri = new ArrayList<>();
         imagesUri.add(firePlaceUri);
         imagesUri.add(damage1);
         imagesUri.add(damage2);
         imagesUri.add(damage3);
         imagesUri.add(damage4);
         
         List<Uri> validImages = new ArrayList<>();
         
         for(Uri image : imagesUri) {
            if(image != null) {
               qtdImages++;
               validImages.add(image);
            }
         }
        
         saveToFirebase(mFire, validImages.size());
         for(Uri image : validImages) {
            if(image != null) {
               uploadImageToFirebase(image, mFire.getFire_id(), new OnImageUploadListener() {
                  @Override
                  public void onSuccess(String downloadUrl) {
                     FirebaseDatabase.getInstance()
                             .getReference(DBNAME)
                             .child(mFire.getFire_id()).child("images").child(String.valueOf(uploadedImages)).setValue(downloadUrl);
                     uploadedImages++;
                     Toast.makeText(requireActivity().getApplicationContext(), "Upload " + uploadedImages + " de " + validImages.size() + " efectuado com " + "sucesso", Toast.LENGTH_LONG)
                             .show();
                     if(uploadedImages == validImages.size()) {
                        progressBar.setVisibility(View.GONE);
                        replaceFragment(new HomeFragment(), false);
                     }
                  }
                  
                  @Override
                  public void onFailure(Exception exception) {
                     // Handle the failure
                     progressBar.setVisibility(View.GONE);
                     Toast.makeText(requireActivity().getApplicationContext(), "O Upload falhou " + exception.getMessage(), Toast.LENGTH_LONG)
                             .show();
                  }
               });
               
            }
            count++;
         }
         qtdImages = 0;
         uploadedImages = 0;
      }else {
         progressBar.setVisibility(View.GONE);
      }
   }
   
   private Uri compressAndUploadImage(Uri imageUri) {
      Uri image = Uri.parse("");
      try {
         File imageFile = new File(imageUri.getPath());
         File compressedImageFile = new Compressor(requireContext())
                 .setMaxWidth(640)
                 .setMaxHeight(480)
                 .setQuality(75)
                 .compressToFile(imageFile);
         
          image = Uri.fromFile(compressedImageFile);
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      return image;
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
   
   private void saveToFirebase(Fire mFire, int imagesTot) {
      FirebaseDatabase.getInstance()
              .getReference(DBNAME)
              .child(mFire.getFire_id())
              .setValue(mFire)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void unused) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Registo efectuado com " + "sucesso", Toast.LENGTH_SHORT)
                            .show();
                    if(imagesTot != 0) {
                       Toast.makeText(requireActivity().getApplicationContext(), "Aguarde enquanto fazemos o upload das images", Toast.LENGTH_SHORT)
                               .show();
                    }
                 }
              })
              .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity().getApplicationContext(), "O correu um erro ao " + "fazer o registo", Toast.LENGTH_SHORT)
                            .show();
                 }
              });
   }
   
   private String getCurrentDate() {
      Calendar calendar = Calendar.getInstance();
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH); // Month value is zero-based (0 - January, 11 - December)
      int day = calendar.get(Calendar.DAY_OF_MONTH);
      
      String currentDate = year + "/" + (month + 1) + "/" + day;
      return currentDate;
   }
   
   private boolean isAllValid(String city, String place, String dateNoticed, String dateFinished) {
      if(city.isEmpty()) {
         Toast.makeText(requireContext(), "O campo cidade é obrigatorio", Toast.LENGTH_SHORT)
                 .show();
         fireCity.setError("Campo obrigatório");
         scrool.scrollTo(0, fireCity.getTop());
         
         return false;
      }else if(place.isEmpty()) {
         Toast.makeText(requireContext(), "O campo localidade é obrigatorio", Toast.LENGTH_SHORT)
                 .show();
         fireLocality.setError("Campo obrigatório");
         scrool.scrollTo(0, fireLocality.getTop());
         return false;
      }else if(dateNoticed.isEmpty()) {
         Toast.makeText(requireContext(), "O campo data de inicio é obrigatorio", Toast.LENGTH_SHORT)
                 .show();
         fireStartDate.setError("Campo obrigatório");
         scrool.scrollTo(0, fireStartDate.getTop());
         return false;
      }else if(!dateFinished.isEmpty()) {
         if(new Date(dateNoticed).after(new Date(dateFinished))) {
            Toast.makeText(requireContext(), "A data de Inicio nao pode ser posterir a data" + " " + "de fim", Toast.LENGTH_SHORT)
                    .show();
            return false;
         }
      }
      return true;
   }
   
   private void uploadImageToFirebase(Uri imageUri, String fireId,
                                      OnImageUploadListener listener) {
//      Uri imageUri = compressAndUploadImage(mImageUri);
      
      progressBar.setVisibility(View.VISIBLE);
      FirebaseUser user = FirebaseAuth.getInstance()
              .getCurrentUser();
      FirebaseStorage storage = FirebaseStorage.getInstance();
      StorageReference storageRef = storage.getReference();
      StorageReference imagesRef = storageRef.child("images/" + fireId + "/" + imageUri.getLastPathSegment());
      UploadTask uploadTask = imagesRef.putFile(imageUri);
      
      uploadTask.addOnFailureListener(exception -> {
                 // Handle unsuccessful uploads
                 listener.onFailure(exception);
              })
              .addOnSuccessListener(taskSnapshot -> {
                 // Handle successful uploads
                 imagesRef.getDownloadUrl()
                         .addOnSuccessListener(uri -> {
                            listener.onSuccess(uri.toString());
                         })
                         .addOnFailureListener(listener::onFailure);
              });
      progressBar.setVisibility(View.VISIBLE);
   }
   
   private String checkedRadioButton(RadioGroup radioGroup) {
      int id = radioGroup.getCheckedRadioButtonId();
      RadioButton rButton = requireView().findViewById(id);
      
      String text = rButton.getText()
              .toString();
      return text;
   }
   
   private void btnClicked(boolean clickedBtn) {
      if(clickedBtn == placeImageBtnClicked) {
         fireImage1BtnClicked = false;
         fireImage2BtnClicked = false;
         fireImage3BtnClicked = false;
         fireImage4BtnClicked = false;
         
      }else if(clickedBtn == fireImage1BtnClicked) {
         placeImageBtnClicked = false;
         fireImage2BtnClicked = false;
         fireImage3BtnClicked = false;
         fireImage4BtnClicked = false;
      }else if(clickedBtn == fireImage2BtnClicked) {
         placeImageBtnClicked = false;
         fireImage1BtnClicked = false;
         fireImage3BtnClicked = false;
         fireImage4BtnClicked = false;
      }else if(clickedBtn == fireImage3BtnClicked) {
         placeImageBtnClicked = false;
         fireImage1BtnClicked = false;
         fireImage2BtnClicked = false;
         fireImage4BtnClicked = false;
      }else if(clickedBtn == fireImage4BtnClicked) {
         placeImageBtnClicked = false;
         fireImage1BtnClicked = false;
         fireImage2BtnClicked = false;
         fireImage3BtnClicked = false;
      }
      
   }
   
   private boolean checkAndRequestPermissions() {
      boolean allGranted = true;
      for(String permission : permissions) {
         if(ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            allGranted = false;
            break;
         }
      }
      if(!allGranted) {
         ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_PERMISSIONS);
      }
      return allGranted;
   }
   
   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if(requestCode == REQUEST_PERMISSIONS) {
         boolean allGranted = true;
         for(int result : grantResults) {
            if(result != PackageManager.PERMISSION_GRANTED) {
               allGranted = false;
               break;
            }
         }
         if(allGranted) {
            captureImage();
         }else {
            Toast.makeText(requireContext(), "Nao possui permissao para usar a " + "camera", Toast.LENGTH_SHORT)
                    .show();
         }
      }
   }
   
   @SuppressLint("QueryPermissionsNeeded")
   private void captureImage() {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if(takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
         File photoFile = null;
         try {
            photoFile = createImageFile();
         }catch(IOException e) {
            Toast.makeText(requireContext(), "Error creating file " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
         }
         if(photoFile != null) {
            photoUri = FileProvider.getUriForFile(requireContext().getApplicationContext(), "com" + ".example" + ".sosincendios.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
         }
      }
   }
   
   private File createImageFile() throws IOException {
      // Create an image file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      String imageFileName = timeStamp + "_";
      File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      File image = File.createTempFile(imageFileName, ".jpg", storageDir);
      return image;
   }
   
   @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
         if(photoUri != null) {
            if(placeImageBtnClicked) {
               binding.firePlaceImage.setImageURI(photoUri);
               placeImageBtnClicked = false;
               firePlaceImageUri = photoUri;
            }else if(fireImage1BtnClicked) {
               binding.fireImage1.setImageURI(photoUri);
               fireImage1BtnClicked = false;
               fireImage1Uri = photoUri;
            }else if(fireImage2BtnClicked) {
               binding.fireImage2.setImageURI(photoUri);
               fireImage2BtnClicked = false;
               fireImage2Uri = photoUri;
            }else if(fireImage3BtnClicked) {
               binding.fireImage3.setImageURI(photoUri);
               fireImage3BtnClicked = false;
               fireImage3Uri = photoUri;
            }else if(fireImage4BtnClicked) {
               binding.fireImage4.setImageURI(photoUri);
               fireImage4BtnClicked = false;
               fireImage4Uri = photoUri;
            }
         }else {
            Toast.makeText(requireContext(), "Falha ao capturar a imagem", Toast.LENGTH_SHORT)
                    .show();
         }
         //                    uploadImageToFirebase(photoUri);
         
      }
   }
   
   @SuppressLint("ClickableViewAccessibility")
   private void selectStartDate(TextInputEditText fireDate) {
      fireDate.setOnTouchListener((v, event) -> {
         int inType = fireDate.getInputType();
         fireDate.setInputType(InputType.TYPE_NULL);
         fireDate.onTouchEvent(event);
         fireDate.setInputType(inType);
         return true;
      });
      
      fireDate.setOnClickListener(v -> {
         CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())
                 .build();
         
         MaterialDatePicker<Long> selectedDate = MaterialDatePicker.Builder.datePicker()
                 .setTitleText("Selecione a data")
                 .setCalendarConstraints(constraintsBuilder)
                 .build();
         
         selectedDate.show(requireActivity().getSupportFragmentManager(), "date picker");
         selectedDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            String formattedDate = format.format(calendar.getTime());
            fireDate.setText(formattedDate);
         });
         
      });
   }
   
   @Override
   public boolean onBackPressed() {
      return false;
   }
}