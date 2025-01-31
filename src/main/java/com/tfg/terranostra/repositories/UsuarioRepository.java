package com.tfg.terranostra.repositories;

import com.tfg.terranostra.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository <UsuarioModel, Long>{

    /**
     * Método que busca a un usuario por su correo;
     * @param usuario
     * @return
     */

    UsuarioModel findbyEmail(String email);

}
