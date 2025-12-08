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
import com.example.erpproject.data.entity.Worker;

import java.util.Objects;

public class AddWorkerFragment extends Fragment {

    private static final String ARG_ADMIN_ID = "Admin_Id";
    private EditText nameInput, genderInput,ageInput,salaryInput,passwordInput,BAInput, departmentInput, roleInput;
    private CheckBox isAdminBox;
    private Button createButton, cancelButton;
    private WorkerViewModel workerViewModel;
    private String AdminId,AdminDept;

    public static AddWorkerFragment newInstance(String AdminId) {
        AddWorkerFragment fragment = new AddWorkerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADMIN_ID, AdminId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate = convert XML → View object on screen
        return inflater.inflate(R.layout.fragment_add_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind XML views to Java variables
        nameInput = view.findViewById(R.id.worker_name_input);
        departmentInput = view.findViewById(R.id.worker_department_input);
        roleInput = view.findViewById(R.id.worker_role_input);
        genderInput = view.findViewById(R.id.worker_gender_input);
        salaryInput = view.findViewById(R.id.worker_salary_input);
        passwordInput = view.findViewById(R.id.worker_password_input);
        BAInput= view.findViewById(R.id.bank_account_input);
        ageInput = view.findViewById(R.id.worker_age_input);
        isAdminBox = view.findViewById(R.id.is_admin_checkBox);

        createButton = view.findViewById(R.id.admin_create_worker_btn);
        cancelButton = view.findViewById(R.id.cancel_btn);

        // Get the ViewModel from the activity using this fragment
        workerViewModel = new ViewModelProvider(requireActivity()).get(WorkerViewModel.class);
        if (getArguments() != null) {
            AdminId = getArguments().getString(ARG_ADMIN_ID);
        }
        else{
            Toast.makeText(requireContext(), "Admin ID not found", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }

        new Thread(() -> {
            Worker Admin= workerViewModel.getWorkerById(AdminId);
            getActivity().runOnUiThread(() -> {
                AdminDept= Admin.getDepartment();
                if (!Objects.equals(AdminDept, "HR")){
                    departmentInput.setText(AdminDept);
                    departmentInput.setEnabled(false);
                    isAdminBox.setEnabled(false);
                }
            });
        }).start();


        // When CREATE button is clicked:
        createButton.setOnClickListener(v -> {

            // Get typed values
            String name = nameInput.getText().toString().trim();
            String BankAccount = BAInput.getText().toString().trim();
            String dept = departmentInput.getText().toString().trim();
            String role = roleInput.getText().toString().trim();
            String gender = genderInput.getText().toString().trim();

            int age;
            int salary;
            if (salaryInput.getText().toString().trim().isEmpty()||ageInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }try{
            age = Integer.parseInt(ageInput.getText().toString().trim());
            salary = Integer.parseInt(salaryInput.getText().toString().trim());}
            catch (NumberFormatException e){
                Toast.makeText(getContext(), "Invalid salary or age", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = passwordInput.getText().toString().trim();
            boolean isAdmin = isAdminBox.isChecked();

            // Quick validation
            if (Objects.equals(AdminDept, "HR")) {

                if (name.isEmpty() || BankAccount.isEmpty() || dept.isEmpty() || role.isEmpty() || gender.isEmpty() || password.isEmpty() || age == 0 || salary == 0) {
                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Worker object
                Worker worker = new Worker(name, gender, age, 1, dept, 0, salary, role, 30);

                // Insert into DB (Room)
                workerViewModel.insert(worker, password, isAdmin, BankAccount);

                Toast.makeText(getContext(), "Worker added", Toast.LENGTH_SHORT).show();

                // Close this fragment (go back to list)
                requireActivity().getSupportFragmentManager().popBackStack();
            }
            else{
                if (name.isEmpty() || BankAccount.isEmpty() ||role.isEmpty() || gender.isEmpty() || password.isEmpty() || age == 0 || salary == 0) {
                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Worker worker = new Worker(name, gender, age, 1, AdminDept, 0, salary, role, 30);
                workerViewModel.insert(worker, password, false, BankAccount);

                Toast.makeText(getContext(), "Worker added", Toast.LENGTH_SHORT).show();

                // Close this fragment (go back to list)
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });
        // Cancel button → just close fragment
        cancelButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}