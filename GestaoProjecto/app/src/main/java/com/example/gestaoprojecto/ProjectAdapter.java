package com.example.gestaoprojecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectAdapter extends ArrayAdapter<Project> {

    private Context mContext;
    private List<Project> mProjects;

    public ProjectAdapter(Context context, List<Project> projects) {
        super(context, 0, projects);
        mContext = context;
        mProjects = projects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        }

        Project project = mProjects.get(position);

        TextView projectName = convertView.findViewById(R.id.tvProjectName);
        TextView projectStatus = convertView.findViewById(R.id.tvProjectStatus);

        projectName.setText(project.getName());
        projectStatus.setText(project.getStatus());

        return convertView;
    }
}

