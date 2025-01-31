package com.tfg.terranostra.services;

import com.tfg.terranostra.dtos.UsuarioDto;
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
     * Método que recibe un UserDto con los datos y lo guarda en la base de datos.
     * @param userDto
     * @return userModel
     * @author ebp 29/01/25
     */
    @Transactional
    public UsuarioModel aniadirUsuario(@Valid UsuarioDto userDto) {
        try {

            UsuarioModel user = new UsuarioModel();
            user.setNombre(userDto.getNombre());
            user.setApellido(userDto.getApellido());
            user.setEmail(userDto.getEmail());
            user.setTelefono(userDto.getTelefono());
            user.setDireccion(userDto.getDireccion());
            user.setFechaNacimiento(userDto.getFechaNacimiento());

            String encryptedPassword = passwordEncoder.encode(userDto.getContrasenia());
            user.setContresenia(encryptedPassword);

            // Guardar el usuario en la base de datos
            return usuarioRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al añadir el usuario: " + e.getMessage(), e);
        }
    }

}

