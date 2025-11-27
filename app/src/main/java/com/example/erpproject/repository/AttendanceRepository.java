package com.example.erpproject.repository;
import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.erpproject.data.dao.AttendanceDao;
import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.Attendance;
import com.example.erpproject.data.entity.Worker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class AttendanceRepository {
private final AttendanceDao attendanceDao;
private final WorkerDao workerDao;
private final ExecutorService executorService;

    public AttendanceRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        attendanceDao = database.attendanceDao();
        workerDao=database.workerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void checkIn(String workerId, String date, String time,String status) {
        executorService.execute(() -> {
            Attendance att = attendanceDao.getAttendanceRecord(workerId, date);

            if (att == null) {
                Attendance attendance = new Attendance(workerId, date, status, time, null);
                attendanceDao.insert(attendance);
                double score;
                if (status.equals("Absent")) {
                    score = 0;
                }
                else if (status.equals("Late")) {
                    score = 0.5;
                }
                else {
                    score = 1;
                }
                Worker worker = workerDao.getWorkerById(workerId);
                worker.setAttendanceScore((worker.getAttendanceScore()*worker.getDaysAtWork()+score)/(worker.getDaysAtWork()+1));
                workerDao.updateWorker(worker);
                ;
            }
        });
    }

    public void checkOut(String workerId, String date, String time) {
        executorService.execute(() -> {
            Attendance att = attendanceDao.getAttendanceRecord(workerId, date);

            if (att != null) {
                att.setCheckOutTime(time);
                attendanceDao.update(att);
            }
        });
    }

    public LiveData<List<Attendance>> getAttendanceByDepartment(String dept) {
        return attendanceDao.getAttendanceByDepartment(dept);
    }

    public LiveData<List<Attendance>> getAttendanceByDate(String date) {
        return attendanceDao.getAttendanceByDate(date);
    }

    public Attendance getAttendanceRecordForWorker(String workerId) {
        return attendanceDao.getAttendanceRecordForWorker(workerId);
    }

    public LiveData<List<Worker>> getWorkersWithNoAttendanceByDept(String dept) {
        return attendanceDao.getWorkersWithNoAttendance(dept);
    }
}