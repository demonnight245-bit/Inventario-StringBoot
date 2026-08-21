package co.edu.sena.Inventario.controller;

import co.edu.sena.Inventario.model.Producto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    // Lista en memoria simulando la base de datos
    private List<Producto> listaProductos = new ArrayList<>(Arrays.asList(
        new Producto(1L, "Tomate", 2500.0, 15, "Hortalizas"),
        new Producto(2L, "Papa", 1500.0, 5, "Tubérculos"),
        new Producto(3L, "Fresa", 6000.0, 3, "Frutas"),
        new Producto(4L, "Zanahoria", 3000.0, 20, "Hortalizas")
    ));

    private Long contadorId = 5L;

    // --- CRUD BÁSICO ---

    // Reto 1: GET /productos (Obtener todos)
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(listaProductos);
    }

    // Reto 6: GET /productos/{id} (Responder 404 cuando no existe)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return listaProductos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Devuelve 404 Not Found
    }

    // Reto 5 y 7: POST /productos (Validación con @Valid + Respuesta 201 Created)
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        producto.setId(contadorId++);
        listaProductos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto); // Devuelve 201 Created
    }

    // PUT /productos/{id} (Actualizar producto existente)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoActualizado) {
        for (Producto p : listaProductos) {
            if (p.getId().equals(id)) {
                p.setNombre(productoActualizado.getNombre());
                p.setPrecio(productoActualizado.getPrecio());
                p.setCantidad(productoActualizado.getCantidad());
                p.setCategoria(productoActualizado.getCategoria());
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /productos/{id} (Eliminar producto)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = listaProductos.removeIf(p -> p.getId().equals(id));
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- RETOS DEL TALLER ---

    // Reto 2: GET /productos/buscar?nombre=tomate
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> resultado = listaProductos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // Reto 3: GET /productos/categoria?nombre=Hortalizas
    @GetMapping("/categoria")
    public ResponseEntity<List<Producto>> filtrarPorCategoria(@RequestParam String nombre) {
        List<Producto> resultado = listaProductos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // Reto 4: GET /productos/precio?maximo=5000
    @GetMapping("/precio")
    public ResponseEntity<List<Producto>> filtrarPorPrecioMaximo(@RequestParam Double maximo) {
        List<Producto> resultado = listaProductos.stream()
                .filter(p -> p.getPrecio() <= maximo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // Reto 8: GET /productos/stock-bajo (Cantidad < 10)
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> obtenerStockBajo() {
        List<Producto> resultado = listaProductos.stream()
                .filter(p -> p.getCantidad() < 10)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // Reto 9: GET /productos/resumen
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();

        if (listaProductos.isEmpty()) {
            resumen.put("totalProductos", 0);
            resumen.put("productosStockBajo", 0);
            resumen.put("productoMasCostoso", "N/A");
            resumen.put("productoMasEconomico", "N/A");
            return ResponseEntity.ok(resumen);
        }

        long stockBajoCount = listaProductos.stream().filter(p -> p.getCantidad() < 10).count();
        Producto masCostoso = Collections.max(listaProductos, Comparator.comparing(Producto::getPrecio));
        Producto masEconomico = Collections.min(listaProductos, Comparator.comparing(Producto::getPrecio));

        resumen.put("totalProductos", listaProductos.size());
        resumen.put("productosStockBajo", stockBajoCount);
        resumen.put("productoMasCostoso", masCostoso.getNombre());
        resumen.put("productoMasEconomico", masEconomico.getNombre());

        return ResponseEntity.ok(resumen);
    }

    // Reto 15 (Reto final): GET /productos/filtrar?categoria=Hortalizas&precioMaximo=5000
    @GetMapping("/filtrar")
    public ResponseEntity<List<Producto>> filtrarCombinado(
            @RequestParam String categoria,
            @RequestParam Double precioMaximo) {
        List<Producto> resultado = listaProductos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria) && p.getPrecio() <= precioMaximo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}