package com.example.erpproject.data.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.erpproject.data.dao.AnnouncementDao;
import com.example.erpproject.data.dao.AttendanceDao;
import com.example.erpproject.data.dao.LoanDao;
import com.example.erpproject.data.dao.SalaryDao;
import com.example.erpproject.data.dao.UserDao;
import com.example.erpproject.data.dao.VacationDao;
import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.entity.Announcement;
import com.example.erpproject.data.entity.Attendance;
import com.example.erpproject.data.entity.Loan;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.entity.User;
import com.example.erpproject.data.entity.Vacation;
import com.example.erpproject.data.entity.Worker;

@Database(entities = {Worker.class, User.class, Attendance.class, Salary.class, Loan.class, Vacation.class, Announcement.class},version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Abstract method for each DAO
    public abstract WorkerDao workerDao();
    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
    public abstract SalaryDao salaryDao();
    public abstract LoanDao loanDao();
    public abstract VacationDao vacationDao();
    public abstract AnnouncementDao announcementDao();

    // Singleton to prevent multiple instances
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "erp_database")
                    .fallbackToDestructiveMigration() // resets DB if schema changes
                    .build();
        }
        return instance;
    }
}
