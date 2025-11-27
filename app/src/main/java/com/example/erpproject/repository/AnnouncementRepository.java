package com.example.erpproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.erpproject.data.dao.AnnouncementDao;
import com.example.erpproject.data.database.AppDatabase;
import com.example.erpproject.data.entity.Announcement;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AnnouncementRepository {
    private final ExecutorService executorService;
    private final AnnouncementDao announcementDao;

    public AnnouncementRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        announcementDao = db.announcementDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Announcement announcement) {
        executorService.execute(() -> announcementDao.insert(announcement));
    }
    public void update(Announcement announcement) {
        executorService.execute(() -> announcementDao.update(announcement));
    }

    public void delete(Announcement announcement) {
        executorService.execute(() -> announcementDao.delete(announcement));
    }

    public void deleteAllAnnouncements() {
        executorService.execute(announcementDao::deleteAllAnnouncements);
    }

    public void deleteAnnouncementsByDepartment(String dept) {
        executorService.execute(() -> announcementDao.deleteAnnouncementsByDepartment(dept));
    }

    public void deleteAnnouncementsByAdminId(String AdminId) {
        executorService.execute(() -> announcementDao.deleteAnnouncementsByAdminId(AdminId));
    }
    public void deleteAnnouncementById(int id) {
        executorService.execute(() -> announcementDao.deleteAnnouncementById(id));
    }

    public Announcement getAnnouncementById(int id) {
        return announcementDao.getAnnouncementById(id);
    }
    public List<Announcement> getAllAnnouncements() {
        return announcementDao.getAllAnnouncements();
    }

    public LiveData<List<Announcement>> getAllAnnouncementsLive() {
        return announcementDao.getAllAnnouncementsLive();
    }

    public List<Announcement> getAnnouncementsByAdminId(String AdminId) {
        return announcementDao.getAnnouncementsByAdminId(AdminId);
    }

    public LiveData<List<Announcement>> getAnnouncementsByDepartmentLive(String dept) {
        return announcementDao.getAnnouncementsByDepartmentLive(dept);
    }

    public LiveData<List<Announcement>> getAnnouncementsByAdminIdLive(String AdminId) {
        return announcementDao.getAnnouncementsByAdminIdLive(AdminId);
    }


}
