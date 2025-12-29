package com.example.erpproject.ViewModel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.entity.User;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.UserRepository;
import com.example.erpproject.repository.WorkerRepository;

import java.util.ArrayList;
import java.util.List;
public class WorkerViewModel extends AndroidViewModel {
    private final WorkerRepository repository;
    private final UserRepository userRepository;
    private final LiveData<List<Worker>> allWorkers;
    private final MutableLiveData<List<Worker>> filteredWorkers=new MutableLiveData<>();



    public WorkerViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkerRepository(application);
        userRepository = new UserRepository(application);
        allWorkers = repository.getAllWorkers();
    }

    // Expose LiveData to UI
    public LiveData<List<Worker>> getAllWorkers() {
        return allWorkers;
    }

    public LiveData<List<Worker>> getWorkerByDept(String dept) {
        return repository.getWorkersByDepartment(dept);
    }

    public LiveData<List<Worker>> getRecentWorkersDepartmentLive(String dept) {
        return repository.getRecentWorkersDepartmentLive(dept);
    }

    // Insert new worker
    public void insert(Worker worker, String password, boolean isadmin, String BA) {
        repository.insert(worker, password, isadmin, BA);
    }

    // Update worker
    public void update(Worker worker) {
        repository.update(worker);
    }

    // Delete worker
    public void delete(Worker worker) {
        repository.delete(worker);
    }

    // Delete all workers
    public void deleteAllWorkers() {
        repository.deleteAllWorkers();
    }

    public User getUserById(String id) {
        return userRepository.getUserByWorkerId(id);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public Salary getSalaryRecordForWorker(String workerId) {
        return repository.getSalaryRecordForWorker(workerId);
    }

    public void updateSalary(Salary salary) {
        repository.updateSalary(salary);
    }


    public int getWorkerCountByDept(String dept) {
        return repository.getWorkerCountByDepartment(dept);
    }

    public Worker getWorkerById(String id) {
        return repository.getWorkerById(id);
    }

    public LiveData<Worker> getWorkerByIdLive(String id) {
        return repository.getWorkerByIdLive(id);
    }

}
