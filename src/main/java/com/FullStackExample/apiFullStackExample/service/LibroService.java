package com.FullStackExample.apiFullStackExample.service;

import com.FullStackExample.apiFullStackExample.dto.LibroDTO;
import com.FullStackExample.apiFullStackExample.entity.Libro;
import com.FullStackExample.apiFullStackExample.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    // Obtener todos los libros
    public List<LibroDTO> getAllLibros() {
        return libroRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener libro por ID
    public Optional<LibroDTO> getLibroById(Long id) {
        return libroRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Crear nuevo libro
    public LibroDTO createLibro(LibroDTO libroDTO) {
        // Validar que no exista un libro con el mismo nombre y autor
        if (libroRepository.existsByNombreAndAutor(libroDTO.getNombre(), libroDTO.getAutor())) {
            throw new RuntimeException("Ya existe un libro con el mismo nombre y autor");
        }
        
        Libro libro = convertToEntity(libroDTO);
        Libro savedLibro = libroRepository.save(libro);
        return convertToDTO(savedLibro);
    }
    
    // Actualizar libro
    public LibroDTO updateLibro(Long id, LibroDTO libroDTO) {
        return libroRepository.findById(id)
                .map(libro -> {
                    // Verificar si el nombre y autor cambiaron y si ya existe otro libro con esos datos
                    if (!libro.getNombre().equals(libroDTO.getNombre()) || 
                        !libro.getAutor().equals(libroDTO.getAutor())) {
                        if (libroRepository.existsByNombreAndAutor(libroDTO.getNombre(), libroDTO.getAutor())) {
                            throw new RuntimeException("Ya existe un libro con el mismo nombre y autor");
                        }
                    }
                    
                    libro.setNombre(libroDTO.getNombre());
                    libro.setAutor(libroDTO.getAutor());
                    libro.setAnoPublicacion(libroDTO.getAnoPublicacion());
                    libro.setCategoria(libroDTO.getCategoria());
                    
                    Libro updatedLibro = libroRepository.save(libro);
                    return convertToDTO(updatedLibro);
                })
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }
    
    // Eliminar libro
    public void deleteLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
    }
    
    // Buscar libros por nombre
    public List<LibroDTO> findByNombre(String nombre) {
        return libroRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar libros por autor
    public List<LibroDTO> findByAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar libros por categoría
    public List<LibroDTO> findByCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar libros por año de publicación
    public List<LibroDTO> findByAnoPublicacion(Integer anoPublicacion) {
        return libroRepository.findByAnoPublicacion(anoPublicacion)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar libros por rango de años
    public List<LibroDTO> findByRangoAnos(Integer inicioAno, Integer finAno) {
        return libroRepository.findByAnoPublicacionBetween(inicioAno, finAno)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Búsqueda con múltiples criterios
    public List<LibroDTO> findByMultipleCriteria(String nombre, String autor, String categoria, Integer anoPublicacion) {
        return libroRepository.findByMultipleCriteria(nombre, autor, categoria, anoPublicacion)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener todas las categorías
    public List<String> getAllCategorias() {
        return libroRepository.findAllCategorias();
    }
    
    // Obtener todos los autores
    public List<String> getAllAutores() {
        return libroRepository.findAllAutores();
    }
    
    // Contar libros por categoría
    public Long countByCategoria(String categoria) {
        return libroRepository.countByCategoria(categoria);
    }
    
    // Método para convertir Entity a DTO
    private LibroDTO convertToDTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getNombre(),
                libro.getAutor(),
                libro.getAnoPublicacion(),
                libro.getCategoria()
        );
    }
    
    // Método para convertir DTO a Entity
    private Libro convertToEntity(LibroDTO libroDTO) {
        return new Libro(
                libroDTO.getNombre(),
                libroDTO.getAutor(),
                libroDTO.getAnoPublicacion(),
                libroDTO.getCategoria()
        );
    }
}
