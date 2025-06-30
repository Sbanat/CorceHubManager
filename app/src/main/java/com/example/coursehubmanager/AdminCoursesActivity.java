package com.example.coursehubmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.List;

public class AdminCoursesActivity extends AppCompatActivity {

    private AppDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private LinearLayout courseContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_courses);

        db = AppDatabase.getDatabase(this);
        courseContainer = findViewById(R.id.courseContainer);
        Button addCourseBtn = findViewById(R.id.addCourseButton);

        addCourseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCoursesActivity.this, AddCourseActivity.class);
            startActivity(intent);
        });

        loadCourses();
    }

    private void loadCourses() {
        executor.execute(() -> {
            List<Course> courses = db.appDao().getAllCoursesStatic();  // use a non-LiveData version
            runOnUiThread(() -> {
                courseContainer.removeAllViews();
                for (Course course : courses) {
                    View courseView = createCourseView(course);
                    courseContainer.addView(courseView);
                }
            });
        });
    }

    private View createCourseView(Course course) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        TextView name = new TextView(this);
        name.setText("الدورة: " + course.getCourseName());

        TextView instructor = new TextView(this);
        instructor.setText("المدرب: " + course.getInstructor());

        Button edit = new Button(this);
        edit.setText("تعديل");
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminCourseDetailsActivity.class);
            intent.putExtra("courseId", course.getId());
            startActivity(intent);
        });

        Button delete = new Button(this);
        delete.setText("حذف");
        delete.setOnClickListener(v -> {
            executor.execute(() -> {
                db.appDao().deleteCourse(course);
                runOnUiThread(() -> {
                    Toast.makeText(this, "تم حذف الدورة", Toast.LENGTH_SHORT).show();
                    loadCourses();
                });
            });
        });

        layout.addView(name);
        layout.addView(instructor);
        layout.addView(edit);
        layout.addView(delete);

        return layout;
    }
}
