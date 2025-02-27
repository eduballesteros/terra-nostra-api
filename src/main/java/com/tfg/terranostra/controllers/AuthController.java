package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import com.tfg.terranostra.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")

public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> inicioSesion(@RequestBody LoginDto loginDto) {
        UsuarioDto usuarioDto = authService.autenticarUsuario(loginDto);

        if (usuarioDto == null) {
            return ResponseEntity.status(401).body("❌ Credenciales incorrectas");
        }

        return ResponseEntity.ok(usuarioDto);
    }


}
