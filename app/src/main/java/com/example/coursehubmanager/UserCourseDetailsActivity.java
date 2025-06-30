package com.example.coursehubmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserCourseDetailsActivity extends AppCompatActivity {
    private TextView courseNameText, instructorText, priceText, detailsText, durationText;
    private Button joinButton, bookmarkButton;
    private AppDatabase db;
    private Course course;
    private int userId;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_user);

        Toolbar toolbar = findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("تفاصيل الدورة");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        courseNameText = findViewById(R.id.courseName);
        instructorText = findViewById(R.id.instructor);
        priceText = findViewById(R.id.price);
        detailsText = findViewById(R.id.details);
        durationText = findViewById(R.id.duration);
        joinButton = findViewById(R.id.joinButton);
        bookmarkButton = findViewById(R.id.bookmarkButton);

        db = AppDatabase.getDatabase(this);

        // استدعاء userId من SharedPreferences (تم التعديل هنا)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        int courseId = getIntent().getIntExtra("courseId", -1);

        executor.execute(() -> {
            course = db.appDao().getCourseById(courseId);
            if (course != null) {
                runOnUiThread(() -> {
                    courseNameText.setText(course.getCourseName());
                    instructorText.setText(course.getInstructor());
                    priceText.setText(String.format("%.2f", course.getPrice()));
                    detailsText.setText(course.getCourseDetails());
                    durationText.setText(course.getCourseDuration());
                    toolbar.setTitle(course.getCourseName());
                });
            }
        });

        joinButton.setOnClickListener(v -> {
            if (userId == -1 || course == null) return;
            executor.execute(() -> {
                Enrollment enrollment = new Enrollment(userId, course.getId());
                db.appDao().insertEnrollment(enrollment);
                runOnUiThread(() -> Toast.makeText(UserCourseDetailsActivity.this, "تم الانضمام للدورة", Toast.LENGTH_SHORT).show());
            });
        });

        bookmarkButton.setOnClickListener(v -> {
            if (userId == -1 || course == null) return;
            executor.execute(() -> {
                Bookmark bookmark = new Bookmark(userId, course.getId());
                db.appDao().insertBookmark(bookmark);
                runOnUiThread(() -> Toast.makeText(UserCourseDetailsActivity.this, "تمت إضافة الدورة للمحفوظات", Toast.LENGTH_SHORT).show());
            });
        });
    }
}
