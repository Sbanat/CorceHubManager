package com.example.coursehubmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CourseProgressActivity extends AppCompatActivity {

    private LinearLayout lectureList;
    private Button saveProgressButton;
    private String[] lectures = {
            "المحاضرة الأولى", "المحاضرة الثانية", "المحاضرة الثالثة",
            "المحاضرة الرابعة", "المحاضرة الخامسة", "المحاضرة السادسة",
            "المحاضرة السابعة", "المحاضرة الثامنة", "المحاضرة التاسعة", "المحاضرة العاشرة"
    };

    private boolean[] progressStatus = new boolean[lectures.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_progress);

        TextView courseTitle = findViewById(R.id.courseTitle);
        lectureList = findViewById(R.id.lectureList);
        saveProgressButton = findViewById(R.id.saveProgressButton);

        for (int i = 0; i < lectures.length; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(lectures[i]);
            int finalI = i;
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> progressStatus[finalI] = isChecked);
            lectureList.addView(checkBox);
        }

        saveProgressButton.setOnClickListener(v -> {
            int completed = 0;
            for (boolean status : progressStatus) {
                if (status) completed++;
            }

            int progressPercent = (int) (((float) completed / lectures.length) * 100);

            Toast.makeText(this, "تم حفظ التقدم: " + progressPercent + "%", Toast.LENGTH_SHORT).show();

        });
    }
}
