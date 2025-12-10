
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
                bookRepository.save(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling"));
                bookRepository.save(new Book("Harry Potter and the Chamber of Secrets", "J.K. Rowling"));
                bookRepository.save(new Book("Harry Potter and the Prisoner of Azkaban", "J.K. Rowling"));
                bookRepository.save(new Book("Harry Potter and the Goblet of Fire", "J.K. Rowling"));
                bookRepository.save(new Book("Harry Potter and the Order of the Phoenix", "J.K. Rowling"));
                bookRepository.save(new Book("The Hobbit", "J.R.R. Tolkien"));
                bookRepository.save(new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R. Tolkien"));
                bookRepository.save(new Book("The Lord of the Rings: The Two Towers", "J.R.R. Tolkien"));
                bookRepository.save(new Book("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien"));
                bookRepository.save(new Book("1984", "George Orwell"));
                bookRepository.save(new Book("Animal Farm", "George Orwell"));
                bookRepository.save(new Book("To Kill a Mockingbird", "Harper Lee"));
                bookRepository.save(new Book("Pride and Prejudice", "Jane Austen"));
                bookRepository.save(new Book("Sense and Sensibility", "Jane Austen"));
                bookRepository.save(new Book("Emma", "Jane Austen"));
                bookRepository.save(new Book("Moby Dick", "Herman Melville"));
                bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
                bookRepository.save(new Book("Brave New World", "Aldous Huxley"));
                bookRepository.save(new Book("Fahrenheit 451", "Ray Bradbury"));
                bookRepository.save(new Book("The Catcher in the Rye", "J.D. Salinger"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: Prince Caspian", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Voyage of the Dawn Treader", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Silver Chair", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Horse and His Boy", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Magician's Nephew", "C.S. Lewis"));
                bookRepository.save(new Book("The Chronicles of Narnia: The Last Battle", "C.S. Lewis"));
                bookRepository.save(new Book("Jane Eyre", "Charlotte Brontë"));
                bookRepository.save(new Book("Wuthering Heights", "Emily Brontë"));
                bookRepository.save(new Book("Great Expectations", "Charles Dickens"));
                bookRepository.save(new Book("David Copperfield", "Charles Dickens"));
                bookRepository.save(new Book("Oliver Twist", "Charles Dickens"));
                bookRepository.save(new Book("Les Misérables", "Victor Hugo"));
                bookRepository.save(new Book("The Hunchback of Notre-Dame", "Victor Hugo"));
                bookRepository.save(new Book("Dracula", "Bram Stoker"));
                bookRepository.save(new Book("Frankenstein", "Mary Shelley"));
                bookRepository.save(new Book("The Kite Runner", "Khaled Hosseini"));
                bookRepository.save(new Book("A Thousand Splendid Suns", "Khaled Hosseini"));
                bookRepository.save(new Book("The Alchemist", "Paulo Coelho"));
                bookRepository.save(new Book("The Pilgrimage", "Paulo Coelho"));
                bookRepository.save(new Book("The Little Prince", "Antoine de Saint-Exupéry"));
                bookRepository.save(new Book("The Odyssey", "Homer"));
                bookRepository.save(new Book("The Iliad", "Homer"));
                bookRepository.save(new Book("Crime and Punishment", "Fyodor Dostoevsky"));
                bookRepository.save(new Book("The Brothers Karamazov", "Fyodor Dostoevsky"));
                bookRepository.save(new Book("War and Peace", "Leo Tolstoy"));
                bookRepository.save(new Book("Anna Karenina", "Leo Tolstoy"));
                bookRepository.save(new Book("The Picture of Dorian Gray", "Oscar Wilde"));
                bookRepository.save(new Book("The Strange Case of Dr Jekyll and Mr Hyde", "Robert Louis Stevenson"));
                bookRepository.save(new Book("Alice's Adventures in Wonderland", "Lewis Carroll"));
                bookRepository.save(new Book("Through the Looking-Glass", "Lewis Carroll"));

                System.out.println("Sample books loaded into database.");
            }


            //save default librarian
            if (librarianRepository.count() == 0){
                librarianRepository.save(new Librarian("Billy", "Billy@librarian.com", "I am a librarian"));
            }
        };
    }
}
