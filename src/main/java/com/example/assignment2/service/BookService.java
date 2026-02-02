package com.example.assignment2.service;

import com.example.assignment2.model.Book;
import com.example.assignment2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer
 * Contains business logic and acts as an intermediary between Controller and Repository
 * This is where you would add validation, business rules, etc.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor injection (recommended approach)
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Get all books from the library
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get a specific book by ID
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Create a new book
     * Business logic: Validate that book has required fields
     */
    public Book createBook(Book book) {
        // Business validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }

        // Set default values if not provided
        if (book.getAvailable() == null) {
            book.setAvailable(true);
        }

        return bookRepository.save(book);
    }

    /**
     * Update an existing book
     */
    public Book updateBook(Long id, Book book) {
        // Business logic: Check if book exists before updating
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }

        // Validate updated data
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }

        return bookRepository.update(id, book);
    }

    /**
     * Delete a book
     */
    public boolean deleteBook(Long id) {
        // Business logic: Check if book exists before deleting
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }
        return bookRepository.deleteById(id);
    }

    /**
     * Search books by author
     */
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        return bookRepository.findByAuthor(author);
    }

    /**
     * Get all available books
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    /**
     * Mark a book as borrowed (business logic)
     */
    public Book borrowBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }

        Book book = bookOpt.get();
        if (!book.getAvailable()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        book.setAvailable(false);
        return bookRepository.update(id, book);
    }

    /**
     * Return a borrowed book (business logic)
     */
    public Book returnBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }

        Book book = bookOpt.get();
        if (book.getAvailable()) {
            throw new IllegalStateException("Book is already available");
        }

        book.setAvailable(true);
        return bookRepository.update(id, book);
    }
}
