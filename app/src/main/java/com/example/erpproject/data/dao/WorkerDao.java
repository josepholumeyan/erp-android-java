package com.example.erpproject.data.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.Worker;

import java.util.List;

@Dao
public interface WorkerDao {
    @Insert
    void insertWorker(Worker worker);

    // Update an existing worker
    @Update
    void updateWorker(Worker worker);

    // Delete a worker
    @Delete
    void deleteWorker(Worker worker);

    // Get all workers (LiveData auto-updates UI)
    @Query("SELECT * FROM workers ORDER BY id ASC")
    LiveData<List<Worker>> getAllWorkers();

    @Query("SELECT COUNT(*) FROM workers WHERE department = :dept")
    int countWorkersByDepartment(String dept);

    // Get the most recent 5 workers by department
    @Query("SELECT * FROM workers WHERE department = :dept ORDER BY id DESC LIMIT 5")
    LiveData<List<Worker>> getRecentWorkersByDepartment(String dept);

    // Get a worker by ID
    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    Worker getWorkerById(String workerId);

    // Get a worker by ID
    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    LiveData<Worker> getWorkerByIdLive(String workerId);


    //  Delete all workers
    @Query("DELETE FROM workers")
    void deleteAllWorkers();

    @Query("DELETE FROM workers WHERE id = :workerId")
    void deleteWorkerById(String workerId);

    //Delete workers by department excluding admins by joining worker table with user table to check for admin status
    @Query("DELETE FROM workers WHERE department = :dept AND id IN (SELECT workerId FROM users WHERE isAdmin = 0)")
    void deleteWorkersByDepartmentExceptAdmins(String dept);


    //Delete workers by department
    @Query("DELETE FROM workers WHERE department = :dept")
    void deleteWorkersByDepartment(String dept);

    //get workers by department
    @Query("SELECT * FROM workers WHERE department = :dept")
    LiveData<List<Worker>> getWorkersByDepartment(String dept);
}