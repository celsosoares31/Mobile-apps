package com.example.gestaoprojecto.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gestaoprojecto.DatabaseHelper;
import com.example.gestaoprojecto.Project;
import com.example.gestaoprojecto.ProjectAdapter;
import com.example.gestaoprojecto.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   private ListView listViewProjects;
   private ProjectAdapter adapter;
   private DatabaseHelper dbHelper;
   
   public HomeFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment HomeFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static HomeFragment newInstance(String param1, String param2) {
      HomeFragment fragment = new HomeFragment();
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
      View view = inflater.inflate(R.layout.fragment_home, container, false);
      return  view;
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      
      dbHelper = new DatabaseHelper(requireContext());
      
      listViewProjects = view.findViewById(R.id.listViewProjects);
      
      List<Project> projects = getAllProjects();
      adapter = new ProjectAdapter(requireContext(), projects);
      listViewProjects.setAdapter(adapter);
   }
   
   @SuppressLint("Range")
   private List<Project> getAllProjects() {
      List<Project> projectList = new ArrayList<>();
      SQLiteDatabase db = dbHelper.getReadableDatabase();
      Cursor cursor = db.query(DatabaseHelper.TABLE_PROJECTS, null, null, null, null, null, null);
      
      if (cursor != null) {
         while (cursor.moveToNext()) {
            Project project = new Project();
            project.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
            project.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
            project.setType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE)));
            project.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)));
            project.setStartDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE)));
            project.setEndDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE)));
            project.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS)));
            projectList.add(project);
         }
         cursor.close();
      }
      db.close();
      return projectList;
   }
}