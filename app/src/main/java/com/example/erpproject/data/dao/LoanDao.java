package com.example.erpproject.data.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.Loan;

import java.util.List;

@Dao
public interface LoanDao {
    @Insert
    void insert(Loan loan);

    @Update
    void update(Loan loan);

    @Delete
    void delete(Loan loan);

    @Query("DELETE FROM loans")
    void deleteAllLoans();

    @Query("SELECT Loans.* FROM Loans " +
            "JOIN workers ON Loans.WorkerId = workers.id " +
            "WHERE workers.department = :dept")
    LiveData<List<Loan>> getLoansByDepartment(String dept);

    //get pending loans by department
    @Query("SELECT Loans.* FROM Loans " +
            "JOIN workers ON Loans.WorkerId = workers.id " +
            "WHERE workers.department = :dept AND Loans.status = 'pending'")
    LiveData<List<Loan>> getPendingLoansByDepartment(String dept);


    @Query("SELECT * FROM Loans WHERE date = :date")
    LiveData<List<Loan>> getLoansByDate(String date);
    @Query("SELECT * FROM Loans")
    LiveData<List<Loan>> getAllLoansRecords();

    @Query("SELECT * FROM Loans WHERE workerId = :workerId")
    Loan getLoansRecordForWorker(String workerId);

    @Query("SELECT * FROM Loans WHERE status = 'pending'")
    LiveData<List<Loan>> getPendingLoans();

    @Query("SELECT * FROM Loans WHERE id = :loanId")
    Loan getLoan(int loanId);

    @Query("SELECT * FROM Loans WHERE workerId = :workerId ORDER BY date DESC LIMIT 1")
    Loan getLastLoanRecordForWorker(String workerId);

    @Query("SELECT * FROM Loans WHERE workerId = :workerId AND status = 'pending'")
    Loan getPendingLoansRecordForWorker(String workerId);
    @Query("SELECT * FROM Loans WHERE date = :date AND workerId = :workerId LIMIT 1")
    Loan getLoanRecord(String workerId,String date);

//    get count of pending loans by department
    @Query("SELECT COUNT(*) FROM Loans " +
            "JOIN workers ON Loans.WorkerId = workers.id " +
            "WHERE workers.department = :dept AND Loans.status = 'pending'")
    int getPendingLoansCountByDepartment(String dept);

// delete loans for a department
    @Query("DELETE FROM Loans WHERE workerId IN (SELECT id FROM workers WHERE department = :dept)")
    void deleteLoansForDepartment(String dept);
}
