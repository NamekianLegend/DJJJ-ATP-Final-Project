package com.example.FinalProjectATP.model;


import java.util.ArrayList;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public class Person {

    @NotBlank(message = "No Blanks ")
    @NotNull
    @Size(min = 5, message = "Name should have at least 5 characters")
    private String name;

    @NotBlank(message = "I need an email from you please and thank you! ")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Stop trying to use a bad email! >:(")
    //@NotBlank(message = "Email is required")

    @Size(max = 100, message = "Email must be less than 100 characters")
    @Email(message = "Im expecting a VALID email please and thank you!")
    private String email;

    @Size(max = 25, message = "password must be less than 25 characters")
    @NotNull
    @NotBlank(message = "Please come up with a strong password!")
    // lower than 6 characters, it no workie :D
    @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters!")
    // One upper case, one lower case
   @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$", message="Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    private ArrayList<Book> booksBorrowed = new ArrayList<>();

    Person(){};

    Person(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public ArrayList<Book> getBooksBorrowed() {
        return booksBorrowed;
    }


    public void addBook(Book book) {
        this.booksBorrowed.add(book);
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }    
}

