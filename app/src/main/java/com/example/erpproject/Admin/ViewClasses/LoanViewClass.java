package com.example.erpproject.Admin.ViewClasses;

import com.example.erpproject.data.entity.Loan;
import com.example.erpproject.data.entity.Salary;
import com.example.erpproject.data.entity.Worker;

public class LoanViewClass {

    private Worker worker;
    private int LoanId;
    private String WorkerName;
    private String WorkerId;
    private String LoanAmount;
    private String Status;
    private String EffectiveSalary;
    private String CurrentDebt;
    private String DateApplied;
    private String Reason;
    private int Duration;

    public LoanViewClass(Worker worker, Loan loan, Salary salary){

        this.LoanId = loan.getId();
        this.WorkerName = worker.getName();
        this.WorkerId = worker.getId();
        this.LoanAmount = String.valueOf(loan.getAmount());
        this.Status = loan.getStatus();
        this.EffectiveSalary = String.valueOf(salary.getEffectiveSalary());
        this.CurrentDebt = String.valueOf(salary.getLoanDebt());
        this.DateApplied = loan.getDate();
        this.Reason = loan.getReason();
        this.Duration = loan.getDuration();
    }
    //getter and setters
    public int getLoanId() {return LoanId;}
    public void setLoanId(int loanId) {LoanId = loanId;}

    public String getWorkerName() {return WorkerName;}
    public void setWorkerName(String workerName) {WorkerName = workerName;}

    public String getWorkerId() {return WorkerId;}
    public void setWorkerId(String workerId) {WorkerId = workerId;}

    public String getLoanAmount() {return LoanAmount;}
    public void setLoanAmount(String loanAmount) {LoanAmount = loanAmount;}

    public String getStatus() {return Status;}
    public void setStatus(String status) {Status = status;}

    public String getEffectiveSalary() {return EffectiveSalary;}
    public void setEffectiveSalary(String effectiveSalary) {EffectiveSalary = effectiveSalary;}

    public String getCurrentDebt() {return CurrentDebt;}
    public void setCurrentDebt(String currentDebt) {CurrentDebt = currentDebt;}

    public String getDateApplied() {return DateApplied;}
    public void setDateApplied(String dateApplied) {DateApplied = dateApplied;}

    public String getReason() {return Reason;}
    public void setReason(String reason) {Reason = reason;}

    public Worker getWorker() {return worker;}
    public void setWorker(Worker worker) {this.worker = worker;}

    public int getDuration() {return Duration;}
    public void setDuration(int duration) {Duration = duration;}
}
