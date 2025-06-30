package com.example.coursehubmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCoursesFragment extends Fragment {
    private RecyclerView recyclerView;
    private AppDatabase db;
    private CourseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_courses, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMyCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getDatabase(getContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            db.appDao().getCoursesByUserEnrollments(userId).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> courses) {
                    adapter = new CourseAdapter(getContext(), courses, false);
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        return view;
    }
}
