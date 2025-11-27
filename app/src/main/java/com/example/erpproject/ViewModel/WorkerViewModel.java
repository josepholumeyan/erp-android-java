package com.example.erpproject.ViewModel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.WorkerRepository;

import java.util.List;
public class WorkerViewModel extends AndroidViewModel {
    private final WorkerRepository repository;
    private final LiveData<List<Worker>> allWorkers;

    public WorkerViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkerRepository(application);
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
    public void insert(Worker worker,String password,boolean isadmin,String BA) {
        repository.insert(worker,password,isadmin,BA);
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

    public int getWorkerCountByDept(String dept) {
        return repository.getWorkerCountByDepartment(dept);
    }

    public Worker getWorkerById(String id) {
        return repository.getWorkerById(id);
    }
    public LiveData<Worker> getWorkerByIdLive(String id) {return repository.getWorkerByIdLive(id);}
}
