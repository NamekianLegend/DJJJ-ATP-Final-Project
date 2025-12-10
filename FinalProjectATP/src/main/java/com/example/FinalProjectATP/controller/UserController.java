
package com.example.FinalProjectATP.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.FinalProjectATP.model.Book;
import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.repository.BookRepository;
import com.example.FinalProjectATP.repository.BorrowerRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public UserController(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    // Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        List<Borrower> borrowers = borrowerRepository.findAll();
        for(Borrower borrower: borrowers){
            if(borrower.getEmail().equals(email)&&borrower.getPassword().equals(password)){
                model.addAttribute("username", borrower.getName());

                // return a list of books if isBorrowed is false to the model
                model.addAttribute("availableBooks",
                bookRepository.findAll().stream().filter(book -> !book.isBorrowed()).toList());

                // return a list of books if isBorrowed is true to the model
                model.addAttribute("borrowedBooks",
                bookRepository.findAll().stream()
                    .filter(Book::isBorrowed)
                    .toList());

                return "home";
            }
        }

        return "login";
    }

    //Borrow book
    @PostMapping("borrow")
    public String borrowBook(@RequestParam Long bookId, Model model) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.setBorrowed(true);
            book.setBorrowDate(LocalDate.now());
            book.setDueDate(LocalDate.now().plusWeeks(3));

            //update book with new isBorrowed value
            bookRepository.save(book);
        }


        //makes sure to return to the the home page istead of having them sign in again
        model.addAttribute("username", "User");

        //reloads the books to the home page
        loadBooks(model);

        return "home";
    }


    @PostMapping("/return")
    public String returnBook(@RequestParam Long bookId, Model model){
        Book book = bookRepository.findById(bookId).orElse(null);

        if(book != null){
            book.setBorrowed(false);
            book.setBorrowDate(null);
            book.setDueDate(null);
            bookRepository.save(book);
        }

        model.addAttribute("username", "User");
        loadBooks(model);


        return "home";
    }

    //loads books into the available and unavialable books slot in home
    private void loadBooks(Model model) {
        List<Book> allBooks = bookRepository.findAll();
        model.addAttribute("availableBooks",
                allBooks.stream().filter(book -> !book.isBorrowed()).toList());
        model.addAttribute("borrowedBooks",
                allBooks.stream().filter(Book::isBorrowed).toList());
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("username", "User");
        loadBooks(model);
        return "home";
    }
    

    // Show register page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Handle register form submission
    @PostMapping("/register")
    public String processRegister(@Valid @RequestParam String name,
            @Valid @RequestParam String email,
            @Valid @RequestParam String password, @Valid @RequestParam String confirm,
            Model model) {
        if(password.equals(confirm)){
            Borrower tempBorrower = new Borrower(name,email,password);
            
            borrowerRepository.save(tempBorrower);
            System.out.println("\nUSER REGISTRATION");
            tempBorrower.displayDetails();
            return "login";
        }
        
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // This clears all session attributes
        return "redirect:/login"; // Redirect to login page
    }

}
