package com.FullStackExample.apiFullStackExample.repository;

import com.FullStackExample.apiFullStackExample.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find books by title (containing the string, case insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // Find books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // Find books by category
    List<Book> findByCategory(String category);
    
    // Find books by publication year
    List<Book> findByPublicationYear(Integer publicationYear);
    
    // Find books by year range
    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);
    
    // Check if book exists by title and author
    boolean existsByTitleAndAuthor(String title, String author);
    
    // Custom query to search by multiple criteria
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:publicationYear IS NULL OR b.publicationYear = :publicationYear)")
    List<Book> findByMultipleCriteria(
            @Param("title") String title,
            @Param("author") String author,
            @Param("category") String category,
            @Param("publicationYear") Integer publicationYear);
    
    // Get all unique categories
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.category IS NOT NULL ORDER BY b.category")
    List<String> findAllCategories();
    
    // Get all unique authors
    @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author")
    List<String> findAllAuthors();
}
