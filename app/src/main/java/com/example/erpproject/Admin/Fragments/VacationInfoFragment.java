package com.example.erpproject.Admin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.erpproject.Admin.ViewClasses.VacationViewClass;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.VacationViewModel;
import com.example.erpproject.repository.VacationRepository;


public class VacationInfoFragment extends Fragment {
    private static final String ARG_VACATION_ID = "vacation_id";

    private TextView nameView, idView, vacationDurationView,statusView,dateAppliedView,reasonView, lastVacationView, VacationDaysLeftView;

    private Button ViewWorkerBtn, ApproveVacationBtn, RejectVacationBtn;
    private VacationViewClass vacationViewClass;
    private int vacationId;
    private VacationViewModel vacationViewModel;

    public static VacationInfoFragment newInstance(int vacationId) {
        VacationInfoFragment fragment = new VacationInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_VACATION_ID, vacationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_vacation, container, false);

        nameView=view.findViewById(R.id.worker_name_tvview);
        idView=view.findViewById(R.id.worker_id_tvview);
        vacationDurationView=view.findViewById(R.id.vacation_period_tvview);
        statusView=view.findViewById(R.id.status_tvview);
        dateAppliedView=view.findViewById(R.id.date_applied_tvview);
        reasonView=view.findViewById(R.id.vacation_reason_tvView);
        lastVacationView=view.findViewById(R.id.last_vacation_tvView);
        VacationDaysLeftView=view.findViewById(R.id.vacation_days_left_tvView);

        ViewWorkerBtn=view.findViewById(R.id.view_worker_btn);
        ApproveVacationBtn=view.findViewById(R.id.approve_vacation_btn);
        RejectVacationBtn=view.findViewById(R.id.reject_vacation_btn);

        vacationViewModel = new ViewModelProvider(requireActivity()).get(VacationViewModel.class);

        if (getArguments() != null) {
            vacationId = getArguments().getInt(ARG_VACATION_ID);
        }
        new Thread(() -> {
            vacationViewClass = vacationViewModel.getSingleVacationView(vacationId);
            requireActivity().runOnUiThread(() -> {
                nameView.setText(vacationViewClass.getWorkerName());
                idView.setText(vacationViewClass.getWorkerId());
                vacationDurationView.setText(String.valueOf(vacationViewClass.getVacationDuration()));
                statusView.setText(vacationViewClass.getStatus());
                dateAppliedView.setText(vacationViewClass.getDateApplied());
                reasonView.setText(vacationViewClass.getReason());
                lastVacationView.setText(vacationViewClass.getLastVacation());
                VacationDaysLeftView.setText(String.valueOf(vacationViewClass.getVacationDaysLeft()));
            });
        }).start();


        ViewWorkerBtn.setOnClickListener(v -> {
            WorkerInfoFragment fragment = WorkerInfoFragment.newInstance(vacationViewClass.getWorkerId());

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        ApproveVacationBtn.setOnClickListener(v -> {
            VacationRepository.VacationCallback callback= new VacationRepository.VacationCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Application sent successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                }
                @Override
                public void onFailure(String message) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    });
                }
            };
            vacationViewModel.approveVacation(vacationId, callback);
        });

        RejectVacationBtn.setOnClickListener(v -> {
            VacationRepository.VacationCallback callback= new VacationRepository.VacationCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Application sent successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                }
                @Override
                public void onFailure(String message) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    });
                }
            };
                vacationViewModel.rejectVacation(vacationId, callback);
        });

        return view;
    }
}
