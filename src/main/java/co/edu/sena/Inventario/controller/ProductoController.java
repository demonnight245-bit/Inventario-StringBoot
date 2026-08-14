package co.edu.sena.Inventario.controller;

import co.edu.sena.Inventario.model.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final List<Producto> productos = new ArrayList<>(List.of(
        new Producto(1L, "Papa pastusa", "Tuberculos", 2500.0, 50),
        new Producto(2L, "Tomate de árbol", "Frutas", 3200.0, 30),
        new Producto(3L, "Fresa", "Frutas", 8500.0, 20),
        new Producto(4L, "Lechuga crespa", "Hortalizas", 1800.0, 40),
        new Producto(5L, "Zanahoria", "Hortalizas", 2100.0, 60)
    ));


    // Ejemplos: 
    // - /productos
    // - /productos?categoria=Frutas
    // - /productos?nombre=Papa

    @GetMapping
    public List<Producto> listarOFiltrarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria) {

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            boolean coincideNombre = (nombre == null) || 
                producto.getNombre().toLowerCase().contains(nombre.toLowerCase());

            boolean coincideCategoria = (categoria == null) || 
                producto.getCategoria().equalsIgnoreCase(categoria);

            if (coincideNombre && coincideCategoria) {
                resultado.add(producto);
            }
        }

        return resultado;
    }


    // URL: http://localhost:8080/productos/valor-total

    @GetMapping("/valor-total")
    public String calcularValorTotalInventario() {
        double total = 0.0;
        int totalUnidades = 0;

        for (Producto producto : productos) {
            total += producto.getPrecio() * producto.getCantidad();
            totalUnidades += producto.getCantidad();
        }

        return String.format(
            "El valor total del inventario es de $%.2f COP para un total de %d unidades.",
            total, totalUnidades
        );
    }

    // URL Ejemplo: http://localhost:8080/productos/1/descuento?porcentaje=15
    @GetMapping("/{id}/descuento")
    public String calcularDescuento(
            @PathVariable Long id, 
            @RequestParam Double porcentaje) {

        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                double descuento = producto.getPrecio() * (porcentaje / 100.0);
                double precioConDescuento = producto.getPrecio() - descuento;

                return String.format(
                    "Producto: %s | Precio original: $%.2f | Descuento (%s%%): -$%.2f | Precio final: $%.2f",
                    producto.getNombre(), producto.getPrecio(), porcentaje, descuento, precioConDescuento
                );
            }
        }

        return "Producto no encontrado.";
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
                producto.setCategoria(productoActualizado.getCategoria());
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