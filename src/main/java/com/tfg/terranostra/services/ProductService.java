package com.tfg.terranostra.services;

import com.tfg.terranostra.dtos.ProductDto;
import com.tfg.terranostra.models.ProductModel;
import com.tfg.terranostra.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired

    private ProductRepository productRepository;

    public ProductModel aniadirProducto(ProductDto productDto){

        try {

            ProductModel product = new ProductModel();
            product.setName(product.getName());
            product.setPrice(product.getPrice());
            product.setDescription(product.getDescription());
            product.setStock(product.getStock());
            product.setCategory(product.getCategory());

            return productRepository.save(product);

        } catch (Exception e) {
            System.err.println("Error al guardar el producto: " + e.getMessage());
            throw new RuntimeException("Error al añadir el producto", e);
        }

    }

}
