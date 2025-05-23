package com.FullStackExample.apiFullStackExample.controller;

import com.FullStackExample.apiFullStackExample.dto.LibroDTO;
import com.FullStackExample.apiFullStackExample.service.LibroService;
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
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*")
@Tag(name = "Libros", description = "API para la gestión de libros en la biblioteca")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
      // Obtener todos los libros
    @GetMapping
    @Operation(summary = "Obtener todos los libros", 
               description = "Retorna una lista con todos los libros registrados en la biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<LibroDTO>> getAllLibros() {
        try {
            List<LibroDTO> libros = libroService.getAllLibros();
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
      // Obtener libro por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", 
               description = "Retorna un libro específico basado en su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<LibroDTO> getLibroById(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<LibroDTO> libro = libroService.getLibroById(id);
            return libro.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
      // Crear nuevo libro
    @PostMapping
    @Operation(summary = "Crear nuevo libro", 
               description = "Crea un nuevo libro en la biblioteca con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o libro ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createLibro(
            @Parameter(description = "Datos del libro a crear", required = true)
            @Valid @RequestBody LibroDTO libroDTO) {
        try {
            LibroDTO nuevoLibro = libroService.createLibro(libroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno del servidor");
        }
    }
      // Actualizar libro
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", 
               description = "Actualiza los datos de un libro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateLibro(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id, 
            @Parameter(description = "Nuevos datos del libro", required = true)
            @Valid @RequestBody LibroDTO libroDTO) {
        try {
            LibroDTO libroActualizado = libroService.updateLibro(id, libroDTO);
            return ResponseEntity.ok(libroActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno del servidor");
        }
    }
      // Eliminar libro
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", 
               description = "Elimina permanentemente un libro de la biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteLibro(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id) {
        try {
            libroService.deleteLibro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno del servidor");
        }
    }
      // Buscar libros por nombre
    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar libros por nombre", 
               description = "Busca libros que contengan el texto especificado en el nombre (búsqueda parcial, sin distinción de mayúsculas)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<LibroDTO>> findByNombre(
            @Parameter(description = "Texto a buscar en el nombre del libro", required = true, example = "Don Quijote")
            @RequestParam String nombre) {
        try {
            List<LibroDTO> libros = libroService.findByNombre(nombre);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar libros por autor
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<LibroDTO>> findByAutor(@RequestParam String autor) {
        try {
            List<LibroDTO> libros = libroService.findByAutor(autor);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar libros por categoría
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<LibroDTO>> findByCategoria(@RequestParam String categoria) {
        try {
            List<LibroDTO> libros = libroService.findByCategoria(categoria);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar libros por año de publicación
    @GetMapping("/buscar/ano")
    public ResponseEntity<List<LibroDTO>> findByAnoPublicacion(@RequestParam Integer ano) {
        try {
            List<LibroDTO> libros = libroService.findByAnoPublicacion(ano);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar libros por rango de años
    @GetMapping("/buscar/rango-anos")
    public ResponseEntity<List<LibroDTO>> findByRangoAnos(
            @RequestParam Integer inicioAno, 
            @RequestParam Integer finAno) {
        try {
            List<LibroDTO> libros = libroService.findByRangoAnos(inicioAno, finAno);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Búsqueda avanzada con múltiples criterios
    @GetMapping("/buscar/avanzada")
    public ResponseEntity<List<LibroDTO>> findByMultipleCriteria(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer anoPublicacion) {
        try {
            List<LibroDTO> libros = libroService.findByMultipleCriteria(nombre, autor, categoria, anoPublicacion);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtener todas las categorías
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> getAllCategorias() {
        try {
            List<String> categorias = libroService.getAllCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtener todos los autores
    @GetMapping("/autores")
    public ResponseEntity<List<String>> getAllAutores() {
        try {
            List<String> autores = libroService.getAllAutores();
            return ResponseEntity.ok(autores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Contar libros por categoría
    @GetMapping("/contar/categoria")
    public ResponseEntity<Long> countByCategoria(@RequestParam String categoria) {
        try {
            Long count = libroService.countByCategoria(categoria);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
