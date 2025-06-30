package com.example.coursehubmanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coursehubmanager.NavigationBarFragment;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NavigationBarFragment())
                    .commit();
        }
    }
}
