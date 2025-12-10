package com.example.FinalProjectATP.config;

import com.example.FinalProjectATP.model.Book;
import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.model.Notification;
import com.example.FinalProjectATP.repository.BookRepository;
import com.example.FinalProjectATP.repository.BorrowerRepository;
import com.example.FinalProjectATP.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledDueDateChecker {
    private final BorrowerRepository borrowerRepository;
    private final NotificationRepository notificationRepository;

    public ScheduledDueDateChecker(BorrowerRepository borrowerRepository, NotificationRepository notificationRepository) {
        this.borrowerRepository = borrowerRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional // makes it so the database connection doesnt close
    @Scheduled(fixedRate = 10000) // every 10 seconds (for testing)
    //@Scheduled(fixedRate = 300000) // every 5 minutes
    public void checkingOverdueBooks(){
        LocalDate currentDate = LocalDate.now();
        List<Borrower> borrowers = borrowerRepository.findAll();
        for(Borrower borrower: borrowers){
            // get list of books from each borrower
            List <Book> bookList = borrower.getBasket();

            // if book.duedate is greater than today's date
            for(Book book : bookList){
                if(currentDate.isAfter(book.getDueDate())){
                    // create a new notification in the database
                    String overdueMessage = "Book overdue: " + book.getTitle();
                    Notification notification = new Notification(overdueMessage, borrower);
                    notificationRepository.save(notification);
                    System.out.println("Overdue book found");
                }
            }
        }
    }

}
