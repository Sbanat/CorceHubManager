package com.example.coursehubmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddCourseActivity extends AppCompatActivity {
    private EditText courseNameEditText, instructorEditText, priceEditText, detailsEditText, durationEditText, categoryEditText;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseNameEditText = findViewById(R.id.courseName);
        instructorEditText = findViewById(R.id.instructorName);
        priceEditText = findViewById(R.id.coursePrice);
        detailsEditText = findViewById(R.id.courseDetails);
        durationEditText = findViewById(R.id.courseDuration);
        categoryEditText = findViewById(R.id.category);

        Button saveButton = findViewById(R.id.saveCourseButton);
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

            AppDatabase db = AppDatabase.getDatabase(this);

            executor.execute(() -> {
                List<Category> categories = db.appDao().getAllCategoriesRaw();
                Category category = null;
                for (Category cat : categories) {
                    if (cat.getCategoryName().equalsIgnoreCase(categoryName)) {
                        category = cat;
                        break;
                    }
                }

                if (category == null) {
                    category = new Category();
                    category.setCategoryName(categoryName);
                    long newCatId = db.appDao().insertCategory(category);
                    category.setId((int) newCatId);
                }

                Course course = new Course();
                course.setCourseName(courseName);
                course.setInstructor(instructor);
                course.setPrice(price);
                course.setCourseDetails(details);
                course.setCourseDuration(duration);
                course.setCategoryId(category.getId());

                db.appDao().insertCourse(course);

                runOnUiThread(() -> {
                    Toast.makeText(this, "تم إضافة الدورة بنجاح", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
