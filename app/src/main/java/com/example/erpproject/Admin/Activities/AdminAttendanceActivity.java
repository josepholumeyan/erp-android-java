package com.example.erpproject.Admin.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.Adapters.AttendanceAdapter;
import com.example.erpproject.Admin.Fragments.WorkerInfoFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.AttendanceViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;
import com.example.erpproject.utils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class AdminAttendanceActivity extends AppCompatActivity {
    private Worker Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        Button openDrawerButton = findViewById(R.id.attendance_drawer_button);
        DrawerLayout drawerLayout;

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        AttendanceViewModel attendanceViewModel = new AttendanceViewModel(getApplication());
        RecyclerView recyclerView = findViewById(R.id.attendance_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AttendanceAdapter adapter = new AttendanceAdapter();
        recyclerView.setAdapter(adapter);

        new Thread(() -> {
            Admin = workerRepository.getWorkerById(AdminId);
            runOnUiThread(() -> {
                if (Admin != null) {
                    attendanceViewModel.getAttendanceByDepartment(Admin.getDepartment()).observe(this, adapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        adapter.setOnAttendanceClickListener(new AttendanceAdapter.OnAttendanceClickListener() {
            @Override
            public void onAttendanceClicked(String workerId) {
                WorkerInfoFragment fragment = WorkerInfoFragment.newInstance(workerId, AdminId);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        drawerLayout = findViewById(R.id.DrawerLayout);
        NavigationView navigationView = findViewById(R.id.AttendanceNavigationView);

        openDrawerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_mark_absent) {
                    LiveData<List<Worker>> AbsentWorkers = attendanceViewModel.getWorkersWithNoAttendanceByDept(Admin.getDepartment());
                    AbsentWorkers.observe(this, workers -> {
                        if (workers != null && !workers.isEmpty()) {
                            for (Worker worker : workers) {
                                attendanceViewModel.markAbsent(worker.getId(), utils.getCurrentDate(), utils.getCurrentTime());
                            }
                            Toast.makeText(this, "Absent marked for all workers", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No workers found with no attendance", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
                }
                else{return false;}
            });
        });
    }
}
