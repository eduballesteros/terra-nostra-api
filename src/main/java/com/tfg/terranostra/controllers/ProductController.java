package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dtos.ProductDto;
import com.tfg.terranostra.models.ProductModel;
import com.tfg.terranostra.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> aniadirProducto(@RequestBody ProductDto productDto) {
        try {
            ProductModel product = productService.aniadirProducto(productDto);
            return new ResponseEntity<>("Producto añadido con éxito. ID: " + product.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el producto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
