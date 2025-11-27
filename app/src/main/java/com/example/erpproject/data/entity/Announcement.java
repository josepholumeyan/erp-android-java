package com.example.erpproject.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "announcements")
public class Announcement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private String date;
    private String dept;
    private String AdminId;
    private String AdminName;

    public Announcement(String title, String content, String date, String dept, String AdminId, String AdminName) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.dept = dept;
        this.AdminId = AdminId;
        this.AdminName = AdminName;
    }

    // Getters and setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getDept() {return dept;}
    public void setDept(String dept) {this.dept = dept;}

    public String getAdminId() {return AdminId;}
    public void setAdminId(String AdminId) {this.AdminId = AdminId;}

    public String getAdminName() {return AdminName;}
    public void setAdminName(String AdminName) {this.AdminName = AdminName;}
}
