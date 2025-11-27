package com.example.erpproject.Admin.Activities;
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

public class AdminLoginActivity extends AppCompatActivity {
    private Button loginButton;
    private UserRepository userRepository=new UserRepository(getApplication());
    private EditText AdminIdInput,AdminPasswordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        AdminIdInput = findViewById(R.id.admin_id_input);
        AdminPasswordInput = findViewById(R.id.admin_passsword_input);
        loginButton = findViewById(R.id.admin_login_button);

        loginButton.setOnClickListener(v -> {
            String AdminId = AdminIdInput.getText().toString().trim();
            String credential = AdminPasswordInput.getText().toString().trim();
            if (AdminId.isEmpty() || credential.isEmpty()) {
                Toast.makeText(this, "Please enter both Admin ID and Password", Toast.LENGTH_SHORT).show();
                return;
            }  new Thread(() -> {
                User user = userRepository.getUserByWorkerId(AdminId);

                runOnUiThread(() -> {
                    if (user != null) {
                        BCrypt.Result result = BCrypt.verifyer()
                                .verify(credential.toCharArray(), user.getCredential());
                        if (!result.verified) {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!user.isAdmin()) {
                            Toast.makeText(this, "You do not have admin access", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                        intent.putExtra("Admin_Id", AdminId);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}
