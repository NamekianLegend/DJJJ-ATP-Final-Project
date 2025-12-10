package com.example.FinalProjectATP.service;

import com.example.FinalProjectATP.model.Librarian;
import com.example.FinalProjectATP.repository.LibrarianRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibrarianService {

    private final LibrarianRepository librarianRepository;

    public LibrarianService(LibrarianRepository librarianRepository) {
        this.librarianRepository = librarianRepository;
    }

    public Librarian authenticate(String name, String password) {
        List<Librarian> librarians = librarianRepository.findAll();
        for (Librarian librarian : librarians) {
            if (librarian.getName().equals(name) && librarian.getPassword().equals(password)) {
                return librarian;
            }
        }
        return null;
    }
}
