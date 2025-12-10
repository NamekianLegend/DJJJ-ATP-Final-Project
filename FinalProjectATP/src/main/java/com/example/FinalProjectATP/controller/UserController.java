
package com.example.FinalProjectATP.controller;

import java.time.LocalDate;
import java.util.List;


import com.example.FinalProjectATP.service.BookService;
import com.example.FinalProjectATP.service.BorrowerService;
import com.example.FinalProjectATP.service.LibrarianService;
import com.example.FinalProjectATP.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.FinalProjectATP.form.BorrowerForm;
import com.example.FinalProjectATP.model.Book;
import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.model.Librarian;
import com.example.FinalProjectATP.model.Notification;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;


    private final BorrowerService borrowerService;
    private final LibrarianService librarianService;
    private final BookService bookService;
    private final NotificationService notificationService;

    public UserController(BorrowerService borrowerService, LibrarianService librarianService, BookService bookService, NotificationService notificationService) {
        this.borrowerService = borrowerService;
        this.librarianService = librarianService;
        this.bookService = bookService;
        this.notificationService = notificationService;
    }


    //-------------------------------Login-------------------------------
    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String name, @RequestParam String password, HttpSession session, Model model) {

        // authenticate borrower login
        Borrower borrower = borrowerService.authenticate(name, password);

        // if borrower authenticated successfully, establish session attributes
        if (borrower != null){
            session.setAttribute("loggedInBorrower", borrower);
            session.setAttribute("isLoggedIn", true);
            System.out.println("Hashed Password: "+borrower.getPassword());
            model.addAttribute("username", borrower.getName());

            // return a list of books if isBorrowed is false to the model
            model.addAttribute("availableBooks", bookService.getAllAvailableBooks());

            // return a list of books if isBorrowed is true to the model
            model.addAttribute("borrowedBooks", borrower.getBasket());
            return "home";
        }


        // authenticate librarian login
        Librarian librarian = librarianService.authenticate(name, password);

        // if librarian authenticated successfully, establish session attributes
        if(librarian != null){
            session.setAttribute("loggedInLibrarian", librarian);
            session.setAttribute("isLoggedIn", true);


            model.addAttribute("username", librarian.getName());

            // return a list of books if isBorrowed is false to the model
            model.addAttribute("availableBooks", bookService.getAllAvailableBooks());

            // return a list of books if isBorrowed is true to the model
            model.addAttribute("borrowedBooks", bookService.getAllBorrowedBooks());

            return "home";
        }

        return "login";
    }

    //-------------------------------REGISTER-------------------------------
    // Show register page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("form", new BorrowerForm());
        return "register";
    }


    // Handle register form submission
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("form") BorrowerForm form,
                                  BindingResult bindingResult,
                                  Model model) {

        if (!form.getPassword().equals(form.getConfirm())) {
            model.addAttribute("passwordError", "Passwords do not match.");
            bindingResult.rejectValue("confirm", "userForm Error -> Confirm Password", "Passwords do not match");
            return "register";
        }

        if(bindingResult.hasErrors()){
            return "register";
        }

        borrowerService.register(form);

        return "login";
    }



    //-------------------------------HOME-------------------------------
    @GetMapping("/home")
    public String loadHomePage(HttpSession session, Model model){

        if (session.getAttribute("isLoggedIn") == null)
            return "login";

        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");
        if (sessionBorrower != null) {
            Borrower borrower = borrowerService.getById(sessionBorrower.getId());

            // force Hibernate to initialize basket
            System.out.println("Basket Size: "+borrower.getBasket().size());

            loadBooks(model, borrower);  // ← IMPORTANT

            checkForNotifications(model, borrower); // checks for any overdue notifications when the homepage loads

            return "home";
        }

        Librarian librarian = (Librarian) session.getAttribute("loggedInLibrarian");
        if (librarian != null) {

            // show books for librarian
            model.addAttribute("availableBooks", bookService.getAllAvailableBooks());

            model.addAttribute("borrowedBooks", bookService.getAllBorrowedBooks());

            return "home";
        }

        return "login";
    }
    //--------------------------------BORROW BOOK--------------------------------
    @PostMapping("borrow")
    public String borrowBook(@RequestParam Long bookId, HttpSession session, Model model) {
        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");

        if (sessionBorrower == null) return "login";

        bookService.borrowBook(bookId, sessionBorrower.getId());

        Borrower borrower = borrowerService.getById(sessionBorrower.getId());
        session.setAttribute("loggedInBorrower", borrower);

        //reloads the books to the home page
        loadBooks(model, borrower);

        return "redirect:/home";
    }

    //--------------------------------RETURN BOOK--------------------------------
    @PostMapping("/return")
    public String returnBook(@RequestParam Long bookId, HttpSession session, Model model){
        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");

        if (sessionBorrower == null) return "login";

        bookService.returnBook(bookId, sessionBorrower.getId());

        Borrower borrower = borrowerService.getById(sessionBorrower.getId());
        session.setAttribute("loggedInBorrower", borrower);

        loadBooks(model, borrower);
        return "redirect:/home";
    }

    //--------------------------------LOAD BOOKS--------------------------------
    private void loadBooks(Model model, Borrower borrower) {
        List<Book> allBooks = bookService.getAllBooks();
        

        model.addAttribute("username", borrower.getName());

        //show all avialble books
        model.addAttribute("availableBooks",
                allBooks.stream().filter(book -> !book.isBorrowed()).toList());
 
        model.addAttribute("borrowedBooks", borrower.getBasket());
    }
 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // This clears all session attributes
        return "redirect:/login"; // Redirect to login page
    }

    //--------------------------------NOTIFICATION--------------------------------
    private void checkForNotifications(Model model, Borrower borrower){
        // checks the database of notifications, seeing if the borrower passed in has any messages tied to them
        List<Notification> notifications = notificationService.getNotificationList(borrower);

        // if the system found anything
        if(!notifications.isEmpty()){
            // grab the notification
            Notification alert = notifications.getFirst();

            // add the alert message to the data model
            model.addAttribute("overdueAlert", alert.getMessage());

            // put the id in the model so we know which alert to delete when the user hits ok
            model.addAttribute("notificationId", alert.getId());
        }
    }

    @PostMapping("/dismissNotification")
    public String dismissNotification(@RequestParam Long notificationId){
        notificationService.dismissNotification(notificationId);
        return "redirect:/home";
    }
}
