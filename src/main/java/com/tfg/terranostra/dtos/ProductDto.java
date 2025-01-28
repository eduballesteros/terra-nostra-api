package com.tfg.terranostra.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal price;

    @NotNull(message = "La cantidad en stock no puede estar vacía")
    @Positive(message = "La cantidad en stock debe ser positiva")
    private int stock;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String category;

    private String imageUrl;

}
