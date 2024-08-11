package com.example.gestaoprojecto.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gestaoprojecto.DatabaseHelper;
import com.example.gestaoprojecto.Project;
import com.example.gestaoprojecto.ProjectListActivity;
import com.example.gestaoprojecto.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   private TextView tvTotalProjects;
   private TextView tvCompletedProjects;
   private TextView tvInProgressProjects;
   private TextView tvNotStartedProjects;
   
   private DatabaseHelper dbHelper;
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public StatisticsFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment StatisticsFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static StatisticsFragment newInstance(String param1, String param2) {
      StatisticsFragment fragment = new StatisticsFragment();
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
      View view = inflater.inflate(R.layout.fragment_statistics, container,
              false);
      return  view;
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      dbHelper = new DatabaseHelper(requireContext());
      
      tvTotalProjects = view.findViewById(R.id.tvTotalProjects);
      tvCompletedProjects = view.findViewById(R.id.tvCompletedProjects);
      tvInProgressProjects = view.findViewById(R.id.tvInProgressProjects);
      tvNotStartedProjects = view.findViewById(R.id.tvNotStartedProjects);
      
      updateDashboard();
   }
   
   private void updateDashboard() {
      List<Project> allProjects = getAllProjects();
      int totalProjects = allProjects.size();
      int completedProjects = 0;
      int inProgressProjects = 0;
      int notStartedProjects = 0;
      
      for (Project project : allProjects) {
         switch (project.getStatus()) {
            case "Completed":
               completedProjects++;
               break;
            case "In Progress":
               inProgressProjects++;
               break;
            case "Not Started":
               notStartedProjects++;
               break;
         }
      }
      
      tvTotalProjects.setText("Total Projects: " + totalProjects);
      tvCompletedProjects.setText("Completed Projects: " + completedProjects);
      tvInProgressProjects.setText("In Progress Projects: " + inProgressProjects);
      tvNotStartedProjects.setText("Not Started Projects: " + notStartedProjects);
   }
   
   private List<Project> getAllProjects() {
      return dbHelper.getAllProjects();
   }
   
}