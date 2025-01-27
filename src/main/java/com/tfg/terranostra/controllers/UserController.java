package com.tfg.terranostra.controllers;

import com.tfg.terranostra.models.UserModel;
import com.tfg.terranostra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Cambié la ruta a /users (plural)

public class UserController {

    private final UserService userService;

    // Constructor para inyectar el servicio
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel user) {
        UserModel newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // Puedes agregar más endpoints de GET, PUT, DELETE aquí si lo necesitas
}
