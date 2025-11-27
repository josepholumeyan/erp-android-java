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

import com.example.erpproject.R;
import com.example.erpproject.ViewModel.WorkerViewModel;

public class WorkerInfoFragment extends Fragment {

    private static final String ARG_WORKER_ID = "worker_id";

    private TextView nameView, idView, deptView, roleView, ageView, salaryView, debtView,attendanceScoreView;
    private Button deleteBtn;

    private String workerId;
    private WorkerViewModel workerViewModel;

    public static WorkerInfoFragment newInstance(String workerId) {
        WorkerInfoFragment fragment = new WorkerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORKER_ID, workerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_info_fragment, container, false);

        nameView = view.findViewById(R.id.info_worker_name);
        idView = view.findViewById(R.id.info_worker_id);
        deptView = view.findViewById(R.id.info_worker_dept);
        roleView = view.findViewById(R.id.info_worker_role);
        ageView = view.findViewById(R.id.info_worker_age);
        salaryView = view.findViewById(R.id.info_worker_salary);
        debtView = view.findViewById(R.id.info_worker_debt);
        deleteBtn = view.findViewById(R.id.delete_worker_btn);
        attendanceScoreView = view.findViewById(R.id.worker_attendanceScore_view);
        ;

        workerViewModel = new ViewModelProvider(requireActivity()).get(WorkerViewModel.class);

        if (getArguments() != null) {
            workerId = getArguments().getString(ARG_WORKER_ID);
        }

        workerViewModel.getWorkerByIdLive(workerId).observe(getViewLifecycleOwner(), worker -> {
            if (worker != null) {
                nameView.setText(worker.getName());
                idView.setText("ID: " + workerId);
                deptView.setText(worker.getDepartment());
                roleView.setText(worker.getRole());
                ageView.setText("Age: " + worker.getAge());
                salaryView.setText(String.valueOf( worker.getSalary()));
                debtView.setText(String.valueOf(worker.getDebt()));
                attendanceScoreView.setText(String.valueOf(worker.getAttendanceScore()));
            }
        });

        deleteBtn.setOnClickListener(v -> {
            new Thread(() -> {
            workerViewModel.delete(workerViewModel.getWorkerById(workerId));
            }).start();
            Toast.makeText(requireContext(), "Worker deleted", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}

