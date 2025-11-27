package com.example.erpproject.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.User;

@Dao
public interface UserDao {
    // Insert a new user (worker login info)
    @Insert
    void insertUser(User user);


    @Query("SELECT * FROM users WHERE workerId = :workerId")
    User getUserByWorkerId(String workerId);

    @Query("SELECT * FROM users WHERE workerId = :workerId AND credential = :credential")
    User getUserByCredentials(String workerId, String credential);
    @Update
    void updateUser(User user);
    @Query("UPDATE users SET isAdmin = 1 WHERE workerId = :workerId")
    void promoteToAdmin(int workerId);
}