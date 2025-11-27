package com.example.erpproject.Admin.ViewClasses;

import com.example.erpproject.data.entity.Vacation;
import com.example.erpproject.data.entity.Worker;

public class VacationViewClass {
    private Worker worker;
    private int VacationId;
    private String WorkerName;
    private String WorkerId;
    private String VacationDuration;
    private String Status;
    private String VacationDaysLeft;
    private String LastVacation;
    private String DateApplied;
    private String Reason;

    public VacationViewClass(Worker worker, Vacation vacation,String last_vacation_date){
        this.VacationId = vacation.getId();
        this.WorkerName = worker.getName();
        this.WorkerId = worker.getId();
        this.VacationDuration = String.valueOf(vacation.getDuration());
        this.Status = vacation.getStatus();
        this.VacationDaysLeft = String.valueOf(worker.getVacation_days());
        this.LastVacation = last_vacation_date;
        this.DateApplied = vacation.getDate();
        this.Reason = vacation.getReason();
    }
    //getters and setters
    public int getVacationId() {return VacationId;}
    public void setVacationId(int vacationId) {VacationId = vacationId;}

    public String getWorkerName() {return WorkerName;}
    public void setWorkerName(String workerName) {WorkerName = workerName;}

    public String getWorkerId() {return WorkerId;}
    public void setWorkerId(String workerId) {WorkerId = workerId;}

    public String getVacationDuration() {return VacationDuration;}
    public void setVacationDuration(String vacationDuration) {VacationDuration = vacationDuration;}

    public String getStatus() {return Status;}
    public void setStatus(String status) {Status = status;}

    public String getVacationDaysLeft() {return VacationDaysLeft;}
    public void setVacationDaysLeft(String vacationDaysLeft) {VacationDaysLeft = vacationDaysLeft;}

    public String getLastVacation() {return LastVacation;}
    public void setLastVacation(String lastVacation) {LastVacation = lastVacation;}

    public String getDateApplied() {return DateApplied;}
    public void setDateApplied(String dateApplied) {DateApplied = dateApplied;}

    public String getReason() {return Reason;}
    public void setReason(String reason) {Reason = reason;}


}
