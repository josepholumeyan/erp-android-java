package com.example.erpproject.Admin.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.erpproject.R;
import com.example.erpproject.data.entity.Announcement;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.AnnouncementRepository;
import com.example.erpproject.repository.WorkerRepository;
import com.example.erpproject.utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MakeAnnouncementBottomSheet extends BottomSheetDialogFragment {
    private TextView titleTextView, contentTextView;
    private Button makeAnnouncementButton;
    private Announcement announcement;
    private Worker Admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_add_announcement, container, false);

        titleTextView = view.findViewById(R.id.announcement_title_input);
        contentTextView = view.findViewById(R.id.announcement_content_input);
        makeAnnouncementButton = view.findViewById(R.id.make_announcement_btn);

        assert getArguments() != null;
        String AdminId = getArguments().getString("AdminId");

        AnnouncementRepository repo = new AnnouncementRepository(requireActivity().getApplication());
        WorkerRepository worker_repo = new WorkerRepository(requireActivity().getApplication());

        new Thread(() -> {
            Admin = worker_repo.getWorkerById(AdminId);
        }).start();

        makeAnnouncementButton.setOnClickListener(v -> {
            String title = titleTextView.getText().toString();
            String content = contentTextView.getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            announcement = new Announcement(title, content, utils.getCurrentDate(), Admin.getDepartment(), AdminId, Admin.getName());
            repo.insert(announcement);
            Toast.makeText(requireContext(), "Announcement made", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return view;
    }
}
