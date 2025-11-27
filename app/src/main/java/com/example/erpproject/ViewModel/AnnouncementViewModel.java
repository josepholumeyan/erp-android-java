package com.example.erpproject.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.erpproject.data.entity.Announcement;
import com.example.erpproject.repository.AnnouncementRepository;

import java.util.List;

public class AnnouncementViewModel extends AndroidViewModel {
    private final AnnouncementRepository Repository;

    public AnnouncementViewModel(@NonNull Application application) {
        super(application);
        Repository = new AnnouncementRepository(application);
    }

    public LiveData<List<Announcement>> getAllAnnouncements() {
        return Repository.getAllAnnouncementsLive();
    }

    public LiveData<List<Announcement>> getAnnouncementsByDepartment(String dept) {
        return Repository.getAnnouncementsByDepartmentLive(dept);
    }

    public LiveData<List<Announcement>> getAnnouncementsByAdminId(String AdminId) {
        return Repository.getAnnouncementsByAdminIdLive(AdminId);
    }

    public void insert(Announcement announcement) {
        Repository.insert(announcement);
    }

    public void update(Announcement announcement) {
        Repository.update(announcement);
    }

    public void delete(Announcement announcement) {
        Repository.delete(announcement);
    }

    public void deleteAllAnnouncements() {
        Repository.deleteAllAnnouncements();
    }

    public void deleteAnnouncementsByDepartment(String dept) {
        Repository.deleteAnnouncementsByDepartment(dept);
    }

    public void deleteAnnouncementsByAdminId(String AdminId) {
        Repository.deleteAnnouncementsByAdminId(AdminId);
    }
    public void deleteAnnouncementById(int id) {
        Repository.deleteAnnouncementById(id);
    }

    public Announcement getAnnouncementById(int id) {
        return Repository.getAnnouncementById(id);
    }
}
