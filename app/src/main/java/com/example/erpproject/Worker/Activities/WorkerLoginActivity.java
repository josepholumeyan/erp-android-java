package com.example.erpproject.Worker.Activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import com.example.erpproject.R;
import com.example.erpproject.data.entity.User;
import com.example.erpproject.repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class WorkerLoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText WorkerIdInput;
    private  EditText WorkerPasswordInput;
    private final UserRepository userRepository=new UserRepository(getApplication());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        WorkerPasswordInput = findViewById(R.id.Worker_password_input1);
        WorkerIdInput = findViewById(R.id.worker_id_input);
        loginButton = findViewById(R.id.worker_login_button);

        loginButton.setOnClickListener(v -> {
            String WorkerId = WorkerIdInput.getText().toString().trim();
            String credential = WorkerPasswordInput.getText().toString().trim();
            if (WorkerId.isEmpty()) {
                Toast.makeText(this, "Please enter both Worker ID and Pin", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
            User user = userRepository.getUserByWorkerId(WorkerId);

                runOnUiThread(() -> {
                    if (user != null) {
                        BCrypt.Result result = BCrypt.verifyer()
                                .verify(credential.toCharArray(), user.getCredential());
                        if (!result.verified) {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WorkerLoginActivity.this, WorkerDashboardActivity.class);
                        intent.putExtra("worker_Id", WorkerId);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}