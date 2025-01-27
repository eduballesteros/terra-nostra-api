package com.tfg.terranostra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "La descripción no puede estar vacía.")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "El precio no puede estar vacío.")
    @Positive(message = "El precio debe ser un valor positivo.")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "La cantidad en stock no puede estar vacía.")
    @Positive(message = "La cantidad en stock debe ser un valor positivo.")
    private int stock;

    @Column
    @NotBlank(message = "La categoría no puede estar vacía.")
    private String category;

    @Column
    private String imageUrl;
}