package com.example.sosincendios.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.sosincendios.Fire;
import com.example.sosincendios.FireViewModel;
import com.example.sosincendios.databinding.FragmentStatisticsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
   private FragmentStatisticsBinding binding;
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   private DatabaseReference databaseReference;
   private final String DBNAME = "fires";
   private List<Fire> fireList;
   private ScrollView scrollView;
   private TextView textView;
   private AnyChartView anyChartBarView;
   private AnyChartView anyChartPieView;
   
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
    *
    * @return A new instance of fragment StatisticsFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static StatisticsFragment newInstance() {
      StatisticsFragment fragment = new StatisticsFragment();
      return fragment;
   }
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentStatisticsBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      
      anyChartBarView = binding.anyChartBarView;
      APIlib.getInstance()
              .setActiveAnyChartView(anyChartBarView);
      databaseReference = FirebaseDatabase.getInstance()
              .getReference(DBNAME);
      FireViewModel fireViewModel = new ViewModelProvider(requireActivity()).get(FireViewModel.class);
      
      // Observe LiveData
      fireViewModel.getFireList()
              .observe(getViewLifecycleOwner(), new Observer<ArrayList<Fire>>() {
                 @Override
                 public void onChanged(ArrayList<Fire> fireList) {
                    loadFiresAndUpdateCharts(fireList);
                 }
              });
      
   }
   
   private void loadFiresAndUpdateCharts(ArrayList<Fire> fireList) {
      ArrayList<Fire> mFireList = new ArrayList<>();
      Map<String, Integer> firesPerYear = new HashMap<>();
      ListIterator<Fire> fireListIterator = fireList.listIterator();
      while(fireListIterator.hasNext()){
         Fire fire = fireListIterator.next();
         if(fire != null) {
            mFireList.add(fire);
            if(fire.getDateNoticed() != null) {
               String year = getYearFromDate(fire.getDateNoticed());
               if(year != null) {
                  Log.d("loadFiresAndUpdateCharts", "year" + year);
                  int count = firesPerYear.getOrDefault(year, 0);
                  firesPerYear.put(year, count + 1);
               }
            }
         }
      }
      
      showBarChart(mFireList);
      showPieChart(firesPerYear);
   }
   
   private void showPieChart(Map<String, Integer> firesPerYear) {
      anyChartPieView = binding.anyChartPieView;
      APIlib.getInstance()
              .setActiveAnyChartView(anyChartPieView);
      
      anyChartBarView.setProgressBar(binding.progressBar);
      Pie pie = AnyChart.pie();
      
      List<DataEntry> dataEntries = new ArrayList<>();
      for(Map.Entry<String, Integer> entry : firesPerYear.entrySet()) {
         dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
      }
      
      pie.data(dataEntries);
      pie.title("Incendios por ano");
      pie.labels()
              .position("outside");
      
      anyChartPieView.setChart(pie);
   }
   
   private String getYearFromDate(String dateNoticed) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
      try {
         return new SimpleDateFormat("yyyy", Locale.getDefault()).format(sdf.parse(dateNoticed));
      }catch(ParseException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private void showBarChart(List<Fire> fireList) {
      anyChartBarView.setProgressBar(binding.progressBar);
      Cartesian cartesian = AnyChart.bar();
      
      List<DataEntry> dataEntries = new ArrayList<>();
      Map<String, String> cityColorMap = new HashMap<>();
      cityColorMap.put("Maputo", "#00bcd4");
      cityColorMap.put("Inhambane", "#ff5722");
      cityColorMap.put("Gaza", "#4caf50");
      cityColorMap.put("Quelimane", "#4fff50");
      cityColorMap.put("Nampula", "#4fda50");
      
      
      for(Fire fire : fireList) {
         String city = fire.getCity();
         long count = fireList.stream()
                 .filter(f -> f.getCity()
                         .equals(city))
                 .count();
         String color = cityColorMap.getOrDefault(city, "#0f00d0");
         dataEntries.add(new CustomDataEntry(city, count, color));
      }
      
      Set set = Set.instantiate();
      set.data(dataEntries);
      
      Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value', fill: 'fill' }");
      
      Bar series1 = cartesian.bar(series1Mapping);
      series1.name("Incendios");
      
      series1.labels()
              .enabled(true);
      series1.labels()
              .position("center");
      series1.labels()
              .anchor("center");
      series1.labels()
              .format("{%Value}");
      series1.labels()
              .fontColor("#ffffff");
      
      cartesian.legend()
              .enabled(true);
      //      cartesian.legend().position("top").itemsLayout(String.valueOf(Layout.VERTICAL)).align(Align.CENTER);
      
      cartesian.animation(true);
      cartesian.title("Incendios por provincia");
      cartesian.yScale()
              .minimum(0d);
      cartesian.yAxis(0)
              .labels()
              .format("{%Value}{groupsSeparator: }");
      cartesian.tooltip()
              .positionMode(TooltipPositionMode.POINT)
              .position(Position.CENTER)
              .anchor(Anchor.CENTER_BOTTOM)
              .offsetX(0d)
              .offsetY(5d);
      cartesian.interactivity()
              .hoverMode(HoverMode.SINGLE);
      cartesian.xAxis(0)
              .title("Provincias");
      cartesian.yAxis(0)
              .title("Ocorrencias de Incendio");
      
      anyChartBarView.setChart(cartesian);
   }
   
   private static class CustomDataEntry extends ValueDataEntry {
      CustomDataEntry(String x, Number value, String fill) {
         super(x, value);
         setValue("fill", fill);
      }
   }
   
}