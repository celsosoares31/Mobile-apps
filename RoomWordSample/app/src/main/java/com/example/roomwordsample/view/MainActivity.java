package com.example.roomwordsample.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomwordsample.R;
import com.example.roomwordsample.entities.Word;
import com.example.roomwordsample.viewholder.WordListAdapter;
import com.example.roomwordsample.viewmodel.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
WordViewModel mWordViewModel;
   
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      mWordViewModel =
              new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(WordViewModel.class);
      
      
//      mWordViewModel = new ViewModelProvider (this,
//               ViewModelProvider.AndroidViewModelFactory.getInstance (this.getApplication())). get(WordViewModel.class);
//
//       mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
//                       new ViewModelProvider.AndroidViewModelFactory()).get(WordViewModel.class);
//        mWordViewModel.getALlWords().observe(this, adapter::submitList);
//       mWordViewModel.init(this.getApplication());
      

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            newWordActivityResultLauncher.launch(intent);
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
   
    // Declare the activity result launcher
    ActivityResultLauncher<Intent> newWordActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
               assert data != null;
               Word word = new Word(Objects.requireNonNull(data.getStringExtra(NewWordActivity.EXTRA_REPLY)));
                mWordViewModel.insert(word);
            } else{
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }
    });
    
    
}

