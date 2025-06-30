package com.example.coursehubmanager;

import androidx.room.Entity;

@Entity(tableName = "enrollments", primaryKeys = {"userId", "courseId"})
public class Enrollment {
    private int userId;
    private int courseId;

    public Enrollment(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public int getUserId() { return userId; }
    public int getCourseId() { return courseId; }
}
