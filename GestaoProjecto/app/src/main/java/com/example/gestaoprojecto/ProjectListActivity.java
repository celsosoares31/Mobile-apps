package com.example.gestaoprojecto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity {

    private ListView listViewProjects;
    private ProjectAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        dbHelper = new DatabaseHelper(this);

        listViewProjects = findViewById(R.id.listViewProjects);

        List<Project> projects = getAllProjects();
        adapter = new ProjectAdapter(this, projects);
        listViewProjects.setAdapter(adapter);
    }

    private List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PROJECTS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Project project = new Project();
                project.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                project.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
                project.setType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE)));
                project.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)));
                project.setStartDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE)));
                project.setEndDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE)));
                project.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS)));
                projectList.add(project);
            }
            cursor.close();
        }
        db.close();
        return projectList;
    }
}
