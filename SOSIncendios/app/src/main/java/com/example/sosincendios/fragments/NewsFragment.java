package com.example.sosincendios.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sosincendios.R;
import com.example.sosincendios.databinding.FragmentNewsBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
   private FragmentNewsBinding binding;
   
   
   // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
   private static final String ARG_PARAM2 = "param2";
   
   // TODO: Rename and change types of parameters
   private String mParam1;
   private String mParam2;
   
   public NewsFragment() {
      // Required empty public constructor
   }
   
   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment NewsFragment.
    */
   // TODO: Rename and change types and number of parameters
   public static NewsFragment newInstance(String param1, String param2) {
      NewsFragment fragment = new NewsFragment();
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
      YouTubePlayerView youTubePlayerView = binding.youtubePlayerView;
      YouTubePlayerView youTubePlayerView1 = binding.youtubePlayerView1;
      YouTubePlayerView youTubePlayerView2 = binding.youtubePlayerView2;
      YouTubePlayerView youTubePlayerView3 = binding.youtubePlayerView3;
      YouTubePlayerView youTubePlayerView4 = binding.youtubePlayerView4;
      YouTubePlayerView youTubePlayerView5 = binding.youtubePlayerView5;
      YouTubePlayerView youTubePlayerView6 = binding.youtubePlayerView6;
   
      getLifecycle().addObserver(youTubePlayerView);
      getLifecycle().addObserver(youTubePlayerView1);
      getLifecycle().addObserver(youTubePlayerView2);
      getLifecycle().addObserver(youTubePlayerView3);
      getLifecycle().addObserver(youTubePlayerView4);
      getLifecycle().addObserver(youTubePlayerView5);
      getLifecycle().addObserver(youTubePlayerView6);
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentNewsBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }
}