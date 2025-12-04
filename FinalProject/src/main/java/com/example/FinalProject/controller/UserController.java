package com.example.FinalProject.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.FinalProject.model.Book;
import com.example.FinalProject.repository.BookRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    private final BookRepository bookRepository;

    public UserController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String user, @RequestParam String password, Model model) {

        model.addAttribute("username", user);

        // return a list of books if isBorrowed is false to the model
        model.addAttribute("availableBooks",
                bookRepository.findAll().stream()
                        .filter(book -> !book.isBorrowed())
                        .toList());

        // return a list of books if isBorrowed is true to the model
        model.addAttribute("borrowedBooks",
                bookRepository.findAll().stream()
                        .filter(Book::isBorrowed)
                        .toList());

        return "home";
    }

    //Borrow book
    @PostMapping("borrow")
    public String borrowBook(@RequestParam Long bookId, Model model) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.setBorrowed(true);

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

    // Show register page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Handle register form submission
    @PostMapping("/register")
    public String processRegister(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {
        // For testing: just echo back the name
        model.addAttribute("username", name);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // This clears all session attributes
        return "redirect:/login"; // Redirect to login page
    }

}
