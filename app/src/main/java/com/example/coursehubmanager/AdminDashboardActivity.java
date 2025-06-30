package com.example.coursehubmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button manageCoursesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button manageCoursesBtn = findViewById(R.id.manageCoursesButton);
        manageCoursesBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminCoursesActivity.class));
        });

        Button manageUsersButton = findViewById(R.id.manageUsersButton);
        manageUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminUsersActivity.class);
            startActivity(intent);
        });
    }
}
