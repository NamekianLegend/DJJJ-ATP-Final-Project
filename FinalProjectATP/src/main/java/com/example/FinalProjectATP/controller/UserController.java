
package com.example.FinalProjectATP.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
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
import com.example.FinalProjectATP.repository.BookRepository;
import com.example.FinalProjectATP.repository.BorrowerRepository;
import com.example.FinalProjectATP.repository.LibrarianRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;


    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final LibrarianRepository librarianRepository;

    public UserController(BookRepository bookRepository, BorrowerRepository borrowerRepository, LibrarianRepository librarianRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.librarianRepository = librarianRepository;
    }

    //-------------------------------Login-------------------------------
    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String name, @RequestParam String password, HttpSession session, Model model) {
        List<Borrower> borrowers = borrowerRepository.findAll();
        List<Librarian> librarians = librarianRepository.findAll();


        for(Borrower borrower: borrowers){
            if(borrower.getName().equals(name)&&passwordEncoder.matches(password, borrower.getPassword())){
                session.setAttribute("loggedInBorrower", borrower);
                session.setAttribute("isLoggedIn", true);
                System.out.println("Hashed Password: "+borrower.getPassword());



                model.addAttribute("username", borrower.getName());

                // return a list of books if isBorrowed is false to the model
                model.addAttribute("availableBooks",
                bookRepository.findAll().stream().filter(book -> !book.isBorrowed()).toList());

                // return a list of books if isBorrowed is true to the model
                model.addAttribute("borrowedBooks", borrower.getBasket());
                return "home";
            }
        }

        for (Librarian librarian : librarians){
            if(librarian.getName().equals(name)&&librarian.getPassword().equals(password)){
                session.setAttribute("loggedInLibrarian", librarian);
                session.setAttribute("isLoggedIn", true);


                model.addAttribute("username", librarian.getName());

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

        String hashedPassword = passwordEncoder.encode(form.getPassword());
        Borrower newBorrower = new Borrower(form.getName(), form.getEmail(), hashedPassword);
        borrowerRepository.save(newBorrower);
        
        return "login";
    }



    //-------------------------------HOME-------------------------------
    @GetMapping("/home")
    public String loadHomePage(HttpSession session, Model model){

        if (session.getAttribute("isLoggedIn") == null)
            return "login";

        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");
        if (sessionBorrower != null) {
            Borrower borrower = borrowerRepository.findById(sessionBorrower.getId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

            // force Hibernate to initialize basket
            System.out.println("Basket Size: "+borrower.getBasket().size());

            loadBooks(model, borrower);  // ← IMPORTANT
            return "home";
        }

        Librarian librarian = (Librarian) session.getAttribute("loggedInLibrarian");
        if (librarian != null) {

            // show books for librarian
            model.addAttribute("availableBooks",
                bookRepository.findAll().stream().filter(book -> !book.isBorrowed()).toList());

            model.addAttribute("borrowedBooks",
                bookRepository.findAll().stream()
                    .filter(Book::isBorrowed)
                    .toList());

            return "home";
        }

        return "login";
    }
    //--------------------------------BORROW BOOK--------------------------------
    @PostMapping("borrow")
    public String borrowBook(@RequestParam Long bookId, HttpSession session, Model model) {
        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");

        if (sessionBorrower == null) return "login";

        Borrower borrower = borrowerRepository.findById(sessionBorrower.getId())
        .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Book book = bookRepository.findById(bookId).orElse(null);
        
        if (book != null) {
            //update book status
            book.setBorrowed(true);
            book.setBorrowDate(LocalDate.now());
            book.setDueDate(LocalDate.now().plusWeeks(3));

            // add book to borrowerbasket
            borrower.getBasket().add(book);

            //update sesison, book, borrower
            borrowerRepository.save(borrower);
            bookRepository.save(book);
            session.setAttribute("loggedInBorrower", borrower);
        }

        //reloads the books to the home page
        loadBooks(model, borrower);

        return "redirect:/home";
    }

    //--------------------------------RETURN BOOK--------------------------------
    @PostMapping("/return")
    public String returnBook(@RequestParam Long bookId, HttpSession session, Model model){
        Borrower sessionBorrower = (Borrower) session.getAttribute("loggedInBorrower");

        if (sessionBorrower == null) return "login";

        Borrower borrower = borrowerRepository.findById(sessionBorrower.getId())
        .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Book book = bookRepository.findById(bookId).orElse(null);

        if(book != null){
            if(borrower.getBasket().contains(book)){
                //update borroed status in library
                book.setBorrowed(false);
                book.setBorrowDate(null);
                book.setDueDate(null);


                //remove book from borrower
                borrower.getBasket().remove(book);

                //save borrower, book and session
                bookRepository.save(book);
                borrowerRepository.save(borrower);
                session.setAttribute("loggedInBorrower", borrower);
            }

        }

        loadBooks(model, borrower);
        return "redirect:/home";
    }

    //--------------------------------LOAD BOOKS--------------------------------
    private void loadBooks(Model model, Borrower borrower) {
        List<Book> allBooks = bookRepository.findAll();
        

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

}
