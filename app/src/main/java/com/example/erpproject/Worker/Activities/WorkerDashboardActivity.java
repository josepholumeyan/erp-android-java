package com.example.erpproject.Worker.Activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erpproject.Announcements.AnnouncementAdapter;
import com.example.erpproject.Announcements.AnnouncementDetailsFragment;
import com.example.erpproject.ViewModel.AnnouncementViewModel;
import com.example.erpproject.Worker.Bottomsheets.ChangePasswordBottomSheet;
import com.example.erpproject.Worker.Bottomsheets.LoanApplicationBottomSheet;
import com.example.erpproject.R;
import com.example.erpproject.Worker.Bottomsheets.VacationApplicationBottomSheet;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.AttendanceRepository;
import com.example.erpproject.repository.WorkerRepository;
import com.example.erpproject.utils;
import com.google.android.material.navigation.NavigationView;


public class WorkerDashboardActivity extends AppCompatActivity {

    private AnnouncementViewModel announcementViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_dashboard);
        String workerId = getIntent().getStringExtra("worker_Id");
        if (workerId == null) {
            Toast.makeText(this, "No worker ID received", Toast.LENGTH_SHORT).show();
            return;
        }
        DrawerLayout drawerLayout;
        WorkerRepository workerRepository=new WorkerRepository(getApplication());
        AttendanceRepository attendanceRepository = new AttendanceRepository(getApplication());
        announcementViewModel = new AnnouncementViewModel(getApplication());

        Button openDrawerButton = findViewById(R.id.drawer_button);
        TextView NameTextView = findViewById(R.id.workerName);
        TextView DepartmentTextView = findViewById(R.id.workerDepartment);
        TextView positionTextView = findViewById(R.id.workerRole);
        Button checkInButton = findViewById(R.id.worker_checkin_btn);
        Button checkOutButton = findViewById(R.id.worker_checkout_btn);

        RecyclerView recyclerView = findViewById(R.id.announcement_list);
        AnnouncementAdapter adapter = new AnnouncementAdapter();
        recyclerView.setAdapter(adapter);


        new Thread(() -> {
            Worker worker = workerRepository.getWorkerById(workerId);

            runOnUiThread(() -> {
                if (worker != null) {
                    NameTextView.setText(worker.getName());
                    DepartmentTextView.setText(worker.getDepartment());
                    positionTextView.setText(worker.getRole());

                    announcementViewModel.getAnnouncementsByDepartment(worker.getDepartment()).observe(this, adapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                }
                else{
                    Toast.makeText(this, "Worker not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        adapter.setOnAnnouncementClickListener(new AnnouncementAdapter.OnAnnouncementClickListener() {
            @Override
            public void onAnnouncementClicked(int announcementId) {
                AnnouncementDetailsFragment fragment = AnnouncementDetailsFragment.newInstance(announcementId);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });




        checkInButton.setOnClickListener(v -> {
            String date = utils.getCurrentDate();
            String time = utils.getCurrentTime();
            if (time.compareTo("09:00:00") > 0){
                attendanceRepository.checkIn(workerId, date, time,"Late");
            }
            else{
                attendanceRepository.checkIn(workerId, date, time,"Present");
            }

            Toast.makeText(this, "Checked in !", Toast.LENGTH_SHORT).show();
        });

        checkOutButton.setOnClickListener(v -> {
            String date = utils.getCurrentDate();
            String time = utils.getCurrentTime();

            attendanceRepository.checkOut(workerId, date, time);

            Toast.makeText(this, "Checked out!", Toast.LENGTH_SHORT).show();
        });


        drawerLayout = findViewById(R.id.DrawerLayout);
        NavigationView navigationView = findViewById(R.id.workerNavigationView);

        openDrawerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_apply_loan) {//handle loan application
                    LoanApplicationBottomSheet loan_sheet = new LoanApplicationBottomSheet();
                    Bundle bundle = new Bundle();
                    bundle.putString("worker_Id", workerId);
                    loan_sheet.setArguments(bundle);
                    loan_sheet.show(getSupportFragmentManager(), "loan_application");
                    return true;
                }
                else if (id == R.id.nav_apply_vacation) {//handle vacation application
                    VacationApplicationBottomSheet vacation_sheet = new VacationApplicationBottomSheet();
                    Bundle bundle = new Bundle();
                    bundle.putString("worker_Id", workerId);
                    vacation_sheet.setArguments(bundle);
                    vacation_sheet.show(getSupportFragmentManager(), "vacation_application");
                    return true;
                 }
                else if (id == R.id.nav_logout) {//handle logout
                    Intent intent = new Intent(this, WorkerLoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (id == R.id.change_password) {
                    ChangePasswordBottomSheet sheet = new ChangePasswordBottomSheet();
                    Bundle bundle = new Bundle();
                    bundle.putString("worker_Id", workerId);
                    sheet.setArguments(bundle);
                    sheet.show(getSupportFragmentManager(), "change_password");
                    return true;
                }
                else{return false;}
            });
        });

    }
}