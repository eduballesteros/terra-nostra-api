package com.tfg.terranostra.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearPedidoDto {
    private Long usuarioId;
    private String emailUsuario;
    private String metodoPago;
    private String direccionEnvio;
    private String telefonoContacto;
    private List<ProductoCantidadDto> productos;
}
