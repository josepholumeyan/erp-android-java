package com.example.erpproject.repository;
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.example.erpproject.Admin.ViewClasses.LoanViewClass;
import com.example.erpproject.Admin.ViewClasses.VacationViewClass;
import com.example.erpproject.data.dao.VacationDao;
import com.example.erpproject.data.dao.WorkerDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.Loan;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.entity.Vacation;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationRepository {
    private WorkerDao workerDao;
    private VacationDao vacationDao;
    private LiveData<List<Vacation>> allVacations;
    private final ExecutorService executorService;
    public VacationRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workerDao = db.workerDao();
        vacationDao = db.vacationDao();
        allVacations = vacationDao.getAllLoansRecords();
        executorService = Executors.newSingleThreadExecutor();
    }
    public interface VacationCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void VacationApplication(String workerId, int duration,String start_date,String end_date, String reason, VacationRepository.VacationCallback callback){
        executorService.execute(() -> {
            Worker worker = workerDao.getWorkerById(workerId);
            if (worker == null) {
                callback.onFailure("Worker not found");
                return;
            }
            Vacation pending_vacation = vacationDao.getPendingVacationRecordForWorker(workerId);
            if (pending_vacation != null) {
                callback.onFailure("Pending Vacation request already exists for worker");
                return;
            }
            Vacation active_vacation = vacationDao.getActiveVacationRecordForWorker(workerId);
            if (active_vacation != null) {
                callback.onFailure("Worker already on a vacation");
                return;
            }
            if (duration > worker.getVacation_days()) {
                callback.onFailure("Exceeded Vacation days");
                return;
            }
            Vacation vacation=new Vacation(workerId,duration,start_date,end_date, utils.getCurrentDate(),reason,"pending");
            vacationDao.insert(vacation);
            callback.onSuccess();

        });
    }

    public void RejectVacation(int vacationId, VacationRepository.VacationCallback callback) {
        executorService.execute(() -> {
            Vacation vacation = vacationDao.getVacation(vacationId);
            if (vacation == null) {
                callback.onFailure("Vacation not found");
                return;
            }
            if (vacation.getStatus().equals("rejected")) {
                callback.onFailure("Vacation already rejected");
                return;
            }
            vacation.setStatus("rejected");
            vacationDao.update(vacation);
            callback.onSuccess();
        });
    }

    public void ApproveVacation(int vacationId, VacationRepository.VacationCallback callback){
        executorService.execute(() -> {
            Vacation vacation = vacationDao.getVacation(vacationId);
            if (vacation == null) {
                callback.onFailure("Vacation not found");
                return;
            }
            Worker worker = workerDao.getWorkerById(vacation.getWorkerId());
            if (worker == null) {
                callback.onFailure("Worker not found");
                return;
            }
            if (vacation.getStatus().equals("approved")) {
                callback.onFailure("Vacation already approved");
                return;
            }

            worker.setVacation_days(worker.getVacation_days()-vacation.getDuration());
            workerDao.updateWorker(worker);
            vacation.setStatus("approved");
            vacationDao.update(vacation);
            callback.onSuccess();
        });
    }


    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public void insert(Vacation vacation) {
        executorService.execute(() -> vacationDao.insert(vacation));
    }

    public void update(Vacation vacation) {
        executorService.execute(() -> vacationDao.update(vacation));
    }

    public void delete(Vacation vacation) {
        executorService.execute(() -> vacationDao.delete(vacation));
    }

    public void deleteAllVacationRecords() {
        executorService.execute(() -> vacationDao.deleteAllVacations());
    }

    public Vacation getVacationRecordById(final String id) {
        return vacationDao.getVacationsRecordForWorker(id);
    }

    public Vacation getLastVacationRecord(final String id) {
        return vacationDao.getLastVacationRecordForWorker(id);
    }

    public LiveData<List<Vacation>> getPendingVacationsByDepartments(final String dept){
        return vacationDao.getPendingVacationsByDepartment(dept);
    }

    public int getPendingVacationsCountByDepartment(final String dept){
        return  vacationDao.getPendingVacationCountByDepartment(dept);
    }

    public String getLastEndDateForWorkerDate(final String workerId){
        return vacationDao.getLastEndDateForWorkerDate(workerId);
    }

    public LiveData<List<VacationViewClass>> getVacationViews(final String dept) {

        LiveData<List<Vacation>> pendingVacationsLiveData = vacationDao.getPendingVacationsByDepartment(dept);

        return Transformations.switchMap(pendingVacationsLiveData, vacations -> {
            MutableLiveData<List<VacationViewClass>> resultLiveData = new MutableLiveData<>();

            executorService.execute(() -> {
                List<VacationViewClass> VacationViewClasses = new ArrayList<>();
                for (Vacation vacation : vacations) {
                    Worker worker = workerDao.getWorkerById(vacation.getWorkerId());
                    String last_vacation_date = getLastEndDateForWorkerDate(worker.getId());
                    VacationViewClasses.add(new VacationViewClass(worker,vacation,last_vacation_date));
                }
                resultLiveData.postValue(VacationViewClasses);
            });
            return resultLiveData;
        });
    }

    public VacationViewClass getSingleVacationView(final int vacationId){
        Vacation vacation= vacationDao.getVacation(vacationId);
        Worker worker = workerDao.getWorkerById(vacation.getWorkerId());
        String last_vacation_date = getLastEndDateForWorkerDate(worker.getId());
        return new VacationViewClass(worker,vacation,last_vacation_date);
    }

    public void deleteVacationsForDepartment(final String dept) {
        executorService.execute(() -> vacationDao.deleteVacationsForDepartment(dept));
    }
}
