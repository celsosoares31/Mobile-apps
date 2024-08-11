package com.example.miniagenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbConnection extends SQLiteOpenHelper {
   private static final String DB_NAME = "mini_agenda.db";
   private static final int VERSION = 1;
   private Context ctx;
   
   public static final String TABLE_NAME = "person";
   private static final String COLUMN_ID = "person_id";
   private static final String COLUMN_NAME = "name";
   private static final String COLUMN_EMAIL = "email";
   private static final String COLUMN_ADDRESS = "address";
   private static final String COLUMN_PHONE = "phone";
   private static final String COLUMN_PICTURE = "profile_picture";
   
   public DbConnection(@Nullable Context context) {
      super(context, DB_NAME, null, VERSION);
      this.ctx = context;
   }
   
   @Override
   public void onCreate(SQLiteDatabase db) {
      String query = "CREATE TABLE " + TABLE_NAME + " ("
              + COLUMN_ID+ " integer primary key autoincrement, "
              + COLUMN_NAME+ " TEXT, "
              + COLUMN_EMAIL + " TEXT, "
              + COLUMN_ADDRESS + " TEXT, "
              + COLUMN_PHONE + " TEXT, "
              + COLUMN_PICTURE + " TEXT);";
      
      db.execSQL(query);
   }
   
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
      onCreate(db);
   }
   
   
   
}
