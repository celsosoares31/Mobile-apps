package com.example.roomwordsample.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.roomwordsample.entities.Word;

import java.util.List;

public class WordListAdapter extends ListAdapter<Word, WordViewHolder> {
   Context context;
   
   public WordListAdapter(@NonNull DiffUtil.ItemCallback<Word> diffCallback) {
      super(diffCallback);
   }
   
   @Override
   public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      context = parent.getContext();
      return WordViewHolder.create(parent);
   }
   
   @Override
   public void onBindViewHolder(WordViewHolder holder, int position) {
      Word current = getItem(position);
      holder.bind(current.getWord());
   }
   
   public static class WordDiff extends DiffUtil.ItemCallback<Word> {
      
      @Override
      public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
         return oldItem == newItem;
      }
      
      @Override
      public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
         return oldItem.getWord()
                 .equals(newItem.getWord());
      }
   }
}
