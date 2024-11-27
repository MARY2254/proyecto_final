package pe.edu.upeu.parisfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.parisfx.modelo.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

}
