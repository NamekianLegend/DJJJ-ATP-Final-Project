package com.example.FinalProject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.FinalProject.model.Book;
import com.example.FinalProject.repository.BookRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadBooks(BookRepository bookRepository) {
        return args -> {

            if (bookRepository.count() == 0) {

                bookRepository.save(new Book("Harry Potter", "J.K. Rowling"));
                bookRepository.save(new Book("The Hobbit", "J.R.R. Tolkien"));
                bookRepository.save(new Book("1984", "George Orwell"));
                bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald"));

                System.out.println("✅ Sample books loaded into database.");
            }
        };
    }
}
