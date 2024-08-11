package com.example.sosincendios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.CarouselViewHolder> {
   
   private Context context;
   private List<String> itemList;
   
   public ImageCarouselAdapter(Context context, List<String> itemList) {
      this.context = context;
      this.itemList = itemList;
   }
   
   @NonNull
   @Override
   public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.item_images_carousel,
              parent, false);
      return new CarouselViewHolder(view);
   }
   
   @Override
   public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
      Glide.with(context).load(itemList.get(position)).into(holder.imageView);
   }
   
   @Override
   public int getItemCount() {
      int size = 0;
      if(itemList != null){
         size = itemList.size();
      }
      
      return size;
   }
   
   static class CarouselViewHolder extends RecyclerView.ViewHolder {
      ImageView imageView;
      public CarouselViewHolder(@NonNull View itemView) {
         super(itemView);
         imageView = itemView.findViewById(R.id.carousel_image_view);
      }
   }
}