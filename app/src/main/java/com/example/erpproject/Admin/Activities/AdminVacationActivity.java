package com.example.erpproject.Admin.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.erpproject.Admin.Adapters.VacationAdapter;

import com.example.erpproject.Admin.Fragments.VacationInfoFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.VacationViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;

public class AdminVacationActivity extends AppCompatActivity {
    private Worker Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vacation);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        Button deleteAllVacationButton = findViewById(R.id.admin_delete_vacations_btn);
        RecyclerView recyclerView = findViewById(R.id.vacation_list);

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        VacationViewModel vacationViewModel = new VacationViewModel(getApplication());

        // Set up RecyclerView and adapter if list doesn't work
        VacationAdapter adapter = new VacationAdapter();
        recyclerView.setAdapter(adapter);

        new Thread(() -> {
            Admin = workerRepository.getWorkerById(AdminId);
            runOnUiThread(() -> {
                if (Admin != null) {
                    vacationViewModel.getVacationViews(Admin.getDepartment()).observe(this, adapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));//i'm talking bout this in line 34
                }
                else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();}
            });
        }).start();
        adapter.setOnVacationClickListener(new VacationAdapter.OnVacationClickListener() {
            @Override
            public void onVacationClicked(int vacationId) {

                VacationInfoFragment fragment = VacationInfoFragment.newInstance(vacationId);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        deleteAllVacationButton.setOnClickListener(v -> {vacationViewModel.deleteVacationsForDepartment(Admin.getDepartment());
            Toast.makeText(this, "Vacations deleted", Toast.LENGTH_SHORT).show();
        });

    }
}
