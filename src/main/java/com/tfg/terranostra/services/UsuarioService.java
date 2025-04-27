package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.PasswordResetToken;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.PasswordResetTokenRepository;
import com.tfg.terranostra.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CorreoService correoService;

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
        user.setContrasenia(passwordEncoder.encode(userDto.getContrasenia()));
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
                        usuario.getFechaModificacion(),
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
                        usuario.getFechaModificacion(),
                        usuario.getRol()
                )).orElse(null);
    }

    @Transactional
    public boolean actualizarUsuarioPorEmail(String email, UsuarioDto userDto) {
        logger.info("🛠️ Intentando actualizar usuario con email: {}", email);

        return usuarioRepository.findByEmail(email).map(usuario -> {
            logger.debug("📥 Datos actuales del usuario antes de la actualización: {}", usuario);
            logger.debug("📤 Datos recibidos para actualización: {}", userDto);

            if (userDto.getId() != null) {
                usuario.setId(userDto.getId());
            }

            usuario.setNombre(userDto.getNombre());
            usuario.setApellido(userDto.getApellido());
            usuario.setTelefono(userDto.getTelefono());
            usuario.setDireccion(userDto.getDireccion());
            usuario.setRol(userDto.getRol());

            if (userDto.getFechaRegistro() != null) {
                usuario.setFechaRegistro(userDto.getFechaRegistro());
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

    /**
     * Envia un enlace de recuperación de contraseña al correo del usuario.
     * Genera y guarda un token temporal en la base de datos.
     */
    @Transactional
    public void enviarEnlaceCambioContrasenia(String email) throws Exception {
        UsuarioModel usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("No se encontró usuario con ese email"));

        // Buscar y eliminar token existente si lo hay
        Optional<PasswordResetToken> tokenExistente = tokenRepository.findByEmail(email);
        tokenExistente.ifPresent(tokenRepository::delete);

        // Crear nuevo token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(10);

        PasswordResetToken nuevoToken = new PasswordResetToken();
        nuevoToken.setEmail(email);
        nuevoToken.setToken(token);
        nuevoToken.setExpiracion(expiracion);

        tokenRepository.save(nuevoToken);

        // Generar enlace de recuperación
        String link = "http://localhost:8080/cambiar-contrasenia?token=" + token;

        String asunto = "🔐 Recuperación de contraseña - Terra Nostra";
        String cuerpoHtml = """
        <div style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
            <h2 style="color: #F18817;">🔐 Recuperación de contraseña</h2>
            <p>Hola <strong>%s</strong>,</p>
            <p>Haz clic en el siguiente botón para cambiar tu contraseña:</p>
            <p style="text-align: center;">
                <a href="%s" style="display: inline-block; padding: 12px 24px;
                   background-color: #F18817; color: white; text-decoration: none;
                   border-radius: 5px; font-weight: bold;">
                   Cambiar contraseña
                </a>
            </p>
            <p>Este enlace expirará en 10 minutos.</p>
            <hr style="margin-top: 30px;">
            <p style="font-size: 12px; color: #888;">
                © 2025 Terra Nostra · No responder a este correo automático.
            </p>
        </div>
        """.formatted(usuario.getNombre(), link);

        correoService.enviarCorreo(email, asunto, cuerpoHtml);
    }
}
