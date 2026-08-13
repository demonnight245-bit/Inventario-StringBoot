package co.edu.sena.Inventario.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductoController {

    @GetMapping("/productos")
    public String listarProductos() {
        return "Productos agropecuarios de Sabana Occidente";
    }
}