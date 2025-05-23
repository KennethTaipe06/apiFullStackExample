package com.FullStackExample.apiFullStackExample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Schema(description = "Data Transfer Object for books")
public class BookDTO {
    
    @Schema(description = "Unique ID of the book", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "Book title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(description = "Title of the book", example = "Don Quixote", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    
    @NotBlank(message = "Author name is required")
    @Size(min = 1, max = 255, message = "Author name must be between 1 and 255 characters")
    @Schema(description = "Author of the book", example = "Miguel de Cervantes", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;
    
    @Min(value = 1000, message = "Publication year must be greater than 999")
    @Max(value = 2100, message = "Publication year must be less than 2101")
    @Schema(description = "Year of publication", example = "1605", minimum = "1000", maximum = "2100")
    private Integer publicationYear;
    
    @Size(max = 100, message = "Category must be maximum 100 characters")
    @Schema(description = "Category or genre of the book", example = "Novel")
    private String category;
    
    // Constructors
    public BookDTO() {}
    
    public BookDTO(String title, String author, Integer publicationYear, String category) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.category = category;
    }
    
    public BookDTO(Long id, String title, String author, Integer publicationYear, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}
