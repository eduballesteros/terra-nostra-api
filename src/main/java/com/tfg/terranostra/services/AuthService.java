package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 📌 Método que maneja el inicio de sesión.
     *
     * @param loginDto Datos de inicio de sesión (email y contraseña).
     * @return UsuarioDto si la autenticación es exitosa, `null` si falla.
     */
    public UsuarioDto autenticarUsuario(LoginDto loginDto) {
        logger.info("📩 Intentando autenticar usuario con email: {}", loginDto.getEmail());

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(loginDto.getEmail());

        if (usuarioOpt.isEmpty()) {
            logger.warn("❌ Usuario no encontrado en la base de datos: {}", loginDto.getEmail());
            return null;
        }

        UsuarioModel usuarioModel = usuarioOpt.get();
        logger.info("✅ Usuario encontrado: {}", usuarioModel.getEmail());
        logger.debug("🎭 Rol en la base de datos: {}", usuarioModel.getRol());

        // Verificar si la contraseña coincide
        boolean match = passwordEncoder.matches(loginDto.getContrasenia(), usuarioModel.getContrasenia());
        logger.info("🔎 ¿Las contraseñas coinciden? {}", match);

        if (!match) {
            logger.error("❌ Contraseña incorrecta para el usuario: {}", loginDto.getEmail());
            return null;
        }

        logger.info("✅ Inicio de sesión exitoso para: {}", usuarioModel.getEmail());
        logger.debug("🎭 Rol antes de crear UsuarioDto: {}", usuarioModel.getRol());

        UsuarioDto usuarioDto = new UsuarioDto(usuarioModel.getId(), usuarioModel.getEmail(), usuarioModel.getRol());

        logger.debug("📤 UsuarioDto generado: {}", usuarioDto);

        return usuarioDto;
    }
}
