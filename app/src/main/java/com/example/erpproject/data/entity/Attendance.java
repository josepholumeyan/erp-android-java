package com.example.erpproject.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "attendance",
        foreignKeys = @ForeignKey(
                entity = Worker.class,
                parentColumns = "id",
                childColumns = "workerId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Attendance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "workerId")
    @NonNull
    private String workerId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "checkInTime")
    private String checkInTime;

    @ColumnInfo(name = "checkOutTime")
    private String checkOutTime;


    public Attendance(@NonNull String workerId, String date, String status,
                      String checkInTime, String checkOutTime) {
        this.workerId = workerId;
        this.date = date;
        this.status = status;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getWorkerId() { return workerId; }
    public void setWorkerId(@NonNull String workerId) { this.workerId = workerId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}
