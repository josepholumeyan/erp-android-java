package com.example.erpproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erpproject.data.entity.Announcement;

import java.util.List;

@Dao
public interface AnnouncementDao {
    @Insert
    void insert(Announcement announcement);

    @Update
    void update(Announcement announcement);

    @Delete
    void delete(Announcement announcement);

    @Query("SELECT * FROM announcements")
    List<Announcement> getAllAnnouncements();

    @Query("SELECT * FROM announcements")
    LiveData<List<Announcement>> getAllAnnouncementsLive();

    @Query("SELECT * FROM announcements WHERE dept = :dept")
    List<Announcement> getAnnouncementsByDepartment(String dept);

    @Query("SELECT * FROM announcements WHERE dept = :dept")
    LiveData<List<Announcement>> getAnnouncementsByDepartmentLive(String dept);

    @Query("SELECT * FROM announcements WHERE id = :id")
    Announcement getAnnouncementById(int id);

    @Query("DELETE FROM announcements WHERE id = :id")
    void deleteAnnouncementById(int id);

    @Query("DELETE FROM announcements")
    void deleteAllAnnouncements();

    @Query("DELETE FROM announcements WHERE dept = :dept")
    void deleteAnnouncementsByDepartment(String dept);

    @Query("SELECT* FROM announcements WHERE AdminId = :AdminId")
    List<Announcement> getAnnouncementsByAdminId(String AdminId);

    @Query("SELECT* FROM announcements WHERE AdminId = :AdminId")
    LiveData<List<Announcement>> getAnnouncementsByAdminIdLive(String AdminId);

    @Query("DELETE FROM announcements WHERE AdminId = :AdminId")
    void deleteAnnouncementsByAdminId(String AdminId);
}
