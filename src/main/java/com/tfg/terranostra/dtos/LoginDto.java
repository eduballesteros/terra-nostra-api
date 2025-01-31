package com.tfg.terranostra.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {

    @NotEmpty(message = "El correo electronico no puede estar vacío ya que es un campo obligatorio.")
    @Email (message = "El correo electrónico no tiene un formato válido.")
    private String email;

    @NotEmpty(message = "La contraseña puede estar vacía ya que es un campo obligatorio.")
    private String contrasenia;

}
