package com.example.erpproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.erpproject.Admin.Activities.AdminLoginActivity;
import com.example.erpproject.Worker.Activities.WorkerLoginActivity;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.UserRepository;
import com.example.erpproject.repository.WorkerRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
            WorkerRepository workerRepository = new WorkerRepository(getApplication());
            UserRepository userRepository = new UserRepository(getApplication());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//            // Example worker
//            String name = "John Doe";
//            String gender = "Male";
//            int age=30;
//            int level = 1;
//            String department = "backend";
//            double debt=0;
//            double salary = 50000;
//            String role="front-end developer";
//            String password = "yourmama";
//            boolean isAdmin = true;
//
//            Worker worker = new Worker(name, gender, age, level, department, debt, salary, role,100);
//            Worker worker2 = new Worker("Joseph Smith", "male", 25, 10, "HR", 10000, 60000, "HR Manager",100);
//
//            // Insert worker and automatically create user
//        workerRepository.insert(worker,password, isAdmin,"00000000");
//        workerRepository.insert(worker2,password, true,"00000000");

        Button home_workerBtn = findViewById(R.id.home_worker);
        Button home_adminBtn =findViewById(R.id.home_admin);

        home_workerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkerLoginActivity.class);
            startActivity(intent);
        });
        home_adminBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }
}