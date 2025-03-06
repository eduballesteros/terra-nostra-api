package com.tfg.terranostra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La descripción no puede estar vacía.")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El precio no puede estar vacío.")
    @Positive(message = "El precio debe ser un valor positivo.")
    private BigDecimal precio;

    @Column(nullable = false)
    @NotNull(message = "La cantidad en stock no puede estar vacía.")
    @Positive(message = "La cantidad en stock debe ser un valor positivo.")
    private int stock;

    @Column
    @NotBlank(message = "La categoría no puede estar vacía.")
    private String categoria;

    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB") // Para MySQL
    @Basic(fetch = FetchType.LAZY) // Carga diferida para optimizar rendimiento
    private byte[] imagen;

}