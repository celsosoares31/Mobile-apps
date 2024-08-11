package com.example.gestaoprojecto.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gestaoprojecto.DatabaseHelper;
import com.example.gestaoprojecto.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends Fragment {
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   private EditText etProjectName;
   private EditText etProjectType;
   private EditText etDescription;
   private EditText etStartDate;
   private EditText etEndDate;
   private Spinner spinnerStatus;
   private Button btnAddProject;
   
   private DatabaseHelper dbHelper;
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public AddProjectFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment DashboardFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static AddProjectFragment newInstance(String param1, String param2) {
      AddProjectFragment fragment = new AddProjectFragment();
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
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      dbHelper = new DatabaseHelper(requireContext());
      
      etProjectName = view.findViewById(R.id.etProjectName);
      etProjectType = view.findViewById(R.id.etProjectType);
      etDescription = view.findViewById(R.id.etDescription);
      etStartDate = view.findViewById(R.id.edtStartDate);
      etEndDate = view.findViewById(R.id.edtEndDate);
      spinnerStatus = view.findViewById(R.id.spinnerStatus);
      btnAddProject = view.findViewById(R.id.btnAddProject);
      
      etStartDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
         selectDate(etStartDate);
         }
      });
      
      etEndDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            selectDate(etEndDate);
         }
      });
      
      btnAddProject.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String name = etProjectName.getText().toString();
            String type = etProjectType.getText().toString();
            String description = etDescription.getText().toString();
            String startDate = etStartDate.getText().toString();
            String endDate = etEndDate.getText().toString();
            String status = spinnerStatus.getSelectedItem().toString();
            
            if (validateInput(name, type, startDate, endDate)) {
               addProject(name, type, description, startDate, endDate, status);
               Toast.makeText(requireContext(), R.string.project_add_success,
                       Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
   
   @SuppressLint("ClickableViewAccessibility")
   private void selectDate(EditText date) {
      date.setOnTouchListener((v, event) -> {
         int inType = date.getInputType();
         date.setInputType(InputType.TYPE_NULL);
         date.onTouchEvent(event);
         date.setInputType(inType);
         return true;
      });
      
      MaterialDatePicker<Long> selectedDate = MaterialDatePicker.Builder.datePicker()
              .setTitleText("Selecione a data")
              .build();
      
      selectedDate.show(requireActivity().getSupportFragmentManager(), "date picker");
      selectedDate.addOnPositiveButtonClickListener(selection -> {
         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         calendar.setTimeInMillis(selection);
         SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
         String formattedDate = format.format(calendar.getTime());
         date.setText(formattedDate);
      });
   }
   
   private void addProject(String name, String type, String description, String startDate, String endDate, String status) {
      SQLiteDatabase db = dbHelper.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(DatabaseHelper.COLUMN_NAME, name);
      values.put(DatabaseHelper.COLUMN_TYPE, type);
      values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
      values.put(DatabaseHelper.COLUMN_START_DATE, startDate);
      values.put(DatabaseHelper.COLUMN_END_DATE, endDate);
      values.put(DatabaseHelper.COLUMN_STATUS, status);
      
      db.insert(DatabaseHelper.TABLE_PROJECTS, null, values);
      db.close();
   }
   private boolean validateInput(String name, String type, String startDate, String endDate) {
      if (name.isEmpty() || type.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
         Toast.makeText(requireContext(), R.string.input_validation_error,
                 Toast.LENGTH_SHORT).show();
         return false;
      }
      if(new Date(startDate).after(new Date(endDate))) {
         Toast.makeText(requireContext(), "A data de Inicio nao pode ser " +
                         "posterir a data de fim", Toast.LENGTH_SHORT)
                 .show();
         return false;
      }
      // Adicione mais validações conforme necessário
      return true;
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_add_project, container, false);
      return  view;
   }
}