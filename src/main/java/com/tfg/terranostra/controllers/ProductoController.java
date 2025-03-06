package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.ProductoDto;
import com.tfg.terranostra.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    /**
     * 📌 Guardar un producto
     */
    @PostMapping("/guardar")
    public ResponseEntity<?> agregarProducto(@Valid @RequestBody ProductoDto productoDto, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errores en la validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            logger.error("❌ Error en la validación de datos: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        try {
            logger.info("📥 Se ha recibido la solicitud para agregar el producto: {}", productoDto.getNombre());
            productoService.agregarProducto(productoDto);
            logger.info("✅ Producto agregado exitosamente: {}", productoDto.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(productoDto);
        } catch (Exception e) {
            logger.error("❌ Error al añadir el producto: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir el producto.");
        }
    }

    /**
     * 📌 Listar todos los productos
     */
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDto>> listarProductos() {
        try {
            List<ProductoDto> productos = productoService.obtenerTodosLosProductos();
            logger.info("✅ Productos obtenidos correctamente. Total: {}", productos.size());
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            logger.error("❌ Error al obtener productos:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * 🗑 Eliminar un producto por ID
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            Optional<ProductoDto> productoOptional = productoService.obtenerProductoPorId(id);
            if (productoOptional.isPresent()) {
                productoService.eliminarProducto(id);
                logger.info("✅ Producto eliminado correctamente. ID: {}", id);
                return ResponseEntity.ok().body("{\"mensaje\": \"✅ Producto eliminado correctamente.\"}");
            } else {
                logger.warn("⚠ Producto no encontrado. ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"mensaje\": \"❌ Producto no encontrado.\"}");
            }
        } catch (Exception e) {
            logger.error("❌ Error al eliminar el producto con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"mensaje\": \"❌ Error al eliminar el producto.\"}");
        }
    }

    /**
     * ✏ Editar un producto por ID
     */
    @PutMapping(value = "/editar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDto productoActualizado,
            BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errores en la validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            logger.error("❌ Error en la validación de datos para edición: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensaje", errorMessage.toString()));
        }

        try {
            Optional<ProductoDto> productoOptional = productoService.obtenerProductoPorId(id);
            if (productoOptional.isPresent()) {
                productoService.editarProducto(id, productoActualizado);
                logger.info("✅ Producto actualizado correctamente. ID: {}", id);
                return ResponseEntity.ok(Collections.singletonMap("mensaje", "✅ Producto actualizado correctamente."));
            } else {
                logger.warn("⚠ Producto no encontrado. ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("mensaje", "❌ Producto no encontrado."));
            }
        } catch (Exception e) {
            logger.error("❌ Error al actualizar el producto con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "❌ Error al actualizar el producto."));
        }
    }

    /**
     * 📌 Obtener un producto por ID
     */
    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        logger.info("📥 Solicitando producto con ID: {}", id);

        Optional<ProductoDto> productoOptional = productoService.obtenerProductoPorId(id);

        if (productoOptional.isPresent()) {
            return ResponseEntity.ok(productoOptional.get());
        } else {
            logger.warn("⚠ Producto no encontrado. ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"mensaje\": \"❌ Producto no encontrado.\"}");
        }
    }


}