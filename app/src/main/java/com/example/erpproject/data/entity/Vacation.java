package com.example.erpproject.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(tableName = "vacations",
        foreignKeys = @ForeignKey(
                entity = Worker.class,
                parentColumns = "id",
                childColumns = "workerId",
                onDelete = ForeignKey.CASCADE
        ))
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @NonNull
    @ColumnInfo(name = "workerId")
    private String workerId;
    @ColumnInfo(name = "duration(days)")
    private int duration;
    @ColumnInfo(name = "startDate")
    private String startDate;
    @ColumnInfo(name = "endDate")
    private String endDate;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "reason")
    private String reason;

    @ColumnInfo(name = "status")
    private String status;

    public Vacation(@NonNull String workerId, int duration, String startDate, String endDate,String date, String reason, String status) {
        this.workerId = workerId;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.date = date;
        this.reason = reason;
        this.status = status;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @NonNull
    public String getWorkerId() {return workerId;}
    public void setWorkerId(@NonNull String workerId) {this.workerId = workerId;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public String getStartDate() {return startDate;}
    public void setStartDate(String startDate) {this.startDate = startDate;}

    public String getEndDate() {return endDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getReason() {return reason;}
    public void setReason(String reason) {this.reason = reason;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}
