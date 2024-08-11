package com.example.sosincendios.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sosincendios.Fire;
import com.example.sosincendios.FireAdapter;
import com.example.sosincendios.FireCarouselAdapter;
import com.example.sosincendios.FireViewModel;
import com.example.sosincendios.R;
import com.example.sosincendios.databinding.FragmentHomeBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
   private FragmentHomeBinding binding;
   private FireAdapter fireAdapter;
   private FireCarouselAdapter fireCarouselAdapter;
   private CircularProgressIndicator progressIndicator;
   private TextView textView, textView2, textView3;
   
   
   public HomeFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @return A new instance of fragment HomeFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static HomeFragment newInstance() {
      HomeFragment fragment = new HomeFragment();
      return fragment;
   }
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      
      FireViewModel fireViewModel = new ViewModelProvider(requireActivity()).get(FireViewModel.class);
      progressIndicator = binding.progressBar;
      progressIndicator.setVisibility(View.VISIBLE);
      textView = binding.textView;
      textView2 = binding.textView2;
      textView3 = binding.textView3;
      textView3.setVisibility(View.INVISIBLE);
      textView2.setVisibility(View.INVISIBLE);
      textView.setVisibility(View.GONE);
      // Observe LiveData
      fireViewModel.getFireList()
              .observe(getViewLifecycleOwner(), this::updateUI);
      
   }
   
   @SuppressLint("ResourceAsColor")
   private void updateUI(List<Fire> fireList) {
      
      
      FloatingActionButton floatingBtnAddFire = binding.floatingBtnAddFire;
      if(fireList != null && !fireList.isEmpty()) {
         
         textView3.setVisibility(View.VISIBLE);
         textView2.setVisibility(View.VISIBLE);
         textView.setVisibility(View.GONE);
         
         RecyclerView carousel_recycler_view = binding.carouselRecyclerView;
         //         carousel_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
         carousel_recycler_view.setLayoutManager(new CarouselLayoutManager(new MultiBrowseCarouselStrategy()));
         
         List<Fire> mostRecentFires = getMostRecentFires(fireList, 3);
         
         fireCarouselAdapter = new FireCarouselAdapter(requireActivity(), mostRecentFires);
         carousel_recycler_view.setAdapter(fireCarouselAdapter);
         
         ListView fireListView = binding.fireListView;
         fireAdapter = new FireAdapter(requireActivity(), fireList);
         
         fireListView.setAdapter(fireAdapter);
         
         progressIndicator.setVisibility(View.GONE);
         
         fireCarouselAdapter.setOnItemClickListener(new FireCarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               showDetails(position, fireList);
            }
         });
         
         fireListView.setOnItemClickListener((parent, view1, position, id) -> {
            showDetails(position, fireList);
         });
      }
      floatingBtnAddFire.setOnClickListener(v -> replaceFragment(new NewFireFragment(), true));
   }
   
   private void showDetails(int position, List<Fire> fireList) {
      Fire fire = fireList.get(position);
      FireDetailsFragment fireDetailsFragment = FireDetailsFragment.newInstance(fire);
      replaceFragment(fireDetailsFragment, true);
   }
   
   private List<Fire> getMostRecentFires(List<Fire> fires, int limit) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      
      Collections.sort(fires, (fire1, fire2) -> {
         try {
            Date date1 = dateFormat.parse(fire1.getFireReportDay());
            Date date2 = dateFormat.parse(fire2.getFireReportedBy());
            return date2.compareTo(date1);
         } catch (ParseException e) {
            e.printStackTrace();
         }
         return 0;
      });
      
      if (fires.size() > limit) {
         return fires.subList(0, limit);
      } else {
         return fires;
      }
   }
   
   private void replaceFragment(Fragment fireDetailsFragment, boolean b) {
      FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.frameLayout, fireDetailsFragment);
      if(b) {
         fragmentTransaction.addToBackStack(null);
      }
      fragmentTransaction.commit();
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      binding = FragmentHomeBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }
   
}