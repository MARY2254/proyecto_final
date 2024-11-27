package pe.edu.upeu.parisfx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Almacen;
import pe.edu.upeu.parisfx.repositorio.AlmacenRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository repo;
    Logger logger= LoggerFactory.getLogger(ProductoService.class);


    /**
     * List all available Almacen entries.
     */
    public List<Almacen> list() {
        return repo.findAll();
    }

    /**
     * Save a new or existing Almacen entry.
     */
    public Almacen save(Almacen Almacen) {
        return repo.save(Almacen);
    }

    public Almacen update(Almacen to){
        return repo.save(to);
    }


    /**
     * Search for a Almacen entry by its ID.
     */
    public Almacen searchById(Long id){
        return repo.findById(id).orElse(null);
    }

    /**
     * Delete a Almacen entry by its ID.
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Retrieve all Almacen entries as ComboBox options.
     */
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        ComboBoxOption cb;
        for (Almacen cate : repo.findAll()) {
            cb = new ComboBoxOption();
                    cb.setKey(String.valueOf(cate.getIdAlmacen()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
