package pe.edu.upeu.parisfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Perfil;
import pe.edu.upeu.parisfx.repositorio.PerfilRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilService {
    @Autowired
    PerfilRepository repo;

    public Perfil save(Perfil to) {
        return repo.save(to);
    }

    public List<Perfil> list() {
        return repo.findAll();
    }

    public Perfil update(Perfil to, Long id) {
        try {
            Perfil toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setNombre(to.getNombre());
                return repo.save(toe);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<ComboBoxOption> listaPerfilCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (Perfil perf : repo.findAll()) {
            listar.add(new ComboBoxOption(String.valueOf(perf.getIdPerfil()),
                    perf.getNombre()
            ));
        }
        return listar;
    }

    public Perfil searchById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
