package com.tfg.terranostra.controllers;

import com.tfg.terranostra.dto.CrearPedidoDto;
import com.tfg.terranostra.dto.PedidoDto;
import com.tfg.terranostra.models.PedidoModel;
import com.tfg.terranostra.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*") // Ajusta si necesitas restringir CORS
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoDto> crearPedido(@RequestBody CrearPedidoDto dto) {
        PedidoModel nuevoPedido = pedidoService.crearPedido(dto);
        PedidoDto respuesta = pedidoService.convertirAPedidoDto(nuevoPedido);
        return ResponseEntity.ok(respuesta);
    }
}