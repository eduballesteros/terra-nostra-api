package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dtos.UserDto;
import com.tfg.terranostra.models.UserModel;
import com.tfg.terranostra.repositories.UserRepository;
import com.tfg.terranostra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") //

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * @param userDto
     * @return ResponseEntity<String>` con mensaje de éxito
     * @author ebp 28/01/25
     */
    @PostMapping("/registro")
    public ResponseEntity<String>aniadirUsuario(@Valid@RequestBody UserDto userDto){

        try {
            UserModel user = userService.aniadirUsuario(userDto);
            return new ResponseEntity<>("Usuario registrado con exito. ID: "+ user.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
