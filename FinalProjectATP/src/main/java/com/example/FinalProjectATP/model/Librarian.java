package com.example.FinalProjectATP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;



@Entity
public class Librarian extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;


    public Librarian(){}

    public Librarian(String name, String email, String password){
        super(name, email, password);

        username = "L"+id;
        
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

    @PostPersist //call after saved in db so can make username based off of id
    public void setUsername() {
        this.username = "L"+getId();
    }

}
