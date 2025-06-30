package com.example.coursehubmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookmarkFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private AppDatabase db;
    private int userId;
    private boolean isAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", getContext().MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);  // Assuming the userId is stored in SharedPreferences
        isAdmin = prefs.getBoolean("isAdmin", false);  // Assuming the isAdmin flag is stored in SharedPreferences

        db = AppDatabase.getDatabase(requireContext());

        db.appDao().getCoursesByUserBookmarks(userId).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (courses != null && !courses.isEmpty()) {
                    adapter = new CourseAdapter(requireContext(), courses, isAdmin);  // Pass isAdmin here
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "لا توجد دورات محفوظه", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
