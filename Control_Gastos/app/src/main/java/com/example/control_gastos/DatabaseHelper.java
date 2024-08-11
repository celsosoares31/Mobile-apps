package com.example.control_gastos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_ITEM + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_PHOTO + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public boolean addExpense(String date, String time, String location, String item, double amount, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_ITEM, item);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_PHOTO, photo);

        long result = db.insert(TABLE_EXPENSES, null, contentValues);
        return result != -1;
    }

    
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    String item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM));
                    double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                    String photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));

                    Expense expense = new Expense(id, date, time, location, item, amount, photo);
                    expenseList.add(expense);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return expenseList;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
       try(Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password})) {
          return cursor.getCount() > 0;
       }
    }
    
}
