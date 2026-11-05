package com.universidad.catalogo.repository;

import com.universidad.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // JOIN FETCH: trae la categoría en la misma consulta (evita el problema N+1
    // al listar, ya que categoria es LAZY)
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria")
    List<Producto> findAllConCategoria();

    // Productos de una categoría con precio mayor a un umbral, en una sola sentencia
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria c " +
            "WHERE c.id = :categoriaId AND p.precio > :precioMinimo " +
            "ORDER BY p.precio DESC")
    List<Producto> buscarPorCategoriaConPrecioMayorA(
            @Param("categoriaId") Long categoriaId,
            @Param("precioMinimo") BigDecimal precioMinimo);
}