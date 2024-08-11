package com.example.control_gastos.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.control_gastos.DatabaseHelper;
import com.example.control_gastos.Expense;
import com.example.control_gastos.ExpenseAdapter;
import com.example.control_gastos.ExpenseReportAdapter;
import com.example.control_gastos.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment {
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   private  DatabaseHelper databaseHelper;
   private Map<String, Double> expenseListPerMonth;
   private Map<String, Double> expenseListPerDay;
   private ExpenseReportAdapter adapter;
   private RecyclerView recyclerViewDays;
   private RecyclerView recyclerViewMonths;
   
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public ReportsFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment ReportsFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static ReportsFragment newInstance(String param1, String param2) {
      ReportsFragment fragment = new ReportsFragment();
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
   View view = inflater.inflate(R.layout.fragment_reports, container, false);
   return  view;
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      databaseHelper = new DatabaseHelper(requireContext());
      recyclerViewDays = view.findViewById(R.id.dayRecyclerView);
      recyclerViewMonths = view.findViewById(R.id.monthRecyclerView);
      
      Log.d ("onViewCreated", ""+ getTotalExpensesPerMonth());
      loadMonthlyExpenses();
      loadDailyExpenses();
   }
   private void loadMonthlyExpenses() {
      expenseListPerMonth = getTotalExpensesPerMonth();
      adapter = new ExpenseReportAdapter(expenseListPerMonth);
      recyclerViewMonths.setAdapter(adapter);
   }
   private void loadDailyExpenses() {
      expenseListPerDay = getTotalExpensesPerDay();
      adapter = new ExpenseReportAdapter(expenseListPerDay);
      recyclerViewDays.setAdapter(adapter);
   }
   private Map<String, Double> getTotalExpensesPerMonth() {
      List<Expense> allExpenses = databaseHelper.getAllExpenses();
      Map<String, Double> totalExpensesPerMonth = new HashMap<>();
      
      for (Expense expense : allExpenses) {
         try {
            String expenseDate = expense.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(expenseDate);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            
            String key = String.format("%04d-%02d", year, month);
            
            if (!totalExpensesPerMonth.containsKey(key)) {
               totalExpensesPerMonth.put(key, 0.0);
            }
            
            totalExpensesPerMonth.put(key, totalExpensesPerMonth.get(key) + expense.getAmount());
         } catch (ParseException e) {
            e.printStackTrace();
         }
      }
      
      return totalExpensesPerMonth;
   }
   
   private Map<String, Double> getTotalExpensesPerDay() {
      List<Expense> allExpenses = databaseHelper.getAllExpenses();
      Map<String, Double> totalExpensesPerDay = new HashMap<>();
      
      for (Expense expense : allExpenses) {
         try {
            String expenseDate = expense.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(expenseDate);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            
            String key = String.format("%04d-%02d-%02d", year, month, day);
            
            if (!totalExpensesPerDay.containsKey(key)) {
               totalExpensesPerDay.put(key, 0.0);
            }
            
            totalExpensesPerDay.put(key, totalExpensesPerDay.get(key) + expense.getAmount());
         } catch (ParseException e) {
            e.printStackTrace();
         }
      }
      
      return totalExpensesPerDay;
   }
}