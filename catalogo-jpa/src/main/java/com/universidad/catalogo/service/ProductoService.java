package com.universidad.catalogo.service;

import com.universidad.catalogo.model.Categoria;
import com.universidad.catalogo.model.Producto;
import com.universidad.catalogo.repository.CategoriaRepository;
import com.universidad.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<Producto> listarTodos() {
        return productoRepo.findAllConCategoria();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        p.getCategoria().getId();   // inicializa la relación LAZY dentro de la transacción
        return p;
    }

    @Transactional
    public Producto guardar(Producto producto, Long categoriaId) {
        Categoria categoria = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaId));

        Producto destino = producto;
        if (producto.getId() != null) {
            // Edición: se trabaja sobre la entidad gestionada y se copian los campos
            destino = productoRepo.findById(producto.getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + producto.getId()));
            destino.setNombre(producto.getNombre());
            destino.setPrecio(producto.getPrecio());
            destino.setStock(producto.getStock());
        }
        destino.asignarCategoria(categoria);
        return productoRepo.save(destino);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepo.deleteById(id);
    }

    public List<Producto> listarPorCategoriaConPrecioMayorA(Long categoriaId, BigDecimal precioMinimo) {
        return productoRepo.buscarPorCategoriaConPrecioMayorA(categoriaId, precioMinimo);
    }
}