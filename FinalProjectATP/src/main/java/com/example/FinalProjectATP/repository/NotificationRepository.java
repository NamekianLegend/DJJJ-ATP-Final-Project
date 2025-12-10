package com.example.FinalProjectATP.repository;

import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.model.Librarian;
import com.example.FinalProjectATP.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // used to find notifications that are tied to the borrower given as the argument
    List<Notification> findByBorrower(Borrower borrower);
}
