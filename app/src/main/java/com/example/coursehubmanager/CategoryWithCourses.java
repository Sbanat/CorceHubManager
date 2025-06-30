package com.example.coursehubmanager;

import java.util.List;
import java.util.Objects;

public class CategoryWithCourses {
    private Category category;
    private List<Course> courses;

    // Constructor
    public CategoryWithCourses(Category category, List<Course> courses) {
        this.category = category;
        this.courses = courses;
    }

    // Getters and Setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // Override toString() for debugging or logging purposes
    @Override
    public String toString() {
        return "CategoryWithCourses{" +
                "category=" + category +
                ", courses=" + courses +
                '}';
    }

    // Override equals() and hashCode() for comparison and hashing purposes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryWithCourses that = (CategoryWithCourses) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, courses);
    }
}
