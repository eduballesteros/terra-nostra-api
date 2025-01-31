package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dtos.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.services.UsuarioService;
import jakarta.validation.Valid;
import com.tfg.terranostra.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") //

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    private JwtUtil jwtUtil;


    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * @param userDto
     * @return ResponseEntity<String>` con mensaje de éxito
     * @author ebp 28/01/25
     */
    @PostMapping("/registro")
    public ResponseEntity<String>aniadirUsuario(@Valid@RequestBody UsuarioDto userDto){

        try {
            UsuarioModel user = usuarioService.aniadirUsuario(userDto);
            return new ResponseEntity<>("Usuario registrado con exito. ID: "+ user.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
