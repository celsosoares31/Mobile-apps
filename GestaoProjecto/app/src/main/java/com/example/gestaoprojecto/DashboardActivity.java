// DashboardActivity.java
package com.example.gestaoprojecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalProjects;
    private TextView tvCompletedProjects;
    private TextView tvInProgressProjects;
    private TextView tvNotStartedProjects;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        tvTotalProjects = findViewById(R.id.tvTotalProjects);
        tvCompletedProjects = findViewById(R.id.tvCompletedProjects);
        tvInProgressProjects = findViewById(R.id.tvInProgressProjects);
        tvNotStartedProjects = findViewById(R.id.tvNotStartedProjects);

        updateDashboard();
    }

    private void updateDashboard() {
        List<Project> allProjects = getAllProjects();
        int totalProjects = allProjects.size();
        int completedProjects = 0;
        int inProgressProjects = 0;
        int notStartedProjects = 0;

        for (Project project : allProjects) {
            switch (project.getStatus()) {
                case "Completed":
                    completedProjects++;
                    break;
                case "In Progress":
                    inProgressProjects++;
                    break;
                case "Not Started":
                    notStartedProjects++;
                    break;
            }
        }

        tvTotalProjects.setText("Total Projects: " + totalProjects);
        tvCompletedProjects.setText("Completed Projects: " + completedProjects);
        tvInProgressProjects.setText("In Progress Projects: " + inProgressProjects);
        tvNotStartedProjects.setText("Not Started Projects: " + notStartedProjects);
    }

    private List<Project> getAllProjects() {
        return dbHelper.getAllProjects();
    }

    public void viewProjects(View view) {
        Intent intent = new Intent(this, ProjectListActivity.class);
        startActivity(intent);
    }
}
