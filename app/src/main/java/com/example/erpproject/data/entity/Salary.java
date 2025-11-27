package com.example.erpproject.data.entity;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="Salary",
        foreignKeys = @ForeignKey(
                entity = Worker.class,
                parentColumns = "id",
                childColumns = "workerId",
                onDelete = ForeignKey.CASCADE
        ))
public class Salary {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "workerId")
    private String workerId;

    @ColumnInfo(name = "base_salary")
    private double baseSalary;

    @ColumnInfo(name = "effective_salary")
    private double effectiveSalary;

    @ColumnInfo(name = "loan debt")
    private double loanDebt;

    @ColumnInfo(name = "monthly deduction")
    private double monthlyDeduction;

    @ColumnInfo(name = "bank_account")
    private String bankAccount;




    public Salary(@NonNull String workerId, double baseSalary,String bankAccount,double effectiveSalary, double loanDebt, double monthlyDeduction) {
        this.workerId = workerId;
        this.baseSalary = baseSalary;
        this.bankAccount = bankAccount;
        this.effectiveSalary = effectiveSalary;
        this.loanDebt = loanDebt;
        this.monthlyDeduction = monthlyDeduction;
    }

    //Getters and Setters

    @NonNull
    public String getWorkerId() {return workerId;}
    public void setWorkerId(@NonNull String worker_id) {this.workerId = worker_id;}

    public double getBaseSalary() {return baseSalary;}
    public void setBaseSalary(double base_salary) {this.baseSalary = base_salary;}

    public double getEffectiveSalary() {return effectiveSalary;}
    public void setEffectiveSalary(double effective_salary) {this.effectiveSalary = effective_salary;}

    public double getLoanDebt() {return loanDebt;}
    public void setLoanDebt(double loan_debt) {this.loanDebt = loan_debt;}

    public String getBankAccount() {return bankAccount;}
    public void setBankAccount(String bank_account) {this.bankAccount = bank_account;}

    public double getMonthlyDeduction() {return monthlyDeduction;}
    public void setMonthlyDeduction(double monthly_deduction) {this.monthlyDeduction = monthly_deduction;}


}
