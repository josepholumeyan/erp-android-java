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


import com.example.erpproject.Admin.ViewClasses.LoanViewClass;
import com.example.erpproject.R;
import com.example.erpproject.ViewModel.LoanViewModel;
import com.example.erpproject.repository.LoanRepository;


public class LoanInfoFragment extends Fragment {
    private static final String ARG_LOAN_ID = "loan_id";

    private TextView nameView, idView, loanAmountView,statusView,dateAppliedView,reasonView, EffectiveSalaryView, debtView,durationView;

    private Button ViewWorkerBtn, ApproveLoanBtn, RejectLoanBtn;
    private LoanViewClass loanViewClass;
    private int loanId;
    private LoanViewModel loanViewModel;

    public static LoanInfoFragment newInstance(int loanId) {
        LoanInfoFragment fragment = new LoanInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOAN_ID, loanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_loan, container, false);

        nameView=view.findViewById(R.id.worker_name_tvview);
        idView=view.findViewById(R.id.worker_id_tvview);
        loanAmountView=view.findViewById(R.id.loan_amount_tvview);
        statusView=view.findViewById(R.id.status_tvview);
        dateAppliedView=view.findViewById(R.id.date_applied_tvview);
        reasonView=view.findViewById(R.id.loan_reason_tvView);
        EffectiveSalaryView=view.findViewById(R.id.effective_salary_tvview);
        debtView=view.findViewById(R.id.debt_tvview);
        durationView=view.findViewById(R.id.duration_tvView);

        ViewWorkerBtn=view.findViewById(R.id.view_worker_btn);
        ApproveLoanBtn=view.findViewById(R.id.approve_loan_btn);
        RejectLoanBtn=view.findViewById(R.id.reject_loan_btn);

        loanViewModel = new ViewModelProvider(requireActivity()).get(LoanViewModel.class);

        if (getArguments() != null) {
            loanId = getArguments().getInt(ARG_LOAN_ID);
        }
        new Thread(() -> {
            loanViewClass = loanViewModel.getSingleLoanView(loanId);
            requireActivity().runOnUiThread(() -> {
                nameView.setText(loanViewClass.getWorkerName());
                idView.setText(loanViewClass.getWorkerId());
                loanAmountView.setText(String.valueOf(loanViewClass.getLoanAmount()));
                statusView.setText(loanViewClass.getStatus());
                dateAppliedView.setText(loanViewClass.getDateApplied());
                reasonView.setText(loanViewClass.getReason());
                EffectiveSalaryView.setText(String.valueOf(loanViewClass.getEffectiveSalary()));
                debtView.setText(String.valueOf(loanViewClass.getCurrentDebt()));
                durationView.setText(String.valueOf(loanViewClass.getDuration()));
            });
        }).start();


        ViewWorkerBtn.setOnClickListener(v -> {
            WorkerInfoFragment fragment = WorkerInfoFragment.newInstance(loanViewClass.getWorkerId());

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        ApproveLoanBtn.setOnClickListener(v -> {
            LoanRepository.LoanCallback callback= new LoanRepository.LoanCallback() {
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
                loanViewModel.approveLoan(loanId, callback);
        });

        RejectLoanBtn.setOnClickListener(v -> {
            LoanRepository.LoanCallback callback= new LoanRepository.LoanCallback() {
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
            loanViewModel.rejectLoan(loanId, callback);
        });
        return view;
    }
}
