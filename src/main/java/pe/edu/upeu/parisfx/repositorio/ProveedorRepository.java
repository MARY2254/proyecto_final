package pe.edu.upeu.parisfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.parisfx.modelo.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Métodos adicionales si es necesario
}
