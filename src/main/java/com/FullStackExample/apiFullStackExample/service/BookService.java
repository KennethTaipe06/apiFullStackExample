package com.FullStackExample.apiFullStackExample.service;

import com.FullStackExample.apiFullStackExample.dto.BookDTO;
import com.FullStackExample.apiFullStackExample.entity.Book;
import com.FullStackExample.apiFullStackExample.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    // Get all books
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get book by ID
    public Optional<BookDTO> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Create new book
    public BookDTO createBook(BookDTO bookDTO) {
        // Validate book with same title and author doesn't already exist
        if (bookRepository.existsByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())) {
            throw new RuntimeException("A book with the same title and author already exists");
        }
        
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }
    
    // Update book
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    // Check if title and author have changed and if another book exists with those details
                    if (!book.getTitle().equals(bookDTO.getTitle()) || 
                        !book.getAuthor().equals(bookDTO.getAuthor())) {
                        
                        if (bookRepository.existsByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())) {
                            throw new RuntimeException("A book with the same title and author already exists");
                        }
                    }
                    
                    // Update book properties
                    book.setTitle(bookDTO.getTitle());
                    book.setAuthor(bookDTO.getAuthor());
                    book.setPublicationYear(bookDTO.getPublicationYear());
                    book.setCategory(bookDTO.getCategory());
                    
                    Book updatedBook = bookRepository.save(book);
                    return convertToDTO(updatedBook);
                })
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
    
    // Delete book
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    // Search books by title
    public List<BookDTO> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search books by author
    public List<BookDTO> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search books by category
    public List<BookDTO> findByCategory(String category) {
        return bookRepository.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search books by publication year
    public List<BookDTO> findByPublicationYear(Integer year) {
        return bookRepository.findByPublicationYear(year)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search books by year range
    public List<BookDTO> findByYearRange(Integer startYear, Integer endYear) {
        return bookRepository.findByPublicationYearBetween(startYear, endYear)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search books by multiple criteria
    public List<BookDTO> findByMultipleCriteria(String title, String author, 
                                              String category, Integer publicationYear) {
        return bookRepository.findByMultipleCriteria(title, author, category, publicationYear)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get all categories
    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }
    
    // Get all authors
    public List<String> getAllAuthors() {
        return bookRepository.findAllAuthors();
    }
    
    // Helper methods for DTO conversion
    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getCategory()
        );
    }
    
    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book(
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getPublicationYear(),
                bookDTO.getCategory()
        );
        
        if (bookDTO.getId() != null) {
            book.setId(bookDTO.getId());
        }
        
        return book;
    }
}
