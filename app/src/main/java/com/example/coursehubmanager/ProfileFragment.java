package com.example.coursehubmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {
    private TextView nameTextView, emailTextView;
    private AppDatabase db;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        db = AppDatabase.getDatabase(getContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", getContext().MODE_PRIVATE);
        String userEmail = prefs.getString("email", null);

        Executors.newSingleThreadExecutor().execute(() -> {
            if (userEmail != null) {
                User user = db.appDao().getUserByEmail(userEmail);
                if (user != null) {
                    userId = user.getId();
                    getActivity().runOnUiThread(() -> {
                        nameTextView.setText(user.getUsername());
                        emailTextView.setText(user.getEmail());
                    });
                }
            }
        });

        return view;
    }
}
