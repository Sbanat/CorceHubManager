package com.example.coursehubmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;
    private boolean isAdmin;

    public CourseAdapter(Context context, List<Course> courseList, boolean isAdmin) {
        this.context = context;
        this.courseList = courseList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.instructor.setText("المدرب: " + course.getInstructor());
        holder.price.setText("السعر: " + course.getPrice() + " شيكل");

        holder.itemView.setOnClickListener(v -> {
            if (isAdmin) {
                Intent intent = new Intent(context, AdminCourseDetailsActivity.class);
                intent.putExtra("courseId", course.getId());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, UserCourseDetailsActivity.class);
                intent.putExtra("courseId", course.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void updateCourseList(List<Course> filteredList) {
        this.courseList = filteredList;
        notifyDataSetChanged();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, instructor, price;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            instructor = itemView.findViewById(R.id.instructor);
            price = itemView.findViewById(R.id.price);
        }
    }
}
