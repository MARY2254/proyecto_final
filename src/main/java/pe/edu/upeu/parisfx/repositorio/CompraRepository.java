package pe.edu.upeu.parisfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.parisfx.modelo.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
