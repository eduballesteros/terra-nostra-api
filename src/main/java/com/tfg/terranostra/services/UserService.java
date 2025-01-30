package com.tfg.terranostra.services;

import com.tfg.terranostra.dtos.UserDto;
import com.tfg.terranostra.models.UserModel;
import com.tfg.terranostra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Método que recibe un UserDto con los datos y lo guarda en la base de datos.
     * @param userDto
     * @return userModel
     * @author ebp 29/01/25
     */
    @Transactional
    public UserModel aniadirUsuario(UserDto userDto) {
        try {
            if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }

            // Crear y asignar los datos del nuevo usuario
            UserModel user = new UserModel();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setAddress(userDto.getAddress());
            user.setBirthDate(userDto.getBirthDate());
            user.setPassword(userDto.getPassword());  // Asegúrate de establecer la contraseña

            // Guardar el usuario en la base de datos
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al añadir el usuario: " + e.getMessage(), e);
        }
    }

}

