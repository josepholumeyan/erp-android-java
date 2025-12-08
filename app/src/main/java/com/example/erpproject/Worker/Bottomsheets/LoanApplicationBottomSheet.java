package com.example.erpproject.Worker.Bottomsheets;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.erpproject.R;
import com.example.erpproject.data.entity.Loan;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.LoanRepository;
import com.example.erpproject.repository.WorkerRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LoanApplicationBottomSheet extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_loan, container, false);
        TextView salary_view = view.findViewById(R.id.worker_Salary);
        TextView debt_view = view.findViewById(R.id.worker_debt);
        TextView last_loan_view = view.findViewById(R.id.Last_loan_status);
        EditText loan_amount_input = view.findViewById(R.id.Loan_Amount_input);
        EditText duration_input = view.findViewById(R.id.Loan_period_input);
        EditText reason_input = view.findViewById(R.id.Loan_reason_input);
        Button apply_button = view.findViewById(R.id.Apply_loan_button);


        assert getArguments() != null;
        String workerId =getArguments().getString("worker_Id");

        LoanRepository repo = new LoanRepository(requireActivity().getApplication());
        WorkerRepository worker_repo = new WorkerRepository(requireActivity().getApplication());
        new Thread(() -> {
            Worker worker = worker_repo.getWorkerById(workerId);
            Loan last_loan= repo.getLastLoanRecord(workerId);
            assert getActivity() != null;
            getActivity().runOnUiThread(() -> {
                String salary_out = "SALARY: " + "$" + worker.getSalary();
                String debt_out = "DEBT: " + "$" + worker.getDebt();
                salary_view.setText(salary_out);
                debt_view.setText(debt_out);
                loan_amount_input.setText(String.valueOf(worker.getSalary() - worker.getDebt()));
                if (!(last_loan == null)) {
                String last_loan_out = "LAST LOAN OF $" + last_loan.getAmount() + " For " + last_loan.getReason() + " is "+last_loan.getStatus() ;
                last_loan_view.setText(last_loan_out);}

            });
        }).start();

        apply_button.setOnClickListener(v -> {
            String input_amount = loan_amount_input.getText().toString().trim();
            String duration = duration_input.getText().toString();
            String reason = reason_input.getText().toString();

            if (input_amount.isEmpty() || duration.isEmpty() || reason.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int amount = Integer.parseInt(input_amount);
                int duration_in_months = Integer.parseInt(duration);
            LoanRepository.LoanCallback callback= new LoanRepository.LoanCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Application sent successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    });
                }
                @Override
                public void onFailure(String message) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    });
                }
            };
            repo.LoanApplication(workerId, amount,reason,duration_in_months,callback);
            }
            catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
