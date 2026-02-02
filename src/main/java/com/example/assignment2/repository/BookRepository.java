package com.example.assignment2.repository;

import com.example.assignment2.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository Layer
 * Manages data storage and retrieval using in-memory mock data
 * In a real application, this would interact with a database
 */
@Repository
public class BookRepository {

    // In-memory storage using HashMap
    private Map<Long, Book> bookDatabase = new HashMap<>();

    // Auto-increment ID generator
    private AtomicLong idGenerator = new AtomicLong(4);

    // Constructor initializes mock data
    public BookRepository() {
        initializeMockData();
    }

    /**
     * Initialize mock data - simulates existing books in the library
     */
    private void initializeMockData() {
        bookDatabase.put(1L, new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", 1925, true));
        bookDatabase.put(2L, new Book(2L, "To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", 1960, true));
        bookDatabase.put(3L, new Book(3L, "1984", "George Orwell", "978-0-452-28423-4", 1949, false));
    }

    /**
     * Retrieve all books
     */
    public List<Book> findAll() {
        return new ArrayList<>(bookDatabase.values());
    }

    /**
     * Find a book by ID
     */
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(bookDatabase.get(id));
    }

    /**
     * Save a new book (Create)
     */
    public Book save(Book book) {
        if (book.getId() == null) {
            // Generate new ID for new book
            book.setId(idGenerator.getAndIncrement());
        }
        bookDatabase.put(book.getId(), book);
        return book;
    }

    /**
     * Update an existing book
     */
    public Book update(Long id, Book book) {
        if (bookDatabase.containsKey(id)) {
            book.setId(id);
            bookDatabase.put(id, book);
            return book;
        }
        return null;
    }

    /**
     * Delete a book by ID
     */
    public boolean deleteById(Long id) {
        if (bookDatabase.containsKey(id)) {
            bookDatabase.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Check if a book exists
     */
    public boolean existsById(Long id) {
        return bookDatabase.containsKey(id);
    }

    /**
     * Find books by author (bonus method for searching)
     */
    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : bookDatabase.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    /**
     * Find available books
     */
    public List<Book> findAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : bookDatabase.values()) {
            if (book.getAvailable()) {
                result.add(book);
            }
        }
        return result;
    }
}
