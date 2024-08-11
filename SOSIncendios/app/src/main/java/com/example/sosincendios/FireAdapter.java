package com.example.sosincendios;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.sosincendios.fragments.FireDetailsFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

public class FireAdapter extends BaseAdapter {
   Activity activity;
   List<Fire> fireList;
   
   public FireAdapter(Activity activity, List<Fire> fireList) {
      this.activity = activity;
      this.fireList = fireList;
   }
   
   @Override
   public int getCount() {
      return fireList.size();
   }
   
   @Override
   public Object getItem(int position) {
      return fireList.get(position);
   }
   
   @Override
   public long getItemId(int position) {
      return 0;
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      @SuppressLint("ViewHolder")
      
      View view = activity.getLayoutInflater()
              .inflate(R.layout.fire_item, parent, false);
      
//      ImageView fireImage = view.findViewById(R.id.fireImage);
      TextView fireCity = view.findViewById(R.id.fireCity);
//      TextView fireReportedBy = view.findViewById(R.id.reportedBy);
      TextView firePlace = view.findViewById(R.id.firePlace);
      TextView fireDate = view.findViewById(R.id.fireDate);
      
      Fire fire = fireList.get(position);
//      if(fireList.size() > 0){
//         List<String> images = fire.getImages();
//
//         Glide.with(view.getContext())
//                 .asBitmap()
//                 .load(images.get(0))
//                 .into(fireImage);
//
//      }
      fireCity.setText(fire.getCity());
      firePlace.setText(fire.getPlace());
      fireDate.setText(fire.getDateNoticed());
//      fireReportedBy.setText(fire.getFireReportedBy());
      
      return view;
   }
   
}
