package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dtos.ProductoDto;
import com.tfg.terranostra.models.ProductoModel;
import com.tfg.terranostra.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/aniadirProducto/")
    public ResponseEntity<String> aniadirProducto(@RequestBody ProductoDto productoDto) {
        try {
            ProductoModel product = productoService.aniadirProducto(productoDto);
            return new ResponseEntity<>("Producto añadido con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el producto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
