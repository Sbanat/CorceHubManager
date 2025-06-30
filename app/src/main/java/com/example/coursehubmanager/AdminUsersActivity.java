package com.example.coursehubmanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminUsersActivity extends AppCompatActivity {

    private ListView listView;
    private AppDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private List<User> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        listView = findViewById(R.id.usersListView);
        db = AppDatabase.getDatabase(this);

        loadUsers();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = allUsers.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("خيارات المستخدم")
                    .setMessage("اختر إجراء:")
                    .setPositiveButton("تبديل الصلاحية", (dialog, which) -> {
                        selectedUser.setAdmin(!selectedUser.isAdmin());
                        executor.execute(() -> {
                            db.appDao().updateUser(selectedUser);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "تم تحديث صلاحية المستخدم", Toast.LENGTH_SHORT).show();
                                loadUsers();
                            });
                        });
                    })
                    .setNegativeButton("حذف المستخدم", (dialog, which) -> {
                        if (selectedUser.getEmail().equalsIgnoreCase("admin@chm.com")) {
                            Toast.makeText(this, "لا يمكن حذف الأدمن الرئيسي", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        executor.execute(() -> {
                            db.appDao().deleteUser(selectedUser);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "تم حذف المستخدم", Toast.LENGTH_SHORT).show();
                                loadUsers();
                            });
                        });
                    })
                    .setNeutralButton("إلغاء", null)
                    .show();
        });
    }

    private void loadUsers() {
        executor.execute(() -> {
            allUsers = db.appDao().getAllUsers();
            List<String> userStrings = getUserStrings(allUsers);

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userStrings);
                listView.setAdapter(adapter);
            });
        });
    }

    private List<String> getUserStrings(List<User> users) {
        List<String> userStrings = new ArrayList<>();
        for (User user : users) {
            String role = user.isAdmin() ? "أدمن" : "مستخدم";
            userStrings.add("ID: " + user.getId() + "\n" + user.getEmail() + "\nالنوع: " + role);
        }
        return userStrings;
    }
}
