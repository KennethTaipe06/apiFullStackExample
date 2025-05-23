package com.FullStackExample.apiFullStackExample.repository;

import com.FullStackExample.apiFullStackExample.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    // Buscar libros por nombre (contiene la cadena, ignorando mayúsculas)
    List<Libro> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar libros por autor
    List<Libro> findByAutorContainingIgnoreCase(String autor);
    
    // Buscar libros por categoría
    List<Libro> findByCategoria(String categoria);
    
    // Buscar libros por año de publicación
    List<Libro> findByAnoPublicacion(Integer anoPublicacion);
    
    // Buscar libros por rango de años
    List<Libro> findByAnoPublicacionBetween(Integer inicioAno, Integer finAno);
    
    // Buscar por autor y categoría
    List<Libro> findByAutorContainingIgnoreCaseAndCategoria(String autor, String categoria);
    
    // Query personalizada para buscar por múltiples criterios
    @Query("SELECT l FROM Libro l WHERE " +
           "(:nombre IS NULL OR LOWER(l.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:autor IS NULL OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
           "(:categoria IS NULL OR l.categoria = :categoria) AND " +
           "(:anoPublicacion IS NULL OR l.anoPublicacion = :anoPublicacion)")
    List<Libro> findByMultipleCriteria(
            @Param("nombre") String nombre,
            @Param("autor") String autor,
            @Param("categoria") String categoria,
            @Param("anoPublicacion") Integer anoPublicacion);
    
    // Obtener todas las categorías únicas
    @Query("SELECT DISTINCT l.categoria FROM Libro l WHERE l.categoria IS NOT NULL ORDER BY l.categoria")
    List<String> findAllCategorias();
    
    // Obtener todos los autores únicos
    @Query("SELECT DISTINCT l.autor FROM Libro l ORDER BY l.autor")
    List<String> findAllAutores();
    
    // Contar libros por categoría
    Long countByCategoria(String categoria);
    
    // Verificar si existe un libro con el mismo nombre y autor
    boolean existsByNombreAndAutor(String nombre, String autor);
}
