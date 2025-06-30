package com.example.coursehubmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminCourseDetailsActivity extends AppCompatActivity {
    private EditText courseNameEditText, instructorEditText, priceEditText, detailsEditText, durationEditText, categoryEditText;
    private Button saveButton, deleteButton;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private AppDatabase db;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_details);

        courseNameEditText = findViewById(R.id.courseName);
        instructorEditText = findViewById(R.id.instructor);
        priceEditText = findViewById(R.id.price);
        detailsEditText = findViewById(R.id.details);
        durationEditText = findViewById(R.id.duration);
        categoryEditText = findViewById(R.id.category);
        saveButton = findViewById(R.id.saveCourseButton);
        deleteButton = findViewById(R.id.deleteCourseButton);

        int courseId = getIntent().getIntExtra("courseId", -1);
        db = AppDatabase.getDatabase(this);

        executor.execute(() -> {
            course = db.appDao().getCourseById(courseId);
            if (course != null) {
                runOnUiThread(() -> {
                    courseNameEditText.setText(course.getCourseName());
                    instructorEditText.setText(course.getInstructor());
                    priceEditText.setText(String.valueOf(course.getPrice()));
                    detailsEditText.setText(course.getCourseDetails());
                    durationEditText.setText(course.getCourseDuration());

                    db.appDao().getAllCategories().observe(this, new Observer<List<Category>>() {
                        @Override
                        public void onChanged(List<Category> categories) {
                            for (Category cat : categories) {
                                if (cat.getId() == course.getCategoryId()) {
                                    categoryEditText.setText(cat.getCategoryName());
                                    break;
                                }
                            }
                        }
                    });
                });
            }
        });

        saveButton.setOnClickListener(v -> {
            String courseName = courseNameEditText.getText().toString().trim();
            String instructor = instructorEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();
            String details = detailsEditText.getText().toString().trim();
            String duration = durationEditText.getText().toString().trim();
            String categoryName = categoryEditText.getText().toString().trim();

            if (courseName.isEmpty() || instructor.isEmpty() || priceStr.isEmpty() || details.isEmpty() || duration.isEmpty() || categoryName.isEmpty()) {
                Toast.makeText(this, "يرجى ملء جميع الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "السعر غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                List<Category> all = db.appDao().getAllCategories().getValue();
                Category category = null;
                if (all != null) {
                    for (Category cat : all) {
                        if (cat.getCategoryName().equalsIgnoreCase(categoryName)) {
                            category = cat;
                            break;
                        }
                    }
                }

                if (category == null) {
                    category = new Category();
                    category.setCategoryName(categoryName);
                    db.appDao().insertCategory(category);
                }

                course.setCourseName(courseName);
                course.setInstructor(instructor);
                course.setPrice(price);
                course.setCourseDetails(details);
                course.setCourseDuration(duration);
                course.setCategoryId(category.getId());

                db.appDao().updateCourse(course);
                runOnUiThread(() -> Toast.makeText(this, "تم تحديث الدورة", Toast.LENGTH_SHORT).show());
            });
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("تأكيد الحذف")
                    .setMessage("هل أنت متأكد من حذف هذه الدورة؟")
                    .setPositiveButton("نعم", (dialog, which) -> executor.execute(() -> {
                        db.appDao().deleteCourse(course);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "تم حذف الدورة", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }))
                    .setNegativeButton("لا", null)
                    .show();
        });
    }
}
