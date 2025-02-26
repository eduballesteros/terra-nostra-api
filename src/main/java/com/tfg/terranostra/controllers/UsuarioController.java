package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * @param userDto Datos del usuario a registrar.
     * @return ResponseEntity con mensaje de éxito o error.
     */
    @PostMapping("/registro")
    public ResponseEntity<String> aniadirUsuario(@RequestBody UsuarioDto userDto) {
        try {
            // Enviar directamente el DTO al servicio
            UsuarioModel nuevoUsuario = usuarioService.aniadirUsuario(userDto);

            return new ResponseEntity<>("Usuario registrado con éxito. ID: " + nuevoUsuario.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
