package com.example.erpproject.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.erpproject.data.entity.Attendance;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.AttendanceRepository;

import java.util.List;

public class AttendanceViewModel extends AndroidViewModel {
    private final AttendanceRepository attendanceRepository;

    public AttendanceViewModel(@NonNull Application application) {
        super(application);
        attendanceRepository = new AttendanceRepository(application);
    }

    public void markAbsent(String workerId, String date, String time) {
        attendanceRepository.checkIn(workerId, date, time, "Absent");
        attendanceRepository.checkOut(workerId, date, time);
    }

    public LiveData<List<Attendance>> getAttendanceByDepartment(String dept) {
        return attendanceRepository.getAttendanceByDepartment(dept);
    }

    public LiveData<List<Worker>> getWorkersWithNoAttendanceByDept(String dept) {
        return attendanceRepository.getWorkersWithNoAttendanceByDept(dept);
    }

}
