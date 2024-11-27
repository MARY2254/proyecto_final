package pe.edu.upeu.parisfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Proveedor;
import pe.edu.upeu.parisfx.repositorio.ProveedorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository repo;

    @Autowired
    public ProveedorService(ProveedorRepository repo) {
        this.repo = repo;
    }

    // Guarda un nuevo proveedor
    public Proveedor save(Proveedor proveedor) {
        return repo.save(proveedor);
    }

    // Lista todos los proveedores
    public List<Proveedor> list() {
        return repo.findAll();
    }

    // Actualiza un proveedor existente
    public Optional<Proveedor> update(Proveedor updatedProveedor, Long id) {
        return repo.findById(id).map(existingProveedor -> {
            existingProveedor.setDniRuc(updatedProveedor.getDniRuc());
            existingProveedor.setNombresRaso(updatedProveedor.getNombresRaso());
            existingProveedor.setTipoDoc(updatedProveedor.getTipoDoc());
            existingProveedor.setCelular(updatedProveedor.getCelular());
            existingProveedor.setEmail(updatedProveedor.getEmail());
            existingProveedor.setDireccion(updatedProveedor.getDireccion());
            return repo.save(existingProveedor);
        });
    }

    // Elimina un proveedor por su ID
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ProveedorNotFoundException("Proveedor no encontrado con el ID: " + id);
        }
        repo.deleteById(id);
    }

    // Busca un proveedor por su ID
    public Proveedor searchById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ProveedorNotFoundException("Proveedor no encontrado con el ID: " + id));
    }

    // Lista proveedores en formato de ComboBox (id y nombre/razón social)
    public List<ComboBoxOption> listarCombobox() {
        return repo.findAll().stream()
                .map(proveedor -> new ComboBoxOption(String.valueOf(proveedor.getIdProveedor()), proveedor.getNombresRaso()))
                .toList();
    }

    public static class ProveedorNotFoundException extends RuntimeException {
        public ProveedorNotFoundException(String message) {
            super(message);
        }
}}
