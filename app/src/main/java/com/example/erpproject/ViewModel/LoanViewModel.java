package com.example.erpproject.ViewModel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.erpproject.Admin.ViewClasses.LoanViewClass;
import com.example.erpproject.data.entity.Loan;


import com.example.erpproject.repository.LoanRepository;

import java.util.List;

public class LoanViewModel extends AndroidViewModel {
    public final LoanRepository Repository;
    public LoanViewModel(@NonNull Application application) {
        super(application);
        Repository = new LoanRepository(application);
    }

    public LiveData<List<Loan>> getAllLoans() {
        return Repository.getAllLoans();
    }

    public LiveData<List<Loan>> getPendingLoansByDept(String dept) {
        return Repository.getPendingLoansByDepartments(dept);
    }
    public int getPendingLoansCountByDept(String dept) {
        return Repository.getPendingLoansCountByDepartment(dept);
    }

    public LiveData<List<LoanViewClass>> getLoanViews(final String dept){
        return Repository.getLoanViews(dept);
    }
    public void approveLoan(int loanId, LoanRepository.LoanCallback callback){
        Repository.ApproveLoan(loanId,callback);
    }

    public void rejectLoan(int loanId, LoanRepository.LoanCallback callback){
        Repository.RejectLoan(loanId,callback);
    }

    public void deleteLoansForDepartment(final String dept) {
        Repository.deleteLoansForDepartment(dept);
    }

    public LoanViewClass getSingleLoanView(final int loanId){
        return Repository.getSingleLoanView(loanId);
    }
}
