package com.example.erpproject.repository;
import android.app.Application;
import android.widget.Toast;

import com.example.erpproject.data.dao.UserDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor(); // runs DB calls off the main thread
    }

    // Insert a new user asynchronously
    public void insert(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public User getUserByWorkerId(String workerId) {
        return userDao.getUserByWorkerId(workerId);
    };

    // Login check (returns user if credentials correct)
    public User getUserByCredentials(String workerId, String credential) {
        return userDao.getUserByCredentials(workerId, credential);
    }

    public void promoteToAdmin(int workerId) {
        executorService.execute(() -> userDao.promoteToAdmin(workerId));
    }

    public void updateUser(User user) {
        executorService.execute(() -> userDao.updateUser(user));
    }

    public interface ChangePasswordCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void changePassword(String workerId,String oldPassword, String newPassword,ChangePasswordCallback callback) {
        executorService.execute(() -> {
            User user = userDao.getUserByWorkerId(workerId);

            if (user==null){
                callback.onFailure("User not found");
                return;
            }
            BCrypt.Result result = BCrypt.verifyer()
                    .verify(oldPassword.toCharArray(), user.getCredential());
            if (!result.verified) {
                callback.onFailure("Incorrect old password");
                return;
            }
            String hashed = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
            user.setCredential(hashed);
            userDao.updateUser(user);
            callback.onSuccess();

        });
    }

}
