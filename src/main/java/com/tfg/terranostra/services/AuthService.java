package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
/**
 * Servicio de autenticación de usuarios.
 * Proporciona métodos para autenticar a los usuarios en la aplicación.
 * Valida credenciales y gestiona la autenticación en el contexto de seguridad.
 *
 * @author ebp
 * @version 1.0
 */

public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Maneja la autenticación de usuarios en la aplicación.
     * - Verifica si el usuario existe en la base de datos.<br>
     * - Compara la contraseña proporcionada con la almacenada en la base de datos.<br>
     * - Si la autenticación es exitosa, devuelve un objeto {@link UsuarioDto} con la información del usuario.<br>
     * - Si la autenticación falla, devuelve `null`.
     * *
     * @param loginDto Datos de inicio de sesión que incluyen email y contraseña.
     * @return {@link UsuarioDto} con los datos del usuario autenticado o `null` si las credenciales son incorrectas.
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

        // Verificar si la contraseña coincide
        boolean match = passwordEncoder.matches(loginDto.getContrasenia(), usuarioModel.getContrasenia());
        logger.info("🔎 ¿Las contraseñas coinciden? {}", match);

        if (!match) {
            logger.error("❌ Contraseña incorrecta para el usuario: {}", loginDto.getEmail());
            return null;
        }

        logger.info("✅ Inicio de sesión exitoso para: {}", usuarioModel.getEmail());

        // 📌 Crear el objeto UsuarioDto con valores reales
        UsuarioDto usuarioDto = new UsuarioDto(
                usuarioModel.getId(),
                usuarioModel.getNombre(),
                usuarioModel.getApellido(),
                usuarioModel.getEmail(),
                usuarioModel.getContrasenia(),
                usuarioModel.getTelefono(),
                usuarioModel.getDireccion(),
                usuarioModel.getFechaRegistro(),
                usuarioModel.getRol()
        );

        UserDetails userDetails = User.withUsername(usuarioModel.getEmail())
                .password(usuarioModel.getContrasenia())
                .build();


        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("🔐 Usuario autenticado correctamente en SecurityContext: {}", usuarioModel.getEmail());

        return usuarioDto;
    }
}
