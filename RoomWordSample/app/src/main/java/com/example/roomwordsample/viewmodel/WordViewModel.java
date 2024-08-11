package com.example.roomwordsample.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.roomwordsample.entities.Word;
import com.example.roomwordsample.repositories.WordRepository;

import java.io.Closeable;
import java.util.List;

public class WordViewModel extends ViewModel {
    private WordRepository mRepository;
    private final LiveData<List<Word>> mALlWords;

    public WordViewModel(Application application) {
        super((Closeable) application);
        mRepository = new WordRepository(application);
        mALlWords = mRepository.getAllWords();
    }
   
  
    public LiveData<List<Word>> getALlWords(){
        return mALlWords;
    }
    public void insert(Word word){
        mRepository.insert(word);
    }

}

