package com.tfg.terranostra.services;

import com.tfg.terranostra.dto.CrearPedidoDto;
import com.tfg.terranostra.dto.PedidoDto;
import com.tfg.terranostra.dto.ProductoCantidadDto;
import com.tfg.terranostra.dto.ProductoPedidoDto;
import com.tfg.terranostra.models.*;
import com.tfg.terranostra.repositories.PedidoRepository;
import com.tfg.terranostra.repositories.ProductoRepository;
import com.tfg.terranostra.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Crea y guarda un nuevo pedido en base a los datos recibidos desde el frontend.
     *
     * - Busca al usuario por ID.
     * - Construye un PedidoModel con los datos de contacto, método de pago y productos.
     * - Calcula el total multiplicando precio × cantidad por cada producto.
     * - Asocia los productos al pedido mediante ProductoPedidoModel.
     * - Guarda el pedido completo en la base de datos.
     *
     * @param dto Objeto CrearPedidoDto con la información del pedido a crear.
     * @return El PedidoModel ya guardado en la base de datos.
     */

    public PedidoModel crearPedido(CrearPedidoDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        PedidoModel pedido = new PedidoModel();
        pedido.setUsuario(usuario);
        pedido.setEmailUsuario(dto.getEmailUsuario());
        pedido.setFecha(LocalDateTime.now());
        pedido.setMetodoPago(dto.getMetodoPago());
        pedido.setDireccionEnvio(dto.getDireccionEnvio());
        pedido.setTelefonoContacto(dto.getTelefonoContacto());
        pedido.setEstado("PENDIENTE");

        List<ProductoPedidoModel> listaProductos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ProductoCantidadDto item : dto.getProductos()) {
            ProductoModel producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            ProductoPedidoModel productoPedido = new ProductoPedidoModel();
            productoPedido.setPedido(pedido);
            productoPedido.setProducto(producto);
            productoPedido.setCantidad(item.getCantidad());

            listaProductos.add(productoPedido);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setListaProductos(listaProductos);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    /**
     * Convierte un PedidoModel (entidad de base de datos) en un PedidoDto limpio
     * para su envío al frontend.
     *
     * - Extrae los campos básicos del pedido.
     * - Transforma la lista de ProductoPedidoModel en ProductoPedidoDto.
     *
     * @param pedido PedidoModel a convertir.
     * @return Objeto PedidoDto listo para ser enviado al frontend.
     */


    public PedidoDto convertirAPedidoDto(PedidoModel pedido) {
        PedidoDto dto = new PedidoDto();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setNombreUsuario(pedido.getUsuario().getNombre());
        dto.setEmailUsuario(pedido.getUsuario().getEmail());
        dto.setFecha(pedido.getFecha());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setTelefonoContacto(pedido.getTelefonoContacto());

        List<ProductoPedidoDto> productosDto = new ArrayList<>();
        for (ProductoPedidoModel productoPedido : pedido.getListaProductos()) {
            ProductoPedidoDto pDto = new ProductoPedidoDto();
            pDto.setProductoId(productoPedido.getProducto().getId());
            pDto.setCantidad(productoPedido.getCantidad());
            // pDto.setNombreProducto(productoPedido.getProducto().getNombre()); // opcional
            productosDto.add(pDto);
        }

        dto.setProductos(productosDto);
        return dto;
    }
}
