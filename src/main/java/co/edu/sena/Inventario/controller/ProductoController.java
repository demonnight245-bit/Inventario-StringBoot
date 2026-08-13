package co.edu.sena.Inventario.controller;

import co.edu.sena.Inventario.model.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final List<Producto> productos = List.of(
        new Producto(1L, "Papa pastusa", 2500.0, 50),
        new Producto(2L, "Tomate de árbol", 3200.0, 30),
        new Producto(3L, "Fresa", 8500.0, 20)
    );

    @GetMapping
    public List<Producto> listarProductos() {
        return productos;
    }

    @GetMapping("/{id}")
    public Producto buscarProducto(@PathVariable Long id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }
}