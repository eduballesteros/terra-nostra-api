package com.tfg.terranostra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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

}
