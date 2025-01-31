package com.tfg.terranostra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users")
//Genera automáticamente Get, set, toString ...
@Data

public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @Column
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @Column
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contresenia;

    @Column
    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @Column
    private String telefono;

    @Column
    private LocalDate fechaNacimiento; // Campo para la fecha de nacimiento

}
