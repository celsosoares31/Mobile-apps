package com.example.control_gastos.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.control_gastos.DatabaseHelper;
import com.example.control_gastos.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpenseFragment extends Fragment {
   private static final int REQUEST_IMAGE_CAPTURE = 1;
   
   private EditText itemEditText, locationEditText, amountEditText;
   private Button dateButton, timeButton, photoButton, saveButton;
   private ImageView photoImageView;
   private String photoPath;
   private String selectedDate, selectedTime;
   private DatabaseHelper databaseHelper;
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public AddExpenseFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment AddExpenseFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static AddExpenseFragment newInstance(String param1, String param2) {
      AddExpenseFragment fragment = new AddExpenseFragment();
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
      // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_add_expense, container,
              false);
      return  view;
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      
      itemEditText = view.findViewById(R.id.itemEditText);
      locationEditText = view.findViewById(R.id.locationEditText);
      amountEditText = view.findViewById(R.id.amountEditText);
      dateButton = view.findViewById(R.id.dateButton);
      timeButton = view.findViewById(R.id.timeButton);
      photoButton = view.findViewById(R.id.photoButton);
      saveButton = view.findViewById(R.id.saveButton);
      photoImageView = view.findViewById(R.id.photoImageView);
      
      databaseHelper = new DatabaseHelper(requireContext());
      
      dateButton.setOnClickListener(v -> showDatePickerDialog());
      timeButton.setOnClickListener(v -> showTimePickerDialog());
      photoButton.setOnClickListener(v -> openCamera());
      saveButton.setOnClickListener(v -> saveExpense());
   }
   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data); // Chamar super.onActivityResult
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
         Bundle extras = data.getExtras();
         if (extras != null) {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
               photoImageView.setImageBitmap(imageBitmap);
               photoImageView.setVisibility(View.VISIBLE);
               
               // Save the photo path for later use
               Uri tempUri = getImageUri(requireContext(), imageBitmap);
               photoPath = getRealPathFromURI(tempUri);
            }
         }
      }
   }
   
   private Uri getImageUri(Context context, Bitmap image) {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
      String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
      return Uri.parse(path);
   }
   
   private String getRealPathFromURI(Uri uri) {
      Cursor cursor = requireActivity().getContentResolver().query(uri, null,
              null, null, null);
      String path;
      if (cursor != null && cursor.moveToFirst()) {
         int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
         path = cursor.getString(idx);
         cursor.close();
      } else {
         path = uri.getPath();
      }
      return path;
   }
   
   private void saveExpense() {
      String item = itemEditText.getText().toString();
      String location = locationEditText.getText().toString();
      String amountString = amountEditText.getText().toString();
      
      if (validateExpense(item, location, amountString)) {
         double amount = Double.parseDouble(amountString); // Converter para double
         
         boolean isInserted = databaseHelper.addExpense(selectedDate, selectedTime, location, item, amount, photoPath);
         if (isInserted) {
            Toast.makeText(requireContext(), "Expense saved successfully",
                    Toast.LENGTH_SHORT).show();
//            finish();
            gotoHome();
         } else {
            Toast.makeText(requireContext(), "Failed to save expense", Toast.LENGTH_SHORT).show();
         }
      } else {
         Toast.makeText(requireContext(), "Please fill in all fields and take a photo",
                 Toast.LENGTH_SHORT).show();
      }
   }
   
   private void gotoHome() {
      HomeFragment homeFragment = new HomeFragment();
      replaceFragment(homeFragment, true);
      BottomNavigationView bottomNavigationView =
              requireActivity().findViewById(R.id.bottom_navigation);
      
      Menu menu = bottomNavigationView.getMenu();
      MenuItem menuItem = menu.getItem(2);
      menuItem.setChecked(true);
   }
   
   private void replaceFragment(Fragment fragment, boolean b) {
      FragmentManager fragmentManager =
              requireActivity().getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.frameLayout, fragment);
      if(b) {
         fragmentTransaction.addToBackStack(null);
      }
      fragmentTransaction.commit();
   }
   
   private boolean validateExpense(String item, String location, String amount) {
      return !item.isEmpty() && !location.isEmpty() && !amount.isEmpty() && photoPath != null;
   }
   
   private void showTimePickerDialog() {
      TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view,
                                                                   hourOfDay, minute) -> {
         selectedTime = formatTime(hourOfDay, minute);
         timeButton.setText(selectedTime);
      }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
      timePickerDialog.show();
   }
   
   private String formatTime(int hourOfDay, int minute) {
      return String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
   }
   
   private void openCamera() {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
         startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
      }
   }
   private void showDatePickerDialog() {
      DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year
              , month, dayOfMonth) -> {
         selectedDate = formatDate(year, month, dayOfMonth);
         dateButton.setText(selectedDate);
      }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
      datePickerDialog.show();
   }
   
   private String formatDate(int year, int month, int dayOfMonth) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(year, month, dayOfMonth);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      return dateFormat.format(calendar.getTime());
   }
}