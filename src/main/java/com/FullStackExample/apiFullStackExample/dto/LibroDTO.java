package com.FullStackExample.apiFullStackExample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Schema(description = "DTO para transferencia de datos de libros")
public class LibroDTO {
      @Schema(description = "ID único del libro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "El nombre del libro es requerido")
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    @Schema(description = "Nombre del libro", example = "Don Quijote de La Mancha", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
    
    @NotBlank(message = "El autor es requerido")
    @Size(min = 1, max = 255, message = "El autor debe tener entre 1 y 255 caracteres")
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes", requiredMode = Schema.RequiredMode.REQUIRED)
    private String autor;
    
    @Min(value = 1000, message = "El año de publicación debe ser mayor a 999")
    @Max(value = 2100, message = "El año de publicación debe ser menor a 2101")
    @Schema(description = "Año de publicación del libro", example = "1605", minimum = "1000", maximum = "2100")
    private Integer anoPublicacion;
    
    @Size(max = 100, message = "La categoría debe tener máximo 100 caracteres")
    @Schema(description = "Categoría o género del libro", example = "Novela")
    private String categoria;
    
    // Constructors
    public LibroDTO() {}
    
    public LibroDTO(String nombre, String autor, Integer anoPublicacion, String categoria) {
        this.nombre = nombre;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.categoria = categoria;
    }
    
    public LibroDTO(Long id, String nombre, String autor, Integer anoPublicacion, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.categoria = categoria;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getAutor() { 
        return autor; 
    }
    
    public void setAutor(String autor) { 
        this.autor = autor; 
    }
    
    public Integer getAnoPublicacion() { 
        return anoPublicacion; 
    }
    
    public void setAnoPublicacion(Integer anoPublicacion) { 
        this.anoPublicacion = anoPublicacion; 
    }
    
    public String getCategoria() { 
        return categoria; 
    }
    
    public void setCategoria(String categoria) { 
        this.categoria = categoria; 
    }
}
