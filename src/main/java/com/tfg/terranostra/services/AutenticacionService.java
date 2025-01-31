package com.tfg.terranostra.services;

import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import com.tfg.terranostra.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para autenticar al usuario
    public String autenticarUsuario(String email, String contrasena) {

        UsuarioModel usuario = usuarioRepository.findbyEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Verificar la contraseña utilizando BCryptPasswordEncoder
        if (!passwordEncoder.matches(contrasena, usuario.getContresenia())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Crear un objeto de autenticación con el email y la contraseña
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, contrasena));

        // Si la autenticación es exitosa, generar el token JWT
        if (authentication.isAuthenticated()) {
            return jwtUtil.generarToken(usuario.getEmail());
        } else {
            throw new RuntimeException("Autenticación fallida");
        }
    }
}
