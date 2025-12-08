package com.example.erpproject.Admin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.erpproject.R;
import com.example.erpproject.ViewModel.WorkerViewModel;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.entity.User;
import com.example.erpproject.data.entity.Worker;

import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UpdateWorkerFragment extends Fragment {
    private static final String ARG_ADMIN_ID = "Admin_Id";
    private static final String ARG_WORKER_ID = "worker_id";
    private EditText nameInput, genderInput,ageInput,salaryInput,passwordInput,BAInput, departmentInput, roleInput;
    private CheckBox isAdminBox;
    private Button updateButton, cancelButton;
    private WorkerViewModel workerViewModel;
    private String AdminId,AdminDept,WorkerId;
    private Worker worker;
    private Worker Admin;
    private User user;
    private Salary salary;

    public static UpdateWorkerFragment newInstance(String AdminId, String workerId)  {
        UpdateWorkerFragment fragment = new UpdateWorkerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADMIN_ID, AdminId);
        args.putString(ARG_WORKER_ID, workerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = view.findViewById(R.id.worker_name_input);
        departmentInput = view.findViewById(R.id.worker_department_input);
        roleInput = view.findViewById(R.id.worker_role_input);
        genderInput = view.findViewById(R.id.worker_gender_input);
        salaryInput = view.findViewById(R.id.worker_salary_input);
        passwordInput = view.findViewById(R.id.worker_password_input);
        BAInput= view.findViewById(R.id.bank_account_input);
        ageInput = view.findViewById(R.id.worker_age_input);
        isAdminBox = view.findViewById(R.id.is_admin_checkBox);

        updateButton = view.findViewById(R.id.admin_Update_worker_btn);
        cancelButton = view.findViewById(R.id.cancel_btn);

        workerViewModel = new ViewModelProvider(requireActivity()).get(WorkerViewModel.class);
        if (getArguments() != null) {
            AdminId = getArguments().getString(ARG_ADMIN_ID);
            WorkerId = getArguments().getString(ARG_WORKER_ID);
        }
        else{
            Toast.makeText(requireContext(), "Admin or Worker ID not found", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }

        new Thread(() -> {
            worker = workerViewModel.getWorkerById(WorkerId);
            Admin= workerViewModel.getWorkerById(AdminId);
            user = workerViewModel.getUserById(WorkerId);
            salary = workerViewModel.getSalaryRecordForWorker(WorkerId);
            assert getActivity() != null;
            getActivity().runOnUiThread(() -> {
                AdminDept= Admin.getDepartment();
                if (!Objects.equals(AdminDept, "HR")){
                    departmentInput.setText(AdminDept);
                    departmentInput.setEnabled(false);
                    isAdminBox.setEnabled(false);
                }
            });
        }).start();

        updateButton.setOnClickListener(v -> {
            if (!nameInput.getText().toString().trim().isEmpty()){
                worker.setName(nameInput.getText().toString().trim());
            }
            if (!BAInput.getText().toString().trim().isEmpty()){
                salary.setBankAccount(BAInput.getText().toString().trim());
            }
            if (!departmentInput.getText().toString().trim().isEmpty()){
                worker.setDepartment(departmentInput.getText().toString().trim());
            }
            if (!roleInput.getText().toString().trim().isEmpty()){
                worker.setRole(roleInput.getText().toString().trim());
            }
            if (!genderInput.getText().toString().trim().isEmpty()){
                worker.setGender(genderInput.getText().toString().trim());
            }
            if (!salaryInput.getText().toString().trim().isEmpty()){
                try {
                    worker.setSalary(Integer.parseInt(salaryInput.getText().toString().trim()));
                    salary.setBaseSalary(Integer.parseInt(salaryInput.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid salary", Toast.LENGTH_SHORT).show();
                }
            }
            if (!ageInput.getText().toString().trim().isEmpty()){
                try {
                    worker.setAge(Integer.parseInt(ageInput.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid age", Toast.LENGTH_SHORT).show();
                }
            }
            if (!passwordInput.getText().toString().trim().isEmpty()){
                String hashed = BCrypt.withDefaults().hashToString(12, passwordInput.getText().toString().trim().toCharArray());
                user.setCredential(hashed);
            }
            if (isAdminBox.isChecked()){
                user.setAdmin(true);
            }
            new Thread(() -> {
            workerViewModel.update(worker);
            workerViewModel.updateSalary(salary);
            workerViewModel.updateUser(user);
            }).start();
            Toast.makeText(requireContext(), "Worker updated", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        cancelButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}
