package com.example.coursehubmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private Button addCourseButton;
    private AppDatabase db;
    private boolean isAdmin;
    private SearchView searchView;
    private List<Course> fullCourseList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addCourseButton = view.findViewById(R.id.addCourseButton);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", getContext().MODE_PRIVATE);
        isAdmin = prefs.getBoolean("isAdmin", false);

        if (isAdmin) {
            addCourseButton.setVisibility(View.VISIBLE);
            addCourseButton.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), AddCourseActivity.class));
            });
        } else {
            addCourseButton.setVisibility(View.GONE);
        }

        db = AppDatabase.getDatabase(requireContext());

        db.appDao().getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            fullCourseList = courses;
            adapter = new CourseAdapter(requireContext(), fullCourseList, isAdmin);
            recyclerView.setAdapter(adapter);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCourses(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses(newText);
                return true;
            }
        });

        return view;
    }

    private void filterCourses(String text) {
        List<Course> filteredList = new ArrayList<>();
        for (Course course : fullCourseList) {
            if (course.getCourseName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(course);
            }
        }
        adapter.updateCourseList(filteredList);
    }
}
