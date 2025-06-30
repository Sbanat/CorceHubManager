package com.example.coursehubmanager;

import android.os.Bundle;
import androidx.room.Room;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "coursehub_db")
                .allowMainThreadQueries()
                .build();

        db.appDao().getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (courses != null) {
                    courseAdapter = new CourseAdapter(getContext(), courses, false);
                    recyclerView.setAdapter(courseAdapter);
                }
            }
        });

        return view;
    }
}
