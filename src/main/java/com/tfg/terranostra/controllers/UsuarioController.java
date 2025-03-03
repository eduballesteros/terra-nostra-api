package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    /**
     * 📌 Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/registro")
    public ResponseEntity<String> aniadirUsuario(@RequestBody UsuarioDto userDto) {
        logger.info("📝 Intentando registrar un nuevo usuario: {}", userDto.getEmail());

        try {
            UsuarioModel nuevoUsuario = usuarioService.aniadirUsuario(userDto);
            logger.info("✅ Usuario registrado con éxito. ID: {}", nuevoUsuario.getId());
            return new ResponseEntity<>("Usuario registrado con éxito. ID: " + nuevoUsuario.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("❌ Error al registrar usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error al añadir el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 📌 Endpoint para obtener todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> obtenerUsuarios() {
        logger.info("📢 Solicitando lista de usuarios...");

        try {
            List<UsuarioDto> usuarios = usuarioService.obtenerTodos();
            logger.info("✅ Usuarios obtenidos con éxito. Total: {}", usuarios.size());
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("❌ Error al obtener usuarios: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 📌 Endpoint para obtener un usuario por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorEmail(@PathVariable String email) {
        logger.info("🔎 Buscando usuario con email: {}", email);

        try {
            UsuarioDto usuario = usuarioService.obtenerPorEmail(email);
            if (usuario != null) {
                logger.info("✅ Usuario encontrado: {}", usuario);
                return ResponseEntity.ok(usuario);
            } else {
                logger.warn("⚠️ Usuario no encontrado con email: {}", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("❌ Error al buscar usuario por email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 📌 Endpoint para actualizar un usuario por email.
     */
    @PutMapping("/email/{email}")
    public ResponseEntity<String> actualizarUsuarioPorEmail(@PathVariable String email, @RequestBody UsuarioDto userDto) {
        logger.info("✏️ Intentando actualizar usuario con email: {}", email);
        logger.debug("📤 Datos recibidos para actualizar: {}", userDto);

        try {
            boolean actualizado = usuarioService.actualizarUsuarioPorEmail(email, userDto);
            if (actualizado) {
                logger.info("✅ Usuario actualizado correctamente: {}", email);
                return ResponseEntity.ok("Usuario actualizado con éxito");
            } else {
                logger.warn("⚠️ No se encontró usuario con email: {} para actualizar", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("❌ Error al actualizar usuario con email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar usuario");
        }
    }

    /**
     * 📌 Endpoint para eliminar un usuario por email.
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<String> eliminarUsuarioPorEmail(@PathVariable String email) {
        logger.info("🗑️ Intentando eliminar usuario con email: {}", email);

        try {
            boolean eliminado = usuarioService.eliminarUsuarioPorEmail(email);
            if (eliminado) {
                logger.info("✅ Usuario eliminado correctamente: {}", email);
                return ResponseEntity.ok("Usuario eliminado correctamente");
            } else {
                logger.warn("⚠️ No se encontró usuario con email: {} para eliminar", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("❌ Error al eliminar usuario con email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar usuario");
        }
    }
}
