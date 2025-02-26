package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.ProductoDto;
import com.tfg.terranostra.models.ProductoModel;
import com.tfg.terranostra.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoModel aniadirProducto(ProductoDto productoDto) {

        try {
            // Crear una nueva instancia del modelo ProductModel
            ProductoModel producto = new ProductoModel();

            // Asignar los valores del DTO al modelo
            producto.setNombre(productoDto.getNombre());
            producto.setPrecio(productoDto.getPrecio());
            producto.setDescripcion(productoDto.getDescripcion());
            producto.setStock(productoDto.getStock());
            producto.setCategoria(productoDto.getCategoria());
            producto.setImagenUrl(productoDto.getImagenUrl());

            // Guardar el producto en la base de datos
            return productoRepository.save(producto);

        } catch (Exception e) {
            // Registrar el error en la consola
            System.err.println("Error al guardar el producto: " + e.getMessage());
            throw new RuntimeException("Error al añadir el producto", e);
        }
    }
}
