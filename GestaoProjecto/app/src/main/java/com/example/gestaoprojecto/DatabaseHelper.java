package com.example.gestaoprojecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definições das constantes para o banco de dados
    private static final String DATABASE_NAME = "project_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Definições das colunas da tabela de projetos
    public static final String TABLE_PROJECTS = "projects";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_STATUS = "status";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método para criação da tabela no banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PROJECTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT, " +
                COLUMN_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    // Método para atualização do banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        onCreate(db);
    }

    // Método para adicionar um novo projeto ao banco de dados
    public boolean addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, project.getName());
        values.put(COLUMN_TYPE, project.getType());
        values.put(COLUMN_DESCRIPTION, project.getDescription());
        values.put(COLUMN_START_DATE, project.getStartDate());
        values.put(COLUMN_END_DATE, project.getEndDate());
        values.put(COLUMN_STATUS, project.getStatus());

        long result = db.insert(TABLE_PROJECTS, null, values);
        db.close();
        return result != -1;
    }

    // Método para recuperar todos os projetos do banco de dados
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROJECTS, null, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Project project = new Project();
                    project.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    project.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    project.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                    project.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    project.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)));
                    project.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)));
                    project.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                    projectList.add(project);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return projectList;
    }
}
