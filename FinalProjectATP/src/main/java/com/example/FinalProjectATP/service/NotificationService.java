package com.example.FinalProjectATP.service;

import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.model.Notification;
import com.example.FinalProjectATP.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification getFirstNotification(Borrower borrower) {
        List<Notification> notifications = notificationRepository.findByBorrower(borrower);
        if (!notifications.isEmpty()) {
            return notifications.getFirst();
        }
        return null;
    }

    public void dismissNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public List<Notification> getNotificationList(Borrower borrower){
        return notificationRepository.findByBorrower(borrower);
    }
}