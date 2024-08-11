package com.example.sosincendios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FireCarouselAdapter extends RecyclerView.Adapter<FireCarouselAdapter.FireViewHolder> {
   private final Context ctx;
   private List<Fire> fireList;
   
   private OnItemClickListener listener;
   
   public interface OnItemClickListener {
      void onItemClick(int position);
   }
   
   public void setOnItemClickListener(OnItemClickListener listener) {
      this.listener = listener;
   }
   
   public FireCarouselAdapter(Context ctx, List<Fire> fireList) {
      this.ctx = ctx;
      this.fireList = fireList;
   }
   
   public void setFireList(List<Fire> fireList) {
      this.fireList = fireList;
   }
   
   @NonNull
   @Override
   public FireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(ctx)
              .inflate(R.layout.item_fire_carousel, parent, false);
      
      return new FireViewHolder(view, listener);
   }
   
   @Override
   public void onBindViewHolder(@NonNull FireViewHolder holder, int position) {
      List<String> images = fireList.get(position).getImages();
      
      if(images != null){
         Glide.with(ctx).asBitmap().load(images.get(0)).into(holder.fireImage);
      }
      
      holder.fireCity.setText(fireList.get(position)
              .getCity());
      holder.firePlace.setText(fireList.get(position)
              .getPlace());
      
   }
   
   @Override
   public int getItemCount() {
      return fireList.size();
   }
   
   static class FireViewHolder extends RecyclerView.ViewHolder {
      
      ImageView fireImage;
      TextView fireCity, firePlace;
      
      private FireViewHolder(@NonNull View itemView,
                             final OnItemClickListener listener) {
         super(itemView);
         fireImage = itemView.findViewById(R.id.fireImage);
         firePlace = itemView.findViewById(R.id.firePlace);
         fireCity = itemView.findViewById(R.id.fireCity);
         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(listener != null){
                  int position = getAdapterPosition();
                  if(position != RecyclerView.NO_POSITION){
                     listener.onItemClick(position);
                  }
               }
            }
         });
      }
   }
}
