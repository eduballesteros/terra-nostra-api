package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.UsuarioDto;
import com.tfg.terranostra.models.UsuarioModel;
import com.tfg.terranostra.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método que recibe un UsuarioDto con los datos y lo guarda en la base de datos.
     *
     * @param userDto Datos del usuario a registrar.
     * @return UsuarioModel guardado en la base de datos.
     */
    @Transactional
    public UsuarioModel aniadirUsuario(@Valid UsuarioDto userDto) {

        if (usuarioRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("❌ El email ya está registrado");
        }

        // Convertir DTO a Modelo
        UsuarioModel user = new UsuarioModel();
        user.setNombre(userDto.getNombre());
        user.setApellido(userDto.getApellido());
        user.setContrasenia(passwordEncoder.encode(userDto.getContrasenia())); // Cifrar contraseña
        user.setEmail(userDto.getEmail());
        user.setDireccion(userDto.getDireccion());
        user.setTelefono(userDto.getTelefono());

        // Guardar el usuario en la base de datos
        return usuarioRepository.save(user);
    }
}
