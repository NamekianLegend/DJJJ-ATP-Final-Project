package com.example.FinalProject.model;

public class Librarian extends Person{
    private Library library;

    Librarian(String fName, String lName, String password, Library library){
        super(fName, lName, password);

        
        /*copying the reference meaning all librarians see
        the same thing even if they change somehtning*/
        this.library = library; 
    }

    public void addBook(Book book){
        library.addBook(book);
    }

    public void removeBook(Book book){
        library.removeBook(book);
    }
    
}
