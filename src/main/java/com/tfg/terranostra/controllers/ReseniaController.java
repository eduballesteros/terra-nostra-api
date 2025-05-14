package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.CrearReseniaDto;
import com.tfg.terranostra.dto.ReseniaDto;
import com.tfg.terranostra.models.ReseniaModel;
import com.tfg.terranostra.services.ReseniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar operaciones relacionadas con las resenias de productos.
 */
@RestController
@RequestMapping("/api/resenias")
@CrossOrigin(origins = "*")
public class ReseniaController {

    @Autowired
    private ReseniaService reseniaService;

    /**
     * Crea una nueva resenia asociada a un producto y usuario.
     *
     * @param dto Datos de la resenia recibidos desde el frontend.
     * @return ReseniaDto con la información de la resenia guardada.
     */
    @PostMapping
    public ResponseEntity<ReseniaDto> crearResenia(@RequestBody CrearReseniaDto dto) {
        ReseniaModel nueva = reseniaService.crearResenia(dto);
        ReseniaDto respuesta = reseniaService.convertirAReseniaDto(nueva);
        return ResponseEntity.ok(respuesta);
    }

    // Más adelante puedes añadir:
    // - GET /api/resenias/producto/{id}
    // - DELETE /api/resenias/{id}
    // - GET /api/resenias/usuario/{id}
}
