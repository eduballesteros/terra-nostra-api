package com.tfg.terranostra.repositories;

import com.tfg.terranostra.models.ProductoModel;
import com.tfg.terranostra.models.ReseniaModel;
import com.tfg.terranostra.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a las resenias en la base de datos.
 */
@Repository
public interface ReseniaRepository extends JpaRepository<ReseniaModel, Long> {

    /**
     * Verifica si ya existe una resenia para un producto hecha por un usuario.
     *
     * @param usuario  Usuario que hizo la resenia.
     * @param producto Producto al que se refiere la resenia.
     * @return true si ya existe, false si no.
     */
    boolean existsByUsuarioAndProducto(UsuarioModel usuario, ProductoModel producto);
}
