package com.example.FinalProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    String title;
    String author;
    String borrowDate;
    boolean isBorrowed = false;
    String dueDate;

    public Book(){};

    public Book(String title, String author){
        this.author = author;
        this.title = title;
    }


    //Getters and Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getBorrowDate() { return borrowDate; }

    public String getDueDate() { return dueDate; }

    public boolean isBorrowed() { return isBorrowed; }

    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    //for thymeleaf
    public String getAuthor() {
        return author;
    }

}
