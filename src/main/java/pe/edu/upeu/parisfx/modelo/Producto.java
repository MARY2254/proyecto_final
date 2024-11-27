package pe.edu.upeu.parisfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Positive(message = "El Precio Unitario debe ser positivo")
    @Column(name = "pu", nullable = false)
    private BigDecimal pu;

    @PositiveOrZero(message = "El Precio Unitario Anterior debe ser positivo o cero")
    @Column(name = "puold", nullable = false)
    private BigDecimal puOld;

    @Positive(message = "La utilidad debe ser positiva")
    @Column(name = "utilidad", nullable = false)
    private Double utilidad;

    @Positive(message = "El Stock debe ser positivo o cero")
    @Column(name = "stock", nullable = false)
    private Double stock;

    @PositiveOrZero(message = "El Stock Anterior debe ser positivo o cero")
    @Column(name = "stockold", nullable = false)
    private Double stockOld;

    @NotNull(message = "Proveedor no puede estar vacío")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor", nullable = false, foreignKey = @ForeignKey(name = "FK_PROVEEDOR_PRODUCTO"))
    private Proveedor proveedor;

    @NotNull(message = "Marca no puede estar vacío")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", referencedColumnName = "id_marca", nullable = false, foreignKey = @ForeignKey(name = "FK_MARCA_PRODUCTO"))
    private Marca marca;

    @NotNull(message = "Almacen no puede estar vacío")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Almacen", referencedColumnName = "id_Almacen", nullable = false, foreignKey = @ForeignKey(name = "FK_ALMACEN_PRODUCTO"))
    private Almacen almacen;
}
