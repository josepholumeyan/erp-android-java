package com.example.erpproject.Admin.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.Adapters.WorkerAdapter;
import com.example.erpproject.Admin.Fragments.AddWorkerFragment;
import com.example.erpproject.Admin.Fragments.WorkerInfoFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.WorkerViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;
import com.google.android.material.navigation.NavigationView;

public class AdminWorkerActivity extends AppCompatActivity {
    private Worker Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_worker);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        Button addWorkerButton = findViewById(R.id.admin_add_worker_btn);
        Button deleteAllWorkersButton = findViewById(R.id.admin_delete_workers_btn);
        Button openDrawerButton = findViewById(R.id.worker_drawer_button);
        DrawerLayout drawerLayout;

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        WorkerViewModel workerViewModel = new WorkerViewModel(getApplication());

        RecyclerView recyclerView = findViewById(R.id.workers_list);
        WorkerAdapter workerAdapter = new WorkerAdapter();
        recyclerView.setAdapter(workerAdapter);

        new Thread(() -> {
            Admin = workerRepository.getWorkerById(AdminId);
            runOnUiThread(() -> {
                if (Admin != null) {
                    workerViewModel.getWorkerByDept(Admin.getDepartment()).observe(this, workerAdapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                } else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
        deleteAllWorkersButton.setOnClickListener(v -> {
            new Thread(() -> {
            workerRepository.deleteWorkersByDepartmentExceptAdmins(Admin.getDepartment());
            runOnUiThread(() -> {
            Toast.makeText(this, "All workers deleted", Toast.LENGTH_SHORT).show();
            });
            }).start();
        });
        addWorkerButton.setOnClickListener(v -> {
            AddWorkerFragment fragment = AddWorkerFragment.newInstance(AdminId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        workerAdapter.setOnWorkerClickListener(new WorkerAdapter.OnWorkerClickListener() {
            @Override
            public void onWorkerClicked(String workerId) {

                WorkerInfoFragment fragment = WorkerInfoFragment.newInstance(workerId, AdminId);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        drawerLayout = findViewById(R.id.DrawerLayout);
        NavigationView navigationView = findViewById(R.id.AdminWorkerNavigationView);

        openDrawerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_check_attendance) {
                    Intent intent = new Intent(this, AdminAttendanceActivity.class);
                    intent.putExtra("Admin_Id", AdminId);
                    startActivity(intent);
                    return true;
                }
                else{return false;}
            });
        });
    }
    private void openAddWorkerFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddWorkerFragment())
                .addToBackStack("add_worker")
                .commit();
    }
}
