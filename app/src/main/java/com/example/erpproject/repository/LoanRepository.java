package com.example.erpproject.repository;
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.dao.SalaryDao;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.data.entity.Loan;
import com.example.erpproject.data.dao.LoanDao;
import com.example.erpproject.Admin.ViewClasses.LoanViewClass;


import com.example.erpproject.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class LoanRepository {
    private WorkerDao workerDao;
    private SalaryDao salaryDao;
    private LoanDao loanDao;
    private final LiveData<List<Loan>> allLoans;
    private final ExecutorService executorService;

    public LoanRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workerDao = db.workerDao();
        salaryDao = db.salaryDao();
        loanDao = db.loanDao();
        allLoans = loanDao.getAllLoansRecords();
        executorService = Executors.newSingleThreadExecutor();
    }

    public interface LoanCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void LoanApplication(String workerId, int amount, String reason, int duration, LoanCallback callback){
        executorService.execute(() -> {
            Worker worker = workerDao.getWorkerById(workerId);
            if (worker == null) {
                callback.onFailure("Worker not found");
                return;
            }
            Salary salary = salaryDao.getSalaryRecordForWorker(workerId);
            if (salary == null) {
                callback.onFailure("Salary not found for worker");
                return;
            }
            Loan pending_loan = loanDao.getPendingLoansRecordForWorker(workerId);
            if (pending_loan != null) {
                callback.onFailure("Pending loan already exists for worker");
                return;
            }
            if (amount+salary.getLoanDebt() > salary.getBaseSalary()) {
                callback.onFailure("Total Loan amount exceeds worker's salary");
                return;
            }
            Loan loan=new Loan(workerId,amount, utils.getCurrentDate(),duration,reason,"pending");
            loanDao.insert(loan);
            callback.onSuccess();

        });
    }

    public void ApproveLoan(int loanId, LoanCallback callback){
        executorService.execute(() -> {
            Loan loan = loanDao.getLoan(loanId);
            if (loan == null) {
                callback.onFailure("Loan not found");
                return;
            }
            Worker worker = workerDao.getWorkerById(loan.getWorkerId());
            if (worker == null) {
                callback.onFailure("Worker not found");
                return;
            }
            Salary salary = salaryDao.getSalaryRecordForWorker(loan.getWorkerId());
            if (salary == null) {
                callback.onFailure("Salary not found for worker");
                return;
            }
            if (loan.getStatus().equals("approved")) {
                callback.onFailure("Loan already approved");
                return;
            }

            worker.setDebt(worker.getDebt()+loan.getAmount());
            workerDao.updateWorker(worker);
            salary.setLoanDebt(salary.getLoanDebt()+loan.getAmount());
            int mth_deduct=((int)Math.ceil((double)loan.getAmount()/loan.getDuration()));
            salary.setMonthlyDeduction(salary.getMonthlyDeduction()+mth_deduct);
            salary.setEffectiveSalary(salary.getEffectiveSalary()-mth_deduct);
            salaryDao.update(salary);
            loan.setStatus("approved");
            loanDao.update(loan);
            callback.onSuccess();
        });
    }

    public void RejectLoan(int loanId, LoanCallback callback) {
        executorService.execute(() -> {
            Loan loan = loanDao.getLoan(loanId);
            if (loan == null) {
                callback.onFailure("Loan not found");
                return;
            }
            if (loan.getStatus().equals("rejected")) {
                callback.onFailure("Loan already rejected");
                return;
            }
            loan.setStatus("rejected");
            loanDao.update(loan);
            callback.onSuccess();
        });
    }


    public LiveData<List<Loan>> getAllLoans() {
        return allLoans;
    }

    public void insert(Loan loan) {
        executorService.execute(() -> loanDao.insert(loan));
    }

    public void update(Loan loan) {
        executorService.execute(() -> loanDao.update(loan));
    }

    public void delete(Loan loan) {
        executorService.execute(() -> loanDao.delete(loan));
    }

    public void deleteAllLoanRecords() {
        executorService.execute(() -> loanDao.deleteAllLoans());
    }

    public Loan getLoanRecordById(final String id) {
        return loanDao.getLoansRecordForWorker(id);
    }

    public Loan getLastLoanRecord(final String id) {
        return loanDao.getLastLoanRecordForWorker(id);
    }

    public LiveData<List<Loan>> getPendingLoansByDepartments(final String dept){
        return loanDao.getPendingLoansByDepartment(dept);
    }

    public int getPendingLoansCountByDepartment(final String dept){
        return loanDao.getPendingLoansCountByDepartment(dept);
    }

    public LiveData<List<LoanViewClass>> getLoanViews(final String dept) {

        LiveData<List<Loan>> pendingLoansLiveData = loanDao.getPendingLoansByDepartment(dept);

        return Transformations.switchMap(pendingLoansLiveData, loans -> {
            MutableLiveData<List<LoanViewClass>> resultLiveData = new MutableLiveData<>();

            executorService.execute(() -> {
                List<LoanViewClass> loanViewClasses = new ArrayList<>();
                for (Loan loan : loans) {
                    Worker worker = workerDao.getWorkerById(loan.getWorkerId());
                    Salary salary = salaryDao.getSalaryRecordForWorker(loan.getWorkerId());
                    loanViewClasses.add(new LoanViewClass(worker, loan, salary));
                }

                resultLiveData.postValue(loanViewClasses);
            });

            return resultLiveData;
        });
    }
    public LoanViewClass getSingleLoanView(final int loanId){
        Loan loan= loanDao.getLoan(loanId);
        Worker worker = workerDao.getWorkerById(loan.getWorkerId());
        Salary salary = salaryDao.getSalaryRecordForWorker(loan.getWorkerId());
        return new LoanViewClass(worker,loan,salary);
    }

    public void deleteLoansForDepartment(final String dept) {
        executorService.execute(() -> loanDao.deleteLoansForDepartment(dept));
    }
}