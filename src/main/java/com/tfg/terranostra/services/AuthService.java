package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.CambioContraseniaDto;
import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.PasswordResetToken;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.PasswordResetTokenRepository;
import com.tfg.terranostra.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private CorreoService correoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDto autenticarUsuario(LoginDto loginDto) {
        logger.info("📩 Intentando autenticar usuario con email: {}", loginDto.getEmail());

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(loginDto.getEmail());

        if (usuarioOpt.isEmpty()) {
            logger.warn("❌ Usuario no encontrado en la base de datos: {}", loginDto.getEmail());
            return null;
        }

        UsuarioModel usuarioModel = usuarioOpt.get();
        boolean match = passwordEncoder.matches(loginDto.getContrasenia(), usuarioModel.getContrasenia());
        logger.info("🔎 ¿Las contraseñas coinciden? {}", match);

        if (!match) {
            logger.error("❌ Contraseña incorrecta para el usuario: {}", loginDto.getEmail());
            return null;
        }

        UsuarioDto usuarioDto = new UsuarioDto(
                usuarioModel.getId(),
                usuarioModel.getNombre(),
                usuarioModel.getApellido(),
                usuarioModel.getEmail(),
                usuarioModel.getContrasenia(),
                usuarioModel.getTelefono(),
                usuarioModel.getDireccion(),
                usuarioModel.getFechaRegistro(),
                usuarioModel.getFechaModificacion(),
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

    @Transactional
    public boolean cambiarContrasenia(CambioContraseniaDto dto) {
        logger.info("🔁 Intentando cambiar la contraseña con token: {}", dto.getToken());

        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(dto.getToken());

        if (tokenOpt.isEmpty()) {
            logger.warn("❌ Token no encontrado: {}", dto.getToken());
            return false;
        }

        PasswordResetToken token = tokenOpt.get();

        if (token.getExpiracion().isBefore(LocalDateTime.now())) { // <-- Cambiado aquí
            logger.warn("❌ Token expirado: {}", dto.getToken());
            return false;
        }

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(token.getEmail()); // <-- Buscamos por email

        if (usuarioOpt.isEmpty()) {
            logger.error("❌ No existe un usuario asociado al email: {}", token.getEmail());
            return false;
        }

        UsuarioModel usuario = usuarioOpt.get();
        usuario.setContrasenia(passwordEncoder.encode(dto.getNuevaContrasenia()));
        usuario.setFechaModificacion(LocalDateTime.now());

        usuarioRepository.save(usuario);

        tokenRepository.delete(token);

        logger.info("✅ Contraseña actualizada exitosamente para el usuario: {}", usuario.getEmail());
        return true;
    }
}
