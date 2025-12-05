package com.example.FinalProjectATP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FinalProjectATP.model.Librarian;



@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    
}
