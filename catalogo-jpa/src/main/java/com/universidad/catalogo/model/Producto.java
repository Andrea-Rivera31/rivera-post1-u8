package com.universidad.catalogo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock")
    private int stock;

    // Lado propietario: guarda la clave foránea categoria_id.
    // LAZY explícito: JPA carga @ManyToOne en EAGER por defecto; aquí se evita
    // traer la categoría completa en cada acceso a un producto.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Constructor vacío requerido por JPA
    public Producto() {}

    // Método helper: mantiene sincronizados los dos lados de la relación
    public void asignarCategoria(Categoria nueva) {
        if (this.categoria != null) {
            this.categoria.getProductos().remove(this);
        }
        this.categoria = nueva;
        if (nueva != null) {
            nueva.getProductos().add(this);
        }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}