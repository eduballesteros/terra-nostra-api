package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioModel aniadirUsuario(@Valid UsuarioDto userDto) {
        logger.info("🟢 Intentando registrar un nuevo usuario con email: {}", userDto.getEmail());

        if (usuarioRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.warn("⚠️ Intento fallido: el email {} ya está registrado", userDto.getEmail());
            throw new RuntimeException("❌ El email ya está registrado");
        }

        UsuarioModel user = new UsuarioModel();
        user.setNombre(userDto.getNombre());
        user.setApellido(userDto.getApellido());
        user.setContrasenia(passwordEncoder.encode(userDto.getContrasenia())); // Cifrar contraseña
        user.setEmail(userDto.getEmail());
        user.setDireccion(userDto.getDireccion());
        user.setTelefono(userDto.getTelefono());
        user.setFechaRegistro(userDto.getFechaRegistro());
        user.setRol("ROLE_USER");

        UsuarioModel savedUser = usuarioRepository.save(user);
        logger.info("✅ Usuario registrado con éxito: {}", savedUser.getEmail());

        return savedUser;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> obtenerTodos() {
        logger.info("📋 Obteniendo la lista de todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioDto(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getDireccion(),
                        usuario.getFechaRegistro(),
                        usuario.getRol()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerPorEmail(String email) {
        logger.info("🔍 Buscando usuario por email: {}", email);
        return usuarioRepository.findByEmail(email)
                .map(usuario -> new UsuarioDto(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getDireccion(),
                        usuario.getFechaRegistro(),
                        usuario.getRol()
                )).orElse(null);
    }

    @Transactional
    public boolean actualizarUsuarioPorEmail(String email, UsuarioDto userDto) {
        logger.info("🛠️ Intentando actualizar usuario con email: {}", email);

        return usuarioRepository.findByEmail(email).map(usuario -> {
            logger.debug("📥 Datos actuales del usuario antes de la actualización: {}", usuario);
            logger.debug("📤 Datos recibidos para actualización: {}", userDto);

            // No modificar el ID si es null
            if (userDto.getId() != null) {
                usuario.setId(userDto.getId());
            } else {
                logger.warn("⚠️ El ID del usuario es null, se mantiene el valor actual.");
            }

            usuario.setNombre(userDto.getNombre());
            usuario.setApellido(userDto.getApellido());
            usuario.setTelefono(userDto.getTelefono());
            usuario.setDireccion(userDto.getDireccion());
            usuario.setRol(userDto.getRol());

            // Verificar si fechaRegistro es null, si lo es mantener el valor actual
            if (userDto.getFechaRegistro() != null) {
                usuario.setFechaRegistro(userDto.getFechaRegistro());
            } else {
                logger.warn("⚠️ `fechaRegistro` es null, manteniendo el valor anterior: {}", usuario.getFechaRegistro());
            }

            usuarioRepository.save(usuario);
            logger.info("✅ Usuario con email {} actualizado correctamente", email);
            return true;
        }).orElseGet(() -> {
            logger.warn("⚠️ No se encontró usuario con email: {} para actualizar", email);
            return false;
        });
    }


    @Transactional
    public boolean eliminarUsuarioPorEmail(String email) {
        logger.info("🗑️ Intentando eliminar usuario con email: {}", email);
        return usuarioRepository.findByEmail(email).map(usuario -> {
            usuarioRepository.delete(usuario);
            logger.info("✅ Usuario con email {} eliminado correctamente", email);
            return true;
        }).orElseGet(() -> {
            logger.warn("⚠️ No se encontró usuario con email: {} para eliminar", email);
            return false;
        });
    }
}
