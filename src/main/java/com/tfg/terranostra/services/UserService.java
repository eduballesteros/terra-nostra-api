package com.tfg.terranostra.services;

import com.tfg.terranostra.models.UserModel;
import com.tfg.terranostra.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor para inyectar el repositorio
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método para crear un nuevo usuario
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }
}
