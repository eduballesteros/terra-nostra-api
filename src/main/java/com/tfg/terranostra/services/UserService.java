package com.tfg.terranostra.services;

import com.tfg.terranostra.dtos.UserDto;
import com.tfg.terranostra.models.ProductModel;
import com.tfg.terranostra.models.UserModel;
import com.tfg.terranostra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel signUpUser(UserDto userDto){

        try {
            UserModel user = new UserModel();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(user.getLastName());
            user.setEmail(user.getEmail());
            return userRepository.save(user);

        } catch (Exception e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
            throw new RuntimeException("Error al añadir el usuario", e);
        }

    }

}
