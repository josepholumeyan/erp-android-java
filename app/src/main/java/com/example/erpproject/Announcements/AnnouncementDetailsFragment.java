package com.example.erpproject.Announcements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.erpproject.R;
import com.example.erpproject.ViewModel.AnnouncementViewModel;
import com.example.erpproject.data.entity.Announcement;

public class AnnouncementDetailsFragment extends Fragment {
    private static final String ARG_ANNOUNCEMENT_ID = "announcement_id";

    private TextView titleTextView, contentTextView;
    private Button closeButton;
    private int announcementId;
    private AnnouncementViewModel announcementViewModel;
    private Announcement announcement;

    public static AnnouncementDetailsFragment newInstance(int announcementId) {
        AnnouncementDetailsFragment fragment = new AnnouncementDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ANNOUNCEMENT_ID, announcementId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement_details, container, false);

        announcementViewModel = new AnnouncementViewModel(requireActivity().getApplication());
        titleTextView = view.findViewById(R.id.announcement_title_tv);
        contentTextView = view.findViewById(R.id.announcement_content_tv);
        closeButton=view.findViewById(R.id.close_btn);

        if (getArguments() != null) {
            announcementId = getArguments().getInt(ARG_ANNOUNCEMENT_ID);
        }

        new Thread(() -> {
            announcement = announcementViewModel.getAnnouncementById(announcementId);
            requireActivity().runOnUiThread(() -> {
                titleTextView.setText(announcement.getTitle());
                contentTextView.setText(announcement.getContent());
            });
        }).start();

        closeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
