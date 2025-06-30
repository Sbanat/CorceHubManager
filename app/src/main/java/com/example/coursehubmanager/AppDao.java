package com.example.coursehubmanager;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCategory(Category category);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories")
    List<Category> getAllCategoriesRaw();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses")
    List<Course> getAllCoursesStatic();

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses WHERE id IN (SELECT courseId FROM bookmarks WHERE userId = :userId)")
    LiveData<List<Course>> getCoursesByUserBookmarks(int userId);

    @Query("SELECT * FROM courses WHERE id IN (SELECT courseId FROM enrollments WHERE userId = :userId)")
    LiveData<List<Course>> getCoursesByUserEnrollments(int userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertLesson(Lesson lesson);

    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    List<Lesson> getLessonsByCourseId(int courseId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEnrollment(Enrollment enrollment);

    @Query("SELECT * FROM enrollments WHERE userId = :userId")
    List<Enrollment> getEnrollmentsByUserId(int userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBookmark(Bookmark bookmark);

    @Query("SELECT * FROM bookmarks WHERE userId = :userId")
    List<Bookmark> getBookmarksByUserId(int userId);

    @Query("SELECT * FROM bookmarks WHERE courseId = :courseId AND userId = :userId LIMIT 1")
    Bookmark getBookmarkByCourseId(int courseId, int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgress(Progress progress);

    @Update
    void updateProgress(Progress progress);

    @Query("SELECT * FROM progress WHERE userId = :userId")
    List<Progress> getProgressByUserId(int userId);

    @Query("SELECT * FROM progress WHERE userId = :userId AND lessonId = :lessonId LIMIT 1")
    Progress getProgress(int userId, int lessonId);
}
