package com.example.coursehubmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<CategoryWithCourses> categoryList;
    private boolean isAdmin;

    public CategoryAdapter(Context context, List<CategoryWithCourses> categoryList, boolean isAdmin) {
        this.context = context;
        this.categoryList = categoryList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_courses, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryWithCourses catWithCourses = categoryList.get(position);
        holder.categoryName.setText(catWithCourses.getCategory().getCategoryName());

        CourseAdapter courseAdapter = new CourseAdapter(context, catWithCourses.getCourses(), isAdmin);
        holder.recyclerViewCourses.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.recyclerViewCourses.setAdapter(courseAdapter);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView recyclerViewCourses;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            recyclerViewCourses = itemView.findViewById(R.id.recyclerViewCourses);
        }
    }
}
