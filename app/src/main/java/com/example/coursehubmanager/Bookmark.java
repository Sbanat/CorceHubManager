package com.example.coursehubmanager;

import androidx.room.Entity;

@Entity(tableName = "bookmarks", primaryKeys = {"userId", "courseId"})
public class Bookmark {
    private int userId;
    private int courseId;

    public Bookmark(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public int getUserId() { return userId; }
    public int getCourseId() { return courseId; }
}
