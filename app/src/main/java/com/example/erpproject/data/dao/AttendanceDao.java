package com.example.erpproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.Attendance;
import com.example.erpproject.data.entity.Worker;

import java.util.List;
@Dao
public interface AttendanceDao {
    @Insert
    void insert(Attendance attendance);

    @Update
    void update(Attendance attendance);

    @Delete
    void delete(Attendance attendance);

    @Query("DELETE FROM attendance")
    void deleteAllAttendance();

    @Query("SELECT attendance.* FROM attendance " +
            "JOIN workers ON attendance.WorkerId = workers.id " +
            "WHERE workers.department = :dept")
    LiveData<List<Attendance>> getAttendanceByDepartment(String dept);

    @Query("SELECT * FROM attendance WHERE date = :date")
    LiveData<List<Attendance>> getAttendanceByDate(String date);

    @Query("SELECT * FROM attendance WHERE workerId = :workerId")
    Attendance getAttendanceRecordForWorker(String workerId);

    @Query("SELECT * FROM attendance WHERE date = :date AND workerId = :workerId LIMIT 1")
    Attendance getAttendanceRecord(String workerId,String date);


    @Query("SELECT workers.* FROM workers " +
            "LEFT JOIN attendance ON workers.id = attendance.workerId " +
            "WHERE attendance.workerId IS NULL AND workers.department = :dept")
    LiveData<List<Worker>> getWorkersWithNoAttendance(String dept);




}