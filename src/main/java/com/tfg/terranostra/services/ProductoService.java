package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.ProductoDto;
import com.tfg.terranostra.repositories.ProductoRepository;
import com.tfg.terranostra.models.ProductoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

/**
 * Servicio para la gestión de productos en la aplicación.
 * Proporciona métodos para agregar, obtener, editar y eliminar productos.
 *
 * @author ebp
 * @version 1.0
 */
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    /**
     * Agrega un nuevo producto a la base de datos.
     * - Convierte un objeto {@link ProductoDto} en {@link ProductoModel}.<br>
     * - Guarda el producto en la base de datos.<br>
     * - Registra logs de éxito o error.
     *
     * @param productoDto Datos del producto a añadir.
     * @throws RuntimeException Si ocurre un error al guardar el producto.
     */

    public void agregarProducto(ProductoDto productoDto) {
        ProductoModel producto = new ProductoModel();
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setPrecio(productoDto.getPrecio());
        producto.setStock(productoDto.getStock());
        producto.setCategoria(productoDto.getCategoria());
        producto.setImagen(productoDto.getImagen());

        logger.info("📦 Creando nuevo producto: {}", producto.getNombre());

        try {
            productoRepository.save(producto);
            logger.info("✅ Producto guardado correctamente: {}", producto.getNombre());
        } catch (Exception e) {
            logger.error("❌ Error al guardar el producto: {}", producto.getNombre(), e);
            throw new RuntimeException("Error al guardar el producto.");
        }
    }

    /**
     * Obtiene la lista de todos los productos almacenados en la base de datos.
     * - Convierte los productos de {@link ProductoModel} a {@link ProductoDto} para la respuesta.<br>
     * - Retorna la lista de productos en formato DTO.
     *
     * @return Lista de productos en formato {@link ProductoDto}.
     */

    public List<ProductoDto> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream().map(producto -> {
            ProductoDto dto = new ProductoDto();
            dto.setId(producto.getId());
            dto.setNombre(producto.getNombre());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecio(producto.getPrecio());
            dto.setStock(producto.getStock());
            dto.setCategoria(producto.getCategoria());
            dto.setImagen(producto.getImagen());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Obtiene un producto de la base de datos según su ID.
     * - Busca el producto en la base de datos.<br>
     * - Si el producto existe, lo convierte a {@link ProductoDto}.<br>
     * - Si el producto no existe, retorna un {@link Optional} vacío.
     *
     * @param id ID del producto a buscar.
     * @return Un {@link Optional} con el producto si se encuentra, vacío si no.
     */

    public Optional<ProductoDto> obtenerProductoPorId(Long id) {
        Optional<ProductoModel> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            ProductoDto dto = new ProductoDto();
            dto.setId(producto.get().getId());
            dto.setNombre(producto.get().getNombre());
            dto.setDescripcion(producto.get().getDescripcion());
            dto.setPrecio(producto.get().getPrecio());
            dto.setStock(producto.get().getStock());
            dto.setCategoria(producto.get().getCategoria());
            dto.setImagen(producto.get().getImagen());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    /**
     * Elimina un producto de la base de datos según su ID.
     * - Verifica si el producto existe antes de eliminarlo.<br>
     * - Si el producto no se encuentra, lanza una excepción.<br>
     * - Registra logs sobre el proceso de eliminación.
     *
     * @param id ID del producto a eliminar.
     * @throws RuntimeException Si el producto no se encuentra en la base de datos.
     */


    public void eliminarProducto(Long id) {
        Optional<ProductoModel> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            logger.info("🗑 Eliminando producto: {}", productoOptional.get().getNombre());
            productoRepository.deleteById(id);
            logger.info("✅ Producto eliminado correctamente. ID: {}", id);
        } else {
            logger.warn("⚠ Producto no encontrado. ID: {}", id);
            throw new RuntimeException("Producto no encontrado.");
        }
    }

    /**
     * Actualiza los datos de un producto en la base de datos.
     * - Busca el producto por su ID.<br>
     * - Si existe, actualiza sus valores con los datos proporcionados en {@link ProductoDto}.<br>
     * - Si el producto no existe, lanza una excepción.<br>
     * - Registra logs sobre el proceso de actualización.
     *
     * @param id ID del producto a actualizar.
     * @param productoDto Datos actualizados del producto.
     * @throws RuntimeException Si el producto no se encuentra en la base de datos.
     */

    public void editarProducto(Long id, ProductoDto productoDto) {
        Optional<ProductoModel> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            ProductoModel producto = productoOptional.get();

            producto.setNombre(productoDto.getNombre());
            producto.setDescripcion(productoDto.getDescripcion());
            producto.setPrecio(productoDto.getPrecio());
            producto.setStock(productoDto.getStock());
            producto.setCategoria(productoDto.getCategoria());

            // 🔥 Añade esta condición
            if(productoDto.getImagen() != null && productoDto.getImagen().length > 0){
                producto.setImagen(productoDto.getImagen());
            }

            logger.info("✏ Editando producto: {}", producto.getNombre());

            productoRepository.save(producto);
            logger.info("✅ Producto actualizado correctamente: {}", producto.getNombre());
        } else {
            logger.warn("⚠ Producto no encontrado para actualizar. ID: {}", id);
            throw new RuntimeException("Producto no encontrado.");
        }
    }

}