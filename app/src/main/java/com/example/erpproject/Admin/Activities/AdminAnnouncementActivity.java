package com.example.erpproject.Admin.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.BottomSheets.MakeAnnouncementBottomSheet;
import com.example.erpproject.Announcements.AnnouncementAdapter;
import com.example.erpproject.Announcements.AnnouncementDetailsFragment;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.AnnouncementViewModel;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;

public class AdminAnnouncementActivity extends AppCompatActivity {
    private Worker Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcements);
        String AdminId = getIntent().getStringExtra("Admin_Id");
        if (AdminId == null) {
            Toast.makeText(this, "Admin ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        Button MakeAnnouncementButton = findViewById(R.id.admin_new_announcement_bn);

        WorkerRepository workerRepository = new WorkerRepository(getApplication());
        AnnouncementViewModel announcementViewModel = new AnnouncementViewModel(getApplication());

        RecyclerView recyclerView = findViewById(R.id.announcements_list);
        AnnouncementAdapter adapter = new AnnouncementAdapter();
        recyclerView.setAdapter(adapter);

        new Thread(() -> {
            Admin = workerRepository.getWorkerById(AdminId);
            runOnUiThread(() -> {
                if (Admin != null) {
                    announcementViewModel.getAnnouncementsByAdminId(Admin.getId()).observe(this, adapter::submitList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
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

        MakeAnnouncementButton.setOnClickListener(v -> {
            MakeAnnouncementBottomSheet bottomSheet = new MakeAnnouncementBottomSheet();
            Bundle bundle = new Bundle();
            bundle.putString("AdminId", AdminId);
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "MakeAnnouncementBottomSheet");
        });
    }

}
