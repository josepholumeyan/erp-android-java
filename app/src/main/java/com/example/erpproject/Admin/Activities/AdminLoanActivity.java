package com.example.erpproject.Admin.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.Adapters.LoanAdapter;
import com.example.erpproject.Admin.Fragments.LoanInfoFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.LoanViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;

public class AdminLoanActivity extends AppCompatActivity {
    private Worker Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_loan);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        Button deleteAllLoanButton = findViewById(R.id.admin_delete_laons_btn);

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        LoanViewModel loanViewModel = new LoanViewModel(getApplication());

        RecyclerView recyclerView = findViewById(R.id.loan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// if vacation list works well delete this
        LoanAdapter adapter = new LoanAdapter();
        recyclerView.setAdapter(adapter);

        new Thread(() -> {
            Admin = workerRepository.getWorkerById(AdminId);
            runOnUiThread(() -> {
                if (Admin != null) {
                    loanViewModel.getLoanViews(Admin.getDepartment()).observe(this, adapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        adapter.setOnLoanClickListener(new LoanAdapter.OnLoanClickListener() {
            @Override
            public void onLoanClicked(int loanId) {

                LoanInfoFragment fragment = LoanInfoFragment.newInstance(loanId, AdminId);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        deleteAllLoanButton.setOnClickListener(v -> {loanViewModel.deleteLoansForDepartment(Admin.getDepartment());
            Toast.makeText(this, "Loans deleted", Toast.LENGTH_SHORT).show();});
    }
}
