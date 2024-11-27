package pe.edu.upeu.parisfx.control;

import jakarta.validation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Proveedor;
import pe.edu.upeu.parisfx.repositorio.ProveedorRepository;
import pe.edu.upeu.parisfx.servicio.ProveedorService;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @FXML
    private TextField txtDniRuc, txtNombresRaso, txtTipoDoc, txtCelular, txtEmail, txtDireccion;

    @FXML
    private TableView<Proveedor> tableViewProveedores;
    @FXML
    private TableColumn<Proveedor, String> colDniRuc;
    @FXML
    private TableColumn<Proveedor, String> colNombresRaso;
    @FXML
    private TableColumn<Proveedor, String> colTipoDoc;
    @FXML
    private TableColumn<Proveedor, String> colCelular;
    @FXML
    private TableColumn<Proveedor, String> colEmail;
    @FXML
    private TableColumn<Proveedor, String> colDireccion;

    @FXML
    private Label lbnMsg;

    @FXML
    private AnchorPane miproveedor;

    private Long idProveedorCE = 0L;
    private Validator validator;

    @FXML
    public void initialize() {
        // Configuración del validador
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Configuración de la tabla
        configureTable();
        loadProveedores();
    }

    private void configureTable() {
        colDniRuc.setCellValueFactory(new PropertyValueFactory<>("dniRuc"));
        colNombresRaso.setCellValueFactory(new PropertyValueFactory<>("nombresRaso"));
        colTipoDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDoc"));
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        tableViewProveedores.setTableMenuButtonVisible(true);
    }

    private void loadProveedores() {
        List<Proveedor> proveedores = proveedorService.list();
        ObservableList<Proveedor> observableProveedores = FXCollections.observableArrayList(proveedores);
        tableViewProveedores.setItems(observableProveedores);
    }

    private boolean validateInputs() {
        if (txtDniRuc.getText().isEmpty() || txtNombresRaso.getText().isEmpty()) {
            lbnMsg.setText("DNI/RUC y Nombres son campos obligatorios.");
            lbnMsg.setStyle("-fx-text-fill: red;");
            return false;
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setDniRuc(txtDniRuc.getText());
        proveedor.setNombresRaso(txtNombresRaso.getText());
        proveedor.setTipoDoc(txtTipoDoc.getText());
        proveedor.setCelular(txtCelular.getText());
        proveedor.setEmail(txtEmail.getText());
        proveedor.setDireccion(txtDireccion.getText());

        Set<ConstraintViolation<Proveedor>> violations = validator.validate(proveedor);
        if (!violations.isEmpty()) {
            displayValidationErrors(violations);
            return false;
        }
        return true;
    }

    private void displayValidationErrors(Set<ConstraintViolation<Proveedor>> violations) {
        StringBuilder sb = new StringBuilder("Errores de validación:\n");
        for (ConstraintViolation<Proveedor> violation : violations) {
            sb.append("- ").append(violation.getMessage()).append("\n");
        }
        lbnMsg.setText(sb.toString());
        lbnMsg.setStyle("-fx-text-fill: red;");
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setDniRuc(txtDniRuc.getText());
        proveedor.setNombresRaso(txtNombresRaso.getText());
        proveedor.setTipoDoc(txtTipoDoc.getText());
        proveedor.setCelular(txtCelular.getText());
        proveedor.setEmail(txtEmail.getText());
        proveedor.setDireccion(txtDireccion.getText());

        try {
            if (idProveedorCE != 0L) {
                proveedor.setIdProveedor(idProveedorCE);
                proveedorService.update(proveedor, idProveedorCE);
                lbnMsg.setText("Proveedor actualizado correctamente.");
            } else {
                proveedorService.save(proveedor);
                lbnMsg.setText("Proveedor guardado correctamente.");
            }
            lbnMsg.setStyle("-fx-text-fill: green;");
            clearFields();
            loadProveedores();
        } catch (Exception e) {
            lbnMsg.setText("Error al guardar: " + e.getMessage());
            lbnMsg.setStyle("-fx-text-fill: red;");
        }
    }

    private void clearFields() {
        txtDniRuc.clear();
        txtNombresRaso.clear();
        txtTipoDoc.clear();
        txtCelular.clear();
        txtEmail.clear();
        txtDireccion.clear();
        idProveedorCE = 0L;
    }

    @FXML
    private void handleDelete() {
        Proveedor selectedProveedor = tableViewProveedores.getSelectionModel().getSelectedItem();
        if (selectedProveedor == null) {
            lbnMsg.setText("Seleccione un proveedor para eliminar.");
            lbnMsg.setStyle("-fx-text-fill: red;");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("Está a punto de eliminar un proveedor.");
        alert.setContentText("¿Está seguro de que desea continuar?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            proveedorService.delete(selectedProveedor.getIdProveedor());
            loadProveedores();
            lbnMsg.setText("Proveedor eliminado correctamente.");
            lbnMsg.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML
    private void handleEdit() {
        Proveedor selectedProveedor = tableViewProveedores.getSelectionModel().getSelectedItem();
        if (selectedProveedor == null) {
            lbnMsg.setText("Seleccione un proveedor para editar.");
            lbnMsg.setStyle("-fx-text-fill: red;");
            return;
        }

        txtDniRuc.setText(selectedProveedor.getDniRuc());
        txtNombresRaso.setText(selectedProveedor.getNombresRaso());
        txtTipoDoc.setText(selectedProveedor.getTipoDoc());
        txtCelular.setText(selectedProveedor.getCelular());
        txtEmail.setText(selectedProveedor.getEmail());
        txtDireccion.setText(selectedProveedor.getDireccion());
        idProveedorCE = selectedProveedor.getIdProveedor();

    }

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<Proveedor> saveProveedor(@Valid @RequestBody Proveedor proveedor) {
        Proveedor savedProveedor = proveedorService.save(proveedor);
        return ResponseEntity.ok(savedProveedor);
    }

    @GetMapping
    public List<Proveedor> getAllProveedores() {
        return proveedorService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Long id) {
        Proveedor proveedor = proveedorService.searchById(id);
        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@Valid @RequestBody Proveedor proveedor, @PathVariable Long id) {
        Proveedor updatedProveedor = proveedorService.update(proveedor, id)
                .orElseThrow();
        return ResponseEntity.ok(updatedProveedor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/combobox")
    public List<ComboBoxOption> getProveedorComboBox() {
        return proveedorService.listarCombobox();
    }

}