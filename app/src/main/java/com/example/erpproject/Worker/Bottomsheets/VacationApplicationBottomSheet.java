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
import com.example.erpproject.data.entity.Vacation;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.VacationRepository;
import com.example.erpproject.repository.WorkerRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class VacationApplicationBottomSheet extends BottomSheetDialogFragment {
    private LocalDate  startDate,endDate;
    private String startDatestring, endDatestring;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_vacation, container, false);
        TextView worker_vacation_days = view.findViewById(R.id.worker_vacation_days);
        TextView last_vacation=view.findViewById(R.id.Last_vacation_status);
        EditText start_date_input=view.findViewById(R.id.start_date_input);
        EditText end_date_input=view.findViewById(R.id.end_date_input);
        EditText reason_input=view.findViewById(R.id.Vacation_reason_input);
        Button apply_button=view.findViewById(R.id.Apply_vacation_button);

        assert getArguments() != null;
        String workerId =getArguments().getString("worker_Id");

        VacationRepository repo = new VacationRepository(requireActivity().getApplication());
        WorkerRepository worker_repo = new WorkerRepository(requireActivity().getApplication());

        new Thread(() -> {
                Worker worker = worker_repo.getWorkerById(workerId);
                Vacation last_Vacation=repo.getLastVacationRecord(workerId);
            getActivity().runOnUiThread(() -> {
                String vacation_out = "VACATION DAYS LEFT: " + worker.getVacation_days()+"days";
                worker_vacation_days.setText(vacation_out);
                if(!(last_Vacation==null))
                {
                String last_vacation_out = "LAST vacation For "+last_Vacation.getReason()+" from "+last_Vacation.getStartDate()+" to "+last_Vacation.getEndDate()+"is"+last_Vacation.getStatus();
                last_vacation.setText(last_vacation_out);}
            });
        }).start();


        start_date_input.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select start date")
                    .build();

            picker.show(getParentFragmentManager(), "START_DATE_PICKER");

            picker.addOnPositiveButtonClickListener(selection -> {
                startDate = Instant.ofEpochMilli(selection)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                startDatestring = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                start_date_input.setText(startDatestring);
            });
        });

        // End Date picker
        end_date_input.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select end date")
                    .build();

            picker.show(getParentFragmentManager(), "END_DATE_PICKER");

            picker.addOnPositiveButtonClickListener(selection -> {
                endDate = Instant.ofEpochMilli(selection)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                endDatestring = endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                end_date_input.setText(endDatestring);
            });
        });

        // Submit button
        apply_button.setOnClickListener(v -> {
            String reason = reason_input.getText().toString();
            if (startDate == null || endDate== null || reason.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (endDate.isBefore(startDate)) {
                Toast.makeText(getContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                return;
            }
            int days = (int)ChronoUnit.DAYS.between(startDate, endDate) + 1; // inclusive

            VacationRepository.VacationCallback callback= new VacationRepository.VacationCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Vacation submitted for " + days + " days", Toast.LENGTH_SHORT).show();
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
            repo.VacationApplication(workerId,days, startDatestring, endDatestring,reason,callback);
            dismiss();
        });
        return view;
    }
}
