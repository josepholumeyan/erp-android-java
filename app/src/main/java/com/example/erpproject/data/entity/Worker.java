package com.example.erpproject.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "workers")
public class Worker {
    @PrimaryKey@NonNull
    private String id;

    @ColumnInfo(name = "full_name")
    private String name;

    private String gender;

    private int age;

    private int level;

    private String department;

    private double debt;

    private double salary;
    private String Role;

    private int vacation_days;
    private int daysAtWork;
    private double AttendanceScore;

    // Constructor
    public Worker(String name, String gender, int age, int level, String department, double debt, double salary,String Role,int vacation_days) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.level = level;
        this.department = department;
        this.debt = debt;
        this.salary = salary;
        this.Role=Role;
        this.vacation_days=vacation_days;
        this.daysAtWork=0;
        this.AttendanceScore=1;
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getDebt() { return debt; }
    public void setDebt(double debt) { this.debt = debt; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getRole() { return Role; }
    public void setRole(String Role) { this.Role = Role; }

    public int getVacation_days() { return vacation_days; }
    public void setVacation_days(int vacation_days) { this.vacation_days = vacation_days; }

    public int getDaysAtWork() { return daysAtWork; }
    public void setDaysAtWork(int daysAtWork) { this.daysAtWork = daysAtWork; }

    public double getAttendanceScore() { return AttendanceScore; }
    public void setAttendanceScore(double AttendanceScore) { this.AttendanceScore = AttendanceScore; }


}