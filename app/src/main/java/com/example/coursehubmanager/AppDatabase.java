package com.example.coursehubmanager;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, Course.class, Lesson.class, Enrollment.class, Bookmark.class, Progress.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final String TAG = "AppDatabase";

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "coursehub_database")
                            .fallbackToDestructiveMigration()
                            .build();

                    databaseWriteExecutor.execute(() -> {
                        AppDao dao = INSTANCE.appDao();

                        Log.d(TAG, "Starting to insert default data...");

                        long adminId = dao.insertUser(new User("Admin", "admin@chm.com", "pass123", "0000000000", true));
                        Log.d(TAG, "Inserted Admin user id: " + adminId);

                        long sbanatId = dao.insertUser(new User("Sbanat", "Sbanat@chm.com", "pass123", "1111111111", false));
                        Log.d(TAG, "Inserted Sbanat user id: " + sbanatId);

                        long yhassanId = dao.insertUser(new User("Yhassan", "Yhassan@chm.com", "pass123", "2222222222", false));
                        Log.d(TAG, "Inserted Yhassan user id: " + yhassanId);

                        long msalehId = dao.insertUser(new User("MSaleh", "MSaleh@chm.com", "pass123", "3333333333", false));
                        Log.d(TAG, "Inserted MSaleh user id: " + msalehId);

                        long catDevId = dao.insertCategory(new Category() {{
                            setCategoryName("البرمجة");
                        }});
                        Log.d(TAG, "Inserted Category البرمجة id: " + catDevId);

                        long catDesignId = dao.insertCategory(new Category() {{
                            setCategoryName("التصميم");
                        }});
                        Log.d(TAG, "Inserted Category التصميم id: " + catDesignId);

                        long catPlanningId = dao.insertCategory(new Category() {{
                            setCategoryName("التخطيط");
                        }});
                        Log.d(TAG, "Inserted Category التخطيط id: " + catPlanningId);

                        long course1Id = dao.insertCourse(new Course() {{
                            setCourseName("مقدمة في البرمجة");
                            setInstructor("م.عدي فياض");
                            setPrice(0);
                            setCourseDetails("مبادئ لغة جافا");
                            setCourseDuration("3 أشهر");
                            setCategoryId((int) catDevId);
                        }});
                        Log.d(TAG, "Inserted Course المدرسي id: " + course1Id);

                        long course2Id = dao.insertCourse(new Course() {{
                            setCourseName("التصميم");
                            setInstructor("محمد حسونة");
                            setPrice(100);
                            setCourseDetails("دورة تصميم جرافيك للمبتدئين.");
                            setCourseDuration("شهران");
                            setCategoryId((int) catDesignId);
                        }});
                        Log.d(TAG, "Inserted Course التصميم id: " + course2Id);

                        long course3Id = dao.insertCourse(new Course() {{
                            setCourseName("التخطيط");
                            setInstructor("محمد سمحان");
                            setPrice(150);
                            setCourseDetails("دورة التخطيط الإستراتيجي.");
                            setCourseDuration("شهر ونصف");
                            setCategoryId((int) catPlanningId);
                        }});
                        Log.d(TAG, "Inserted Course التخطيط id: " + course3Id);

                        long lesson1Id = dao.insertLesson(new Lesson() {{
                            setCourseId((int) course1Id);
                            setLessonName("مقدمة الدورة المدرسية");
                        }});
                        Log.d(TAG, "Inserted Lesson مقدمة الدورة id: " + lesson1Id);

                        long lesson2Id = dao.insertLesson(new Lesson() {{
                            setCourseId((int) course1Id);
                            setLessonName("المادة الأولى");
                        }});
                        Log.d(TAG, "Inserted Lesson المادة الأولى id: " + lesson2Id);

                        long lesson3Id = dao.insertLesson(new Lesson() {{
                            setCourseId((int) course2Id);
                            setLessonName("مبادئ التصميم");
                        }});
                        Log.d(TAG, "Inserted Lesson مبادئ التصميم id: " + lesson3Id);

                        long lesson4Id = dao.insertLesson(new Lesson() {{
                            setCourseId((int) course3Id);
                            setLessonName("التخطيط الفعال");
                        }});
                        Log.d(TAG, "Inserted Lesson التخطيط الفعال id: " + lesson4Id);

                        for (User user : dao.getAllUsers()) {
                            dao.insertProgress(new Progress(user.getId(), (int) lesson1Id, false));
                            dao.insertProgress(new Progress(user.getId(), (int) lesson3Id, false));
                            Log.d(TAG, "Inserted Progress entries for user id: " + user.getId());
                        }

                        Log.d(TAG, "Finished inserting default data.");
                    });
                }
            }
        }
        return INSTANCE;
    }
}
