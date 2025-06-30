package com.example.coursehubmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeFragmentAdapter extends FragmentStateAdapter {

    public HomeFragmentAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyCoursesFragment();
            case 1:
                return new BookmarkFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new MyCoursesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
