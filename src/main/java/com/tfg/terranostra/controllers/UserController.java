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
@RequestMapping("/api/users") // Cambié la ruta a /users (plural)

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String>signUpUser(@RequestBody UserDto userDto){

        try {
            UserModel user = userService.signUpUser(userDto);
            return new ResponseEntity<>("Usuario registrado con exito. ID: "+ user.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
