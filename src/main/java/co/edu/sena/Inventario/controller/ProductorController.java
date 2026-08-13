package co.edu.sena.Inventario.controller;

import co.edu.sena.Inventario.model.Productor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productores")
public class ProductorController {

    private final List<Productor> productores = List.of(
        new Productor(1L, "Asociación Finca El Sol", "Mosquera"),
        new Productor(2L, "Cultivos San José", "Madrid"),
        new Productor(3L, "Granja La Esperanza", "Funza")
    );

    @GetMapping
    public List<Productor> listarProductores() {
        return productores;
    }

    @GetMapping("/{id}")
    public Productor buscarProductor(@PathVariable Long id) {
        for (Productor productor : productores) {
            if (productor.getId().equals(id)) {
                return productor;
            }
        }
        return null;
    }
}