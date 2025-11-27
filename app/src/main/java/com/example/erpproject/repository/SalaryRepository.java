package com.example.erpproject.repository;
import android.app.Application;

import androidx.lifecycle.LiveData;


import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.dao.SalaryDao;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalaryRepository {
    private WorkerDao workerDao;
    private SalaryDao salaryDao;
    private final LiveData<List<Salary>> allSalaries;
    private final ExecutorService executorService;

    public SalaryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workerDao = db.workerDao();
        salaryDao = db.salaryDao();
        allSalaries = salaryDao.getAllSalaryRecords();
        executorService = Executors.newSingleThreadExecutor(); // background tasks

    }

    public void paySalary(final String id) {
        Salary salary = salaryDao.getSalaryRecordForWorker(id);
        Worker worker = workerDao.getWorkerById(id);

        double worker_salary=salary.getEffectiveSalary();
        double salary_deduct=salary.getMonthlyDeduction();
        double debt=salary.getLoanDebt();

        debt=debt-salary_deduct;

        if(debt<=0) {
            salary.setEffectiveSalary(worker_salary);
            salary.setLoanDebt(0);
            salary.setMonthlyDeduction(0);
            salaryDao.update(salary);

            worker.setDebt(0);
            workerDao.updateWorker(worker);
        }
        else{
            salary.setLoanDebt(debt);
            salaryDao.update(salary);
            worker.setDebt(debt);
            workerDao.updateWorker(worker);
        }
    }

    public LiveData<List<Salary>> getAllSalaries() {
        return allSalaries;
    }

    public void insert(Salary salary) {
        executorService.execute(() -> salaryDao.insert(salary));
    }

    public void update(final Salary salary) {
        executorService.execute(() -> salaryDao.update(salary));
    }

    public void delete(final Salary salary) {
        executorService.execute(() -> salaryDao.delete(salary));
    }

    public void deleteAllSalaryRecords() {
        executorService.execute(() -> salaryDao.deleteAllSalaryRecords());
    }

    public Salary getSalaryRecordById(final String id) {
        return salaryDao.getSalaryRecordForWorker(id);
    }
}