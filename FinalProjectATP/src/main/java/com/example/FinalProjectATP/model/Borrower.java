
package com.example.FinalProjectATP.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PostPersist;

@Entity
public class Borrower extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String username;

    // list for what the user has bought
    @ManyToMany
    @JoinTable(
        name = "borrower_basket",
        joinColumns = @JoinColumn(name = "borrower_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> basket;


    public Borrower(){};


    public Borrower(String name, String email, String password){
        super(name,email,password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

        public String getUsername() {
        return username;
    }



    

    public List<Book> getBasket() {
            return basket;
        }


        public void setBasket(List<Book> basket) {
            this.basket = basket;
        }


    @PostPersist //call after saved in db so can make username based off of id
    public void setUsername() {
        this.username = "B"+getId();
    }

    public void displayDetails(){
        System.out.println("Email: "+getEmail());
        System.out.println("Name: "+getName());
        System.out.println("Password: "+getPassword());
        System.out.println("ID: "+getId());
        System.out.println("Username: "+getUsername());
    }

    
}
