package com.FullStackExample.apiFullStackExample.controller;

import com.FullStackExample.apiFullStackExample.dto.BookDTO;
import com.FullStackExample.apiFullStackExample.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@Tag(name = "Books", description = "API for library book management")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    @Operation(summary = "Get all books", 
               description = "Returns a list of all books registered in the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of books retrieved successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<BookDTO> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", 
               description = "Returns a specific book based on its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookDTO> getBookById(
            @Parameter(description = "Unique ID of the book", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<BookDTO> book = bookService.getBookById(id);
            return book.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Create new book", 
               description = "Creates a new book in the library with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or book already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createBook(
            @Parameter(description = "Book data to create", required = true)
            @Valid @RequestBody BookDTO bookDTO) {
        try {
            BookDTO newBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal server error");
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update book", 
               description = "Updates an existing book's data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateBook(
            @Parameter(description = "Unique ID of the book", required = true, example = "1")
            @PathVariable Long id, 
            @Parameter(description = "New book data", required = true)
            @Valid @RequestBody BookDTO bookDTO) {
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal server error");
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", 
               description = "Permanently deletes a book from the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "Unique ID of the book", required = true, example = "1")
            @PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal server error");
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search books by multiple criteria", 
               description = "Search for books using any combination of title, author, category, or publication year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BookDTO>> searchBooks(
            @Parameter(description = "Book title (partial match, case insensitive)")
            @RequestParam(required = false) String title,
            @Parameter(description = "Book author (partial match, case insensitive)")
            @RequestParam(required = false) String author,
            @Parameter(description = "Book category (exact match)")
            @RequestParam(required = false) String category,
            @Parameter(description = "Publication year")
            @RequestParam(required = false) Integer publicationYear,
            @Parameter(description = "Start year for range search")
            @RequestParam(required = false) Integer startYear,
            @Parameter(description = "End year for range search")
            @RequestParam(required = false) Integer endYear) {
        
        try {
            List<BookDTO> books;
            
            // If year range is provided, use that search method
            if (startYear != null && endYear != null) {
                books = bookService.findByYearRange(startYear, endYear);
            } 
            // Otherwise use the multiple criteria search
            else {
                books = bookService.findByMultipleCriteria(title, author, category, publicationYear);
            }
            
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all book categories", 
               description = "Returns a list of all unique book categories in the library")
    public ResponseEntity<List<String>> getAllCategories() {
        try {
            List<String> categories = bookService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/authors")
    @Operation(summary = "Get all book authors", 
               description = "Returns a list of all unique book authors in the library")
    public ResponseEntity<List<String>> getAllAuthors() {
        try {
            List<String> authors = bookService.getAllAuthors();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
