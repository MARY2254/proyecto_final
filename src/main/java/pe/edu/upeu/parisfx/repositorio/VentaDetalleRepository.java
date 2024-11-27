package pe.edu.upeu.parisfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.parisfx.modelo.VentaDetalle;

@Repository
public interface VentaDetalleRepository  extends JpaRepository<VentaDetalle, Long> {
}
