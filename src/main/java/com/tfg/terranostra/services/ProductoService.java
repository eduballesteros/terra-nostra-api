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
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    /**
     * 📌 Método para agregar un nuevo producto.
     * @param productoDto Datos del producto a añadir.
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
     * 📌 Método para obtener todos los productos.
     * @return Lista de productos en formato DTO.
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
     * 📌 Método para obtener un producto por ID.
     * @param id ID del producto.
     * @return Optional con el producto encontrado o vacío si no existe.
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
     * 🗑 Método para eliminar un producto por ID.
     * @param id ID del producto a eliminar.
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
     * ✏ Método para editar un producto existente.
     * @param id ID del producto a editar.
     * @param productoDto Datos actualizados del producto.
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