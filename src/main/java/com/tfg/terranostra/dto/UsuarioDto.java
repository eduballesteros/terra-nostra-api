package com.tfg.terranostra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor 
@AllArgsConstructor

/**
 * Data Transfer Object (DTO) para representar a un usuario en la aplicación.
 * Contiene los datos básicos del usuario y validaciones para asegurar la integridad de los datos.
 *
 * @author ebp
 * @version 1.0
 */


public class UsuarioDto {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasenia;

    private String telefono;
    private String direccion;

    @JsonProperty("fechaRegistro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @JsonProperty("fechaModificacion")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaModificacion;


    private String rol;

    public UsuarioDto(long id,
                      @NotBlank(message = "El nombre no puede estar vacío") String nombre,
                      @NotBlank(message = "El apellido no puede estar vacío") String apellido,
                      @NotBlank(message = "El correo no puede estar vacío") @Email(message = "Debe ser un correo válido") String email,
                      String telefono,
                      String direccion,
                      LocalDateTime fechaRegistro,
                      LocalDateTime fechaModificacion,
                      String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.rol = rol;
    }

    public UsuarioDto(long id, @NotBlank(message = "El correo no puede estar vacío") @Email(message = "Debe ser un correo válido") String email, String rol) {
    }
}
