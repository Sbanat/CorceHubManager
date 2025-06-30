    package com.example.coursehubmanager;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.*;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import java.util.concurrent.Executors;

    public class CourseDetailsFragment extends Fragment {

        private static final String ARG_COURSE_ID = "courseId";
        private static final String ARG_COURSE_NAME = "courseName";
        private static final String ARG_INSTRUCTOR = "instructor";
        private static final String ARG_PRICE = "price";
        private static final String ARG_DETAILS = "details";
        private static final String ARG_DURATION = "duration";

        public static CourseDetailsFragment newInstance(Course course) {
            CourseDetailsFragment fragment = new CourseDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COURSE_ID, course.getId());
            args.putString(ARG_COURSE_NAME, course.getCourseName());
            args.putString(ARG_INSTRUCTOR, course.getInstructor());
            args.putDouble(ARG_PRICE, course.getPrice());
            args.putString(ARG_DETAILS, course.getCourseDetails());
            args.putString(ARG_DURATION, course.getCourseDuration());
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_course_details, container, false);

            TextView name = view.findViewById(R.id.courseName);
            TextView instructor = view.findViewById(R.id.instructor);
            TextView price = view.findViewById(R.id.price);
            TextView duration = view.findViewById(R.id.duration);
            TextView details = view.findViewById(R.id.details);

            Button joinBtn = view.findViewById(R.id.joinCourseButton);
            Button bookmarkBtn = view.findViewById(R.id.bookmarkButton);

            Bundle args = getArguments();
            if (args != null) {
                name.setText(args.getString(ARG_COURSE_NAME));
                instructor.setText("المحاضر: " + args.getString(ARG_INSTRUCTOR));
                price.setText("السعر: " + args.getDouble(ARG_PRICE) + " شيكل");
                duration.setText("المدة: " + args.getString(ARG_DURATION));
                details.setText("التفاصيل:\n" + args.getString(ARG_DETAILS));
            }

            joinBtn.setOnClickListener(v -> {
                Context context = getContext();
                if (context == null) return;

                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                int courseId = args != null ? args.getInt(ARG_COURSE_ID, -1) : -1;

                if (userId == -1 || courseId == -1) {
                    Toast.makeText(context, "خطأ في تحميل بيانات المستخدم أو الدورة", Toast.LENGTH_SHORT).show();
                    return;
                }

                Executors.newSingleThreadExecutor().execute(() -> {
                    Enrollment enrollment = new Enrollment(userId, courseId);
                    AppDatabase db = AppDatabase.getDatabase(context);
                    db.appDao().insertEnrollment(enrollment);

                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(context, "تم الانضمام إلى الدورة", Toast.LENGTH_SHORT).show()
                    );
                });
            });

            bookmarkBtn.setOnClickListener(v -> {
                Context context = getContext();
                if (context == null) return;

                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                int courseId = args != null ? args.getInt(ARG_COURSE_ID, -1) : -1;

                if (userId == -1 || courseId == -1) {
                    Toast.makeText(context, "خطأ في تحميل بيانات المستخدم أو الدورة", Toast.LENGTH_SHORT).show();
                    return;
                }

                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getDatabase(context);
                    Bookmark existing = db.appDao().getBookmarkByCourseId(courseId, userId);
                    if (existing == null) {
                        Bookmark bookmark = new Bookmark(userId, courseId);
                        db.appDao().insertBookmark(bookmark);
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(context, "تمت إضافة الدورة إلى المحفوظات", Toast.LENGTH_SHORT).show()
                        );
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(context, "هذه الدورة موجودة بالفعل في المحفوظات", Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            });

            return view;
        }
    }
