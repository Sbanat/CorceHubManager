// CourseDetailsUserActivity.java
package com.example.coursehubmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseDetailsUserActivity extends AppCompatActivity {

    private LinearLayout lectureList;
    private Button saveProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_user);

        lectureList = findViewById(R.id.lectureList);
        saveProgressButton = findViewById(R.id.saveProgressButton);

        displayLectures();

        saveProgressButton.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                SharedPreferences prefs = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);
                if (userId == -1) return;

                for (int i = 0; i < lectureList.getChildCount(); i++) {
                    View view = lectureList.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        int lessonId = (int) checkBox.getTag();
                        boolean completed = checkBox.isChecked();

                        Progress existing = db.appDao().getProgress(userId, lessonId);
                        if (existing == null) {
                            db.appDao().insertProgress(new Progress(userId, lessonId, completed));
                        } else {
                            existing.setCompleted(completed);
                            db.appDao().updateProgress(existing);
                        }
                    }
                }

                runOnUiThread(() -> Toast.makeText(this, "تم حفظ التقدم", Toast.LENGTH_SHORT).show());
            });
        });
    }

    private void displayLectures() {
        Executor executor = Executors.newSingleThreadExecutor();
        AppDatabase db = AppDatabase.getDatabase(this);
        SharedPreferences prefs = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        int courseId = getIntent().getIntExtra("courseId", -1);

        executor.execute(() -> {
            List<Lesson> lessons = db.appDao().getLessonsByCourseId(courseId);
            runOnUiThread(() -> {
                lectureList.removeAllViews();
                for (Lesson lesson : lessons) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(lesson.getLessonName());
                    checkBox.setTag(lesson.getId());

                    Progress progress = db.appDao().getProgress(userId, lesson.getId());
                    if (progress != null && progress.isCompleted()) {
                        checkBox.setChecked(true);
                    }

                    lectureList.addView(checkBox);
                }
            });
        });
    }
}
