package com.tfg.terranostra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users")
//Genera automáticamente Get, set, toString ...
@Data

public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @Column
    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @Column
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @Column
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Column
    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private LocalDate birthDate; // Campo para la fecha de nacimiento

}
