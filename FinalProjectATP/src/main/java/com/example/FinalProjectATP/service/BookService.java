package com.example.FinalProjectATP.service;

import com.example.FinalProjectATP.model.Book;
import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.repository.BookRepository;
import com.example.FinalProjectATP.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BookService(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public List<Book> getAllAvailableBooks() {
        return bookRepository.findAll().stream().filter(book -> !book.isBorrowed()).toList();
    }

    public List<Book> getAllBorrowedBooks() {
        return bookRepository.findAll().stream().filter(Book::isBorrowed).toList();
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Transactional
    public void borrowBook(Long bookId, Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null && !book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowDate(LocalDate.now());
            //book.setDueDate(LocalDate.now().plusWeeks(3));
            book.setDueDate(LocalDate.of(2025, 12, 9)); // overdue book hack
            book.setBorrower(borrower);

            borrower.getBasket().add(book);

            borrowerRepository.save(borrower);
            bookRepository.save(book);
        }
    }

    @Transactional
    public void returnBook(Long bookId, Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null && borrower.getBasket().contains(book)) {
            book.setBorrowed(false);
            book.setBorrowDate(null);
            book.setDueDate(null);

            borrower.getBasket().remove(book);

            bookRepository.save(book);
            borrowerRepository.save(borrower);
        }
    }
}