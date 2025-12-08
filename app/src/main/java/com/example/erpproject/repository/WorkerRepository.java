package com.example.erpproject.repository;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.erpproject.data.dao.UserDao;
import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.User;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.dao.SalaryDao;


import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class WorkerRepository {
    private final WorkerDao workerDao;
    private final UserDao userDao;
    private final SalaryDao salaryDao;
    private final LiveData<List<Worker>> allWorkers;
    private final ExecutorService executorService;

    public WorkerRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workerDao = db.workerDao();
        userDao = db.userDao();
        salaryDao = db.salaryDao();
        allWorkers = workerDao.getAllWorkers();
        executorService = Executors.newSingleThreadExecutor(); // background tasks
    }

    public LiveData<List<Worker>> getAllWorkers() {
        return allWorkers;
    }

    public void insert(final Worker worker,String password,Boolean isadmin,String BA) {
        executorService.execute(() -> {
            int year = Calendar.getInstance().get(Calendar.YEAR) % 100;

            // Get department code
            String deptPrefix = worker.getDepartment().length() >= 2
                    ? worker.getDepartment().substring(0, 2).toUpperCase()
                    : worker.getDepartment().toUpperCase();
            // Count existing workers in this department
            int count = workerDao.countWorkersByDepartment(worker.getDepartment()) + 1;
            // Format count with leading zero if needed
            String countStr = count < 10 ? "0" + count : String.valueOf(count);
            // Combine to form ID
            String id = year + deptPrefix + countStr;
            while (true){
                Worker oldWorker=workerDao.getWorkerById(id);
                if(oldWorker==null)
                {
                    worker.setId(id);
                    break;
                }
                else{
                    count++;
                    countStr = count < 10 ? "0" + count : String.valueOf(count);
                    id = year + deptPrefix + countStr;
                }
            }
            workerDao.insertWorker(worker);
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        User user = new User(id, hashed,isadmin);
        userDao.insertUser(user);
        Salary salary = new Salary(id, worker.getSalary(), BA,worker.getSalary(),0,0);
        salaryDao.insert(salary);
        });
    }

    public Salary getSalaryRecordForWorker(final String workerId) {
        return salaryDao.getSalaryRecordForWorker(workerId);
    }

    public void updateSalary(final Salary salary) {
        salaryDao.update(salary);
    }


    public void update(final Worker worker) {
        executorService.execute(() -> workerDao.updateWorker(worker));
    }

    public void delete(final Worker worker) {
        executorService.execute(() -> workerDao.deleteWorker(worker));
    }

    public void deleteAllWorkers() {
        executorService.execute(workerDao::deleteAllWorkers);
    }

    public Worker getWorkerById(final String id) {
        return workerDao.getWorkerById(id);
    }

    public LiveData<Worker> getWorkerByIdLive(final String id) {
        return workerDao.getWorkerByIdLive(id);
    }


    public LiveData<List<Worker>> getWorkersByDepartment(final String department) {
        return workerDao.getWorkersByDepartment(department);
    }

    public LiveData<List<Worker>> getRecentWorkersDepartmentLive(final String department) {
        return workerDao.getRecentWorkersByDepartment(department);
    }

    public int getWorkerCountByDepartment(final String department) {
        return workerDao.countWorkersByDepartment(department);
    }
    public void deleteWorkerById(final String id) {
        workerDao.deleteWorkerById(id);
    }
    public void deleteWorkerByDepartment(final String department) {
        workerDao.deleteWorkersByDepartment(department);
    }

    public void deleteWorkersByDepartmentExceptAdmins(final String department) {
        workerDao.deleteWorkersByDepartmentExceptAdmins(department);
    }

}