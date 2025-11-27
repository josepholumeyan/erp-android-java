package com.example.erpproject.data.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.Salary;

import java.util.List;

@Dao
public interface SalaryDao {
    @Insert
    void insert(Salary salary );

    @Update
    void update(Salary salary);

    @Delete
    void delete(Salary salary);

    @Query("DELETE FROM Salary")
    void deleteAllSalaryRecords();

    @Query("SELECT Salary.* FROM Salary " +
            "JOIN workers ON Salary.workerId = workers.id " +
            "WHERE workers.department = :dept")
    LiveData<List<Salary>> getLoansByDepartment(String dept);

    @Query("SELECT * FROM Salary")
    LiveData<List<Salary>> getAllSalaryRecords();

    @Query("SELECT * FROM Salary WHERE workerId = :workerId")
    Salary getSalaryRecordForWorker(String workerId);

}
