package com.example.erpproject.data.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Loans",
        foreignKeys = @ForeignKey(
            entity = Worker.class,
            parentColumns = "id",
            childColumns = "workerId",
            onDelete = ForeignKey.CASCADE
))
public class Loan {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "workerId")
    private String workerId;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name="Reason")
    private String reason;



    public Loan(@NonNull String workerId, int amount, String date, int duration, String reason, String status) {
        this.workerId = workerId;
        this.amount = amount;
        this.date = date;
        this.duration = duration;
        this.reason = reason;
        this.status = status;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @NonNull
    public String getWorkerId() {return workerId;}
    public void setWorkerId(@NonNull String workerId) {this.workerId = workerId;}

    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public String getReason() {return reason;}
    public void setReason(String reason) {this.reason = reason;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}
