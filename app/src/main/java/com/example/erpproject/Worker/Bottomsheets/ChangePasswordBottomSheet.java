package com.example.erpproject.Worker.Bottomsheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.erpproject.R;
import com.example.erpproject.repository.UserRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangePasswordBottomSheet extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_changepassword, container, false);
        EditText oldPassword = view.findViewById(R.id.old_password_input);
        EditText newPassword = view.findViewById(R.id.new_password_input);
        EditText confirmPassword = view.findViewById(R.id.confirm_password_input);
        Button changePassword = view.findViewById(R.id.change_password_button);


        String workerid=getArguments().getString("worker_Id");
        UserRepository repo=new UserRepository(requireActivity().getApplication());


        changePassword.setOnClickListener(v -> {
                String oldPass=oldPassword.getText().toString();
                String newPass=newPassword.getText().toString();
                String confirmPass=confirmPassword.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!newPass.equals(confirmPass)){
                    Toast.makeText(requireActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                repo.changePassword(workerid, oldPass, newPass, new UserRepository.ChangePasswordCallback() {
                    @Override
                    public void onSuccess() {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                            dismiss();
                        });
                    }
                    @Override
                    public void onFailure(String message) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
        });
        return view;
    }
}