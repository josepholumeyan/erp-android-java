package com.example.erpproject.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.erpproject.Admin.ViewClasses.VacationViewClass;
import com.example.erpproject.data.entity.Vacation;
import com.example.erpproject.data.entity.Worker;
import com.example.erpproject.repository.LoanRepository;
import com.example.erpproject.repository.VacationRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    public final VacationRepository Repository;;

    public VacationViewModel(@NonNull Application application) {
        super(application);
        Repository = new VacationRepository(application);
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return Repository.getAllVacations();
    }

    public LiveData<List<Vacation>> getPendingVacationsByDept(String dept) {
        return Repository.getPendingVacationsByDepartments(dept);
    }
    public int getPendingVacationCountByDept(String dept) {
        return Repository.getPendingVacationsCountByDepartment(dept);
    }
    public LiveData<List<VacationViewClass>> getVacationViews(final String dept){
        return Repository.getVacationViews(dept);
    }

    public VacationViewClass getSingleVacationView(final int vacationId){
        return Repository.getSingleVacationView(vacationId);
    }

    public void deleteVacationsForDepartment(final String dept) {
        Repository.deleteVacationsForDepartment(dept);
    }

    public void approveVacation(int vacationId, VacationRepository.VacationCallback callback){
        Repository.ApproveVacation(vacationId,callback);
    }

    public void rejectVacation(int vacationId, VacationRepository.VacationCallback callback){
        Repository.RejectVacation(vacationId,callback);
    }
}
