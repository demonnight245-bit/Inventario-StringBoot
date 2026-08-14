package co.edu.sena.Inventario.controller;

import co.edu.sena.Inventario.model.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    
    private final List<Producto> productos = new ArrayList<>(List.of(
        new Producto(1L, "Papa pastusa", 2500.0, 50),
        new Producto(2L, "Tomate de árbol", 3200.0, 30),
        new Producto(3L, "Fresa", 8500.0, 20),
        new Producto(4L, "Lechuga crespa", 1800.0, 40),
        new Producto(5L, "Zanahoria", 2100.0, 60)
    ));

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

    @PostMapping
    public Producto crearProducto(@RequestBody Producto nuevoProducto) {
        productos.add(nuevoProducto);
        return nuevoProducto;
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                producto.setNombre(productoActualizado.getNombre());
                producto.setPrecio(productoActualizado.getPrecio());
                producto.setCantidad(productoActualizado.getCantidad());
                return producto;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(id)) {
                productos.remove(i);
                return "Producto con ID " + id + " eliminado correctamente.";
            }
        }
        return "Producto no encontrado.";
    }
}