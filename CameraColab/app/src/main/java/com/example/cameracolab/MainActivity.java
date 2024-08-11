package com.example.cameracolab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   private static final int REQUEST_IMAGE_CAPTURE = 1;
   private Uri photoUri;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
   }
   
   public void captureImage(View view) {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
         File photoFile = null;
         try {
            photoFile = createImageFile();
         } catch (IOException ex) {
            // Error occurred while creating the File
         }
         if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
         }
      }
   }
   
   private File createImageFile() throws IOException {
      // Create an image file name
      String timeStamp =
              new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      String imageFileName = "JPEG_" + timeStamp + "_";
      File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      File image = File.createTempFile(
              imageFileName,  /* prefix */
              ".jpg",         /* suffix */
              storageDir      /* directory */
      );
      return image;
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
         uploadImageToFirebase(photoUri);
      }
   }
   
   private void uploadImageToFirebase(Uri imageUri) {
      FirebaseStorage storage = FirebaseStorage.getInstance();
      StorageReference storageRef = storage.getReference();
      StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());
      UploadTask uploadTask = imagesRef.putFile(imageUri);
      
      uploadTask.addOnFailureListener(exception -> {
         // Handle unsuccessful uploads
         Toast.makeText(MainActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
      }).addOnSuccessListener(taskSnapshot -> {
         // Handle successful uploads
         Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
      });
   }
}