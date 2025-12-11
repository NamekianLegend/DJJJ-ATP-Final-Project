package com.example.FinalProjectATP.service;

import com.example.FinalProjectATP.form.BorrowerForm;
import com.example.FinalProjectATP.model.Borrower;
import com.example.FinalProjectATP.repository.BorrowerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final PasswordEncoder passwordEncoder;

    public BorrowerService(BorrowerRepository borrowerRepository, PasswordEncoder passwordEncoder) {
        this.borrowerRepository = borrowerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Borrower register(BorrowerForm form) {
        String hashedPassword = passwordEncoder.encode(form.getPassword());
        Borrower newBorrower = new Borrower(form.getName(), form.getEmail(), hashedPassword);
        return borrowerRepository.save(newBorrower);
    }


    public Borrower authenticate(String name, String password) {
        List<Borrower> borrowers = borrowerRepository.findAll();
        for (Borrower borrower : borrowers) {
            if (borrower.getName().equals(name) && passwordEncoder.matches(password, borrower.getPassword())) {
                return borrower;
            }
        }
        return null;
    }

    public Borrower getById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
    }
}
