package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.LoginDto;
import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método que maneja el inicio de sesión
     * @param loginDto Datos de inicio de sesión (email y contraseña)
     * @return UsuarioDto si la autenticación es exitosa, `null` si falla
     */
    public UsuarioDto autenticarUsuario(LoginDto loginDto) {
        System.out.println("📩 Intentando autenticar usuario con email: " + loginDto.getEmail());

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(loginDto.getEmail());

        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuario no encontrado en la base de datos");
            return null;
        }

        UsuarioModel usuarioModel = usuarioOpt.get();
        System.out.println("✅ Usuario encontrado: " + usuarioModel.getEmail());

        if (!passwordEncoder.matches(loginDto.getContrasenia(), usuarioModel.getContrasenia())) {
            System.out.println("🔑 Contraseña incorrecta para el usuario: " + usuarioModel.getEmail());
            return null;
        }

        System.out.println("✅ Inicio de sesión exitoso para: " + usuarioModel.getEmail());
        return new UsuarioDto(usuarioModel.getId(), usuarioModel.getEmail());
    }
}