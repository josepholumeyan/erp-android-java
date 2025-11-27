package com.example.erpproject.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "users",
        foreignKeys = @ForeignKey(
                entity = Worker.class,       // links this user to a Worker
                parentColumns = "id",        // Worker primary key
                childColumns = "workerId",   // User references this worker id
                onDelete = CASCADE            // if worker deleted, delete user too
        )
)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String workerId;
    private String credential;
    private boolean isAdmin;    // true = admin, false = normal worker

    public User(String workerId, String credential, boolean isAdmin) {
        this.workerId = workerId;
        this.credential = credential;
        this.isAdmin = isAdmin;
    }


    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }

    public String getCredential() { return credential; }
    public void setCredential(String credential) { this.credential = credential; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
