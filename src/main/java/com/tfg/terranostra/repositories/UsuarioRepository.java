package com.tfg.terranostra.repositories;

import com.tfg.terranostra.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    /**
     * Método que busca a un usuario por su correo.
     * @param email Correo del usuario.
     * @return Un Optional<UsuarioModel> si existe, vacío si no.
     */
    Optional<UsuarioModel> findByEmail(String email);
}
