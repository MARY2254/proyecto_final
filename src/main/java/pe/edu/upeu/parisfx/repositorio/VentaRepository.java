package pe.edu.upeu.parisfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.parisfx.modelo.Venta;

@Repository
public interface VentaRepository  extends JpaRepository<Venta, Long> {
}
