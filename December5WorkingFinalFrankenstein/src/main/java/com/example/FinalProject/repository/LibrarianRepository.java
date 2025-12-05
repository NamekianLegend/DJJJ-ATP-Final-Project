package com.example.FinalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FinalProject.model.Librarian;



@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    
}