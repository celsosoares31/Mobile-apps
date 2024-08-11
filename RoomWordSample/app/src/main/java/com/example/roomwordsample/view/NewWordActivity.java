package com.example.roomwordsample.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.roomwordsample.R;

public class NewWordActivity extends AppCompatActivity {

    public static String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_word);

        EditText mEditWordView = findViewById(R.id.edit_word);
        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(view ->{
//            Intent replyIntent = new Intent();
//            if (TextUtils.isEmpty(mEditWordView.getText())){
//                setResult(RESULT_CANCELED, replyIntent);
//            }else{
//                String word = mEditWordView.getText().toString();
//                replyIntent.putExtra(EXTRA_REPLY, word);
//                setResult(RESULT_OK, replyIntent);
//            }
//            finish();
        
        });
        
    }
}