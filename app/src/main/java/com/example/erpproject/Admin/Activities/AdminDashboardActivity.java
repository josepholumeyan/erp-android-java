package com.example.erpproject.Admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.Adapters.WorkerAdapter;
import com.example.erpproject.Admin.Fragments.WorkerInfoFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.LoanViewModel;
import com.example.erpproject.ViewModel.VacationViewModel;
import com.example.erpproject.ViewModel.WorkerViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.LoanRepository;
import com.example.erpproject.repository.VacationRepository;
import com.example.erpproject.repository.WorkerRepository;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private LiveData <List<Worker>> workers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        LoanRepository loanRepository = new LoanRepository(getApplication());
        VacationRepository vacationRepository = new VacationRepository(getApplication());

        WorkerViewModel workerViewModel = new WorkerViewModel(getApplication());
        LoanViewModel loanViewModel = new LoanViewModel(getApplication());
        VacationViewModel vacationViewModel = new VacationViewModel(getApplication());

        TextView workerCountTextView = findViewById(R.id.No_of_worker_in_dept);
        TextView loanCountTextView = findViewById(R.id.No_of_pending_loans);
        TextView vacationCountTextView = findViewById(R.id.No_of_pending_vacations);

        Button logoutButton = findViewById(R.id.logout_button);
        Button WorkerButton = findViewById(R.id.Admin_worker_btn);
        Button LoanButton = findViewById(R.id.Admin_loan_btn);
        Button VacationButton = findViewById(R.id.Admin_vacation_btn);
        Button AnnouncementsButton = findViewById(R.id.Admin_announcements_btn);

        RecyclerView recyclerView = findViewById(R.id.recent_workers_list);
        WorkerAdapter workerAdapter = new WorkerAdapter();
        recyclerView.setAdapter(workerAdapter);


        new Thread(() -> {
            Worker Admin = workerRepository.getWorkerById(AdminId);
            int workerCount = workerViewModel.getWorkerCountByDept(Admin.getDepartment());
            int loanCount = loanViewModel.getPendingLoansCountByDept(Admin.getDepartment());
            int vacationCount = vacationViewModel.getPendingVacationCountByDept(Admin.getDepartment());

            runOnUiThread(() -> {
                if (Admin != null) {
                    workerCountTextView.setText("Workers in Department: "+String.valueOf(workerCount));
                    loanCountTextView.setText("Pending Loans:"+String.valueOf(loanCount));
                    vacationCountTextView.setText("Pending Vacations:"+String.valueOf(vacationCount));
                    workerViewModel.getRecentWorkersDepartmentLive(Admin.getDepartment()).observe(this, workerAdapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                } else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

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
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });

        WorkerButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminWorkerActivity.class);
            intent.putExtra("Admin_Id", AdminId);
            startActivity(intent);
        });

        LoanButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminLoanActivity.class);
            intent.putExtra("Admin_Id", AdminId);
            startActivity(intent);
        });

        VacationButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminVacationActivity.class);
            intent.putExtra("Admin_Id", AdminId);
            startActivity(intent);
        });

        AnnouncementsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminAnnouncementActivity.class);
            intent.putExtra("Admin_Id", AdminId);
            startActivity(intent);
        });

    }

}
