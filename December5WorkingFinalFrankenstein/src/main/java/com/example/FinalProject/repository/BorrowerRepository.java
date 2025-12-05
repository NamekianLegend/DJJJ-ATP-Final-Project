package com.example.FinalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FinalProject.model.Borrower;



@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    
}
