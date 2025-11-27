package com.example.erpproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;



import com.example.erpproject.data.entity.Vacation;

import java.util.List;
@Dao
public interface VacationDao {
    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("DELETE FROM vacations")
    void deleteAllVacations();

    @Query("SELECT vacations.* FROM vacations " +
            "JOIN workers ON vacations.WorkerId = workers.id " +
            "WHERE workers.department = :dept")
    LiveData<List<Vacation>> getVacationsByDepartment(String dept);

    @Query("SELECT * FROM vacations WHERE date = :date")
    LiveData<List<Vacation>> getVacationsByDate(String date);
    @Query("SELECT * FROM vacations")
    LiveData<List<Vacation>> getAllLoansRecords();

    @Query("SELECT * FROM vacations WHERE workerId = :workerId")
    Vacation getVacationsRecordForWorker(String workerId);

    @Query("SELECT * FROM vacations WHERE status = 'pending'")
    LiveData<List<Vacation>> getPendingVacations();

    @Query("SELECT * FROM vacations WHERE id = :vacationId")
    Vacation getVacation(int vacationId);

    @Query("SELECT * FROM vacations WHERE workerId = :workerId ORDER BY date DESC LIMIT 1")
    Vacation getLastVacationRecordForWorker(String workerId);

    @Query("SELECT * FROM vacations WHERE workerId = :workerId AND status = 'pending'")
    Vacation getPendingVacationRecordForWorker(String workerId);

    @Query("SELECT * FROM vacations WHERE workerId = :workerId AND status = 'active'")
    Vacation getActiveVacationRecordForWorker(String workerId);
    @Query("SELECT * FROM vacations WHERE date = :date AND workerId = :workerId LIMIT 1")
    Vacation getVacationRecord(String workerId,String date);

    //get pending vacations for department
    @Query("SELECT vacations.* FROM vacations " +
            "JOIN workers ON vacations.WorkerId = workers.id " +
            "WHERE workers.department = :dept AND vacations.status = 'pending'")
    LiveData<List<Vacation>> getPendingVacationsByDepartment(String dept);

    @Query("SELECT COUNT(*) FROM vacations " +
            "JOIN workers ON vacations.WorkerId = workers.id " +
            "WHERE workers.department = :dept AND vacations.status = 'pending'")
    int getPendingVacationCountByDepartment(String dept);

    @Query("SELECT endDate FROM vacations " +
            "JOIN workers ON vacations.WorkerId = workers.id " +
            "WHERE workers.id = :workerId AND vacations.status = 'approved' " +
            "ORDER BY vacations.date DESC LIMIT 1")
    String getLastEndDateForWorkerDate(String workerId);

    @Query("DELETE FROM vacations WHERE workerId IN (SELECT id FROM workers WHERE department = :dept)")
    void deleteVacationsForDepartment(String dept);
}
