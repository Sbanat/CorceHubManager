package com.example.coursehubmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private final Context context;
    private final List<Course> bookmarkCourses;

    public BookmarkAdapter(List<Course> bookmarkCourses, Context context) {
        this.context = context;
        this.bookmarkCourses = bookmarkCourses;
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookmark_course, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Course course = bookmarkCourses.get(position);
        holder.courseNameTextView.setText(course.getCourseName());
        holder.instructorTextView.setText(course.getInstructor());
    }

    @Override
    public int getItemCount() {
        return bookmarkCourses.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTextView;
        TextView instructorTextView;

        public BookmarkViewHolder(View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            instructorTextView = itemView.findViewById(R.id.instructorTextView);
        }
    }
}
