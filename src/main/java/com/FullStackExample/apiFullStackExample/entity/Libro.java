package com.FullStackExample.apiFullStackExample.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String autor;
    
    @Column(name = "ano_publicacion")
    private Integer anoPublicacion;
    
    private String categoria;
    
    // Constructors
    public Libro() {}
    
    public Libro(String nombre, String autor, Integer anoPublicacion, String categoria) {
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
    
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacion=" + anoPublicacion +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
