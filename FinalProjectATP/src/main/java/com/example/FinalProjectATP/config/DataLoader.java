
package com.example.FinalProjectATP.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.FinalProjectATP.model.Book;
import com.example.FinalProjectATP.model.Librarian;
import com.example.FinalProjectATP.repository.BookRepository;
import com.example.FinalProjectATP.repository.LibrarianRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadBooks(BookRepository bookRepository, LibrarianRepository librarianRepository) {
        return args -> {

            if (bookRepository.count() == 0) {

                bookRepository.save(new Book("Harry Potter", "J.K. Rowling"));
                bookRepository.save(new Book("The Hobbit", "J.R.R. Tolkien"));
                bookRepository.save(new Book("1984", "George Orwell"));
                bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald"));

                System.out.println("Sample books loaded into database.");
            }


            //save default librarian
            if (librarianRepository.count() == 0){
                librarianRepository.save(new Librarian("Billy", "Billy@librarian.com", "I am a librarian"));
            }
        };
    }
}
