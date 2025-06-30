package com.example.coursehubmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseName;
    private String instructor;
    private double price;
    private String courseDetails;
    private String courseDuration;
    private int categoryId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCourseDetails() { return courseDetails; }
    public void setCourseDetails(String courseDetails) { this.courseDetails = courseDetails; }

    public String getCourseDuration() { return courseDuration; }
    public void setCourseDuration(String courseDuration) { this.courseDuration = courseDuration; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
