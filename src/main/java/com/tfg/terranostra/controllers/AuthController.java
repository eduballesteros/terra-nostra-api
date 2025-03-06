package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.repositories.UsuarioRepository;
import com.tfg.terranostra.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")

/**
 * Controlador para la autenticación de usuarios en la aplicación.
 * Proporciona endpoints para el inicio de sesión de los usuarios.
 *
 * @author ebp
 * @version 1.0
 */
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    /**
     * Maneja el inicio de sesión de los usuarios.
     * - Recibe un objeto `LoginDto` con las credenciales del usuario.<br>
     * - Si las credenciales son correctas, devuelve un objeto `UsuarioDto` con los datos del usuario autenticado.<br>
     * - Si las credenciales son incorrectas, devuelve un código de estado HTTP 401 con un mensaje de error.
     *
     * @param loginDto Objeto que contiene el correo electrónico y la contraseña del usuario.
     * @return `ResponseEntity` con el objeto `UsuarioDto` si la autenticación es exitosa,
     *         o un mensaje de error si las credenciales son incorrectas.
     */

    @PostMapping("/login")
    public ResponseEntity<?> inicioSesion(@RequestBody LoginDto loginDto) {
        logger.info("🔐 Intento de inicio de sesión para el usuario: {}", loginDto.getEmail());

        UsuarioDto usuarioDto = authService.autenticarUsuario(loginDto);

        if (usuarioDto == null) {
            logger.warn("❌ Inicio de sesión fallido: Credenciales incorrectas para el usuario {}", loginDto.getEmail());
            return ResponseEntity.status(401).body("❌ Credenciales incorrectas");
        }

        logger.info("✅ Inicio de sesión exitoso para el usuario: {}", loginDto.getEmail());
        logger.debug("📤 Respuesta JSON enviada por la API: {}", usuarioDto);

        return ResponseEntity.ok(usuarioDto);
    }
}
