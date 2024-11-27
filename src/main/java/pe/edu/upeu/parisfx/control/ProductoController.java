package pe.edu.upeu.parisfx.control;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.parisfx.componente.*;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Marca;
import pe.edu.upeu.parisfx.modelo.Producto;
import pe.edu.upeu.parisfx.modelo.Proveedor;
import pe.edu.upeu.parisfx.servicio.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ProductoController {

    @FXML
    private TextField txtNombreProducto, txtPUnit, txtPUnitOld, txtUtilidad, txtStock, txtStockOld, txtFiltroDato;
    @FXML
    private ComboBox<ComboBoxOption> cbxMarca, cbxProovedor, cbxAlmacen;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;

    private Stage stage;

    @Autowired
    private MarcaService ms;
    @Autowired
    private ProveedorService cs;
    @Autowired
    private ProductoService ps;
    @Autowired
    private AlmacenService ums;

    private Validator validator;
    private ObservableList<Producto> listarProducto;
    private Producto formulario;
    private Long idProductoCE = 0L;

    @FXML
    public void initialize() {
        setupStageTitleListener();
        initializeComboBoxes();
        initializeValidator();
        setupTableView();
        listar();
    }

    private void setupStageTitleListener() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void initializeComboBoxes() {
        setupComboBox(cbxMarca, ms.listarCombobox());
        setupComboBox(cbxProovedor, cs.listarCombobox());
        setupComboBox(cbxAlmacen, ums.listarCombobox());
    }

    private void setupComboBox(ComboBox<ComboBoxOption> comboBox, List<ComboBoxOption> items) {
        comboBox.setTooltip(new Tooltip());
        comboBox.getItems().addAll(items);
        comboBox.setOnAction(event -> {
            ComboBoxOption selectedItem = comboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                System.out.println("ID seleccionado: " + selectedItem.getKey());
            }
        });
        new ComboBoxAutoComplete<>(comboBox);
    }

    private void initializeValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private void setupTableView() {
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();

        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0));
        columns.put("Nombre Producto", new ColumnInfo("nombre", 200.0));
        columns.put("P. Unitario", new ColumnInfo("pu", 150.0));
        columns.put("Utilidad", new ColumnInfo("utilidad", 100.0));
        columns.put("Marca", new ColumnInfo("marca.nombre", 200.0));
        columns.put("Proveedor", new ColumnInfo("proveedor.nombresRaso", 200.0));
        columns.put("Almacén", new ColumnInfo("almacen.nombre", 150.0));

        Consumer<Producto> updateAction = this::editForm;
        Consumer<Producto> deleteAction = producto -> {
            ps.delete(producto.getIdProducto());
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
    }

    @FXML
    public void listar() {
        try {
            listarProducto = FXCollections.observableArrayList(ps.list());
            tableView.setItems(listarProducto);

            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> filtrarProductos(newValue));
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    private void filtrarProductos(String filtro) {
        ObservableList<Producto> productosFiltrados = listarProducto.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableView.setItems(productosFiltrados);
    }

    @FXML
    public void clearForm() {
        txtNombreProducto.clear();
        txtPUnit.clear();
        txtPUnitOld.clear();
        txtUtilidad.clear();
        txtStock.clear();
        txtStockOld.clear();
        cbxMarca.getSelectionModel().clearSelection();
        cbxProovedor.getSelectionModel().clearSelection();
        cbxAlmacen.getSelectionModel().clearSelection();
        idProductoCE = 0L;
        limpiarError();
    }

    @FXML
    public void limpiarError() {
        lbnMsg.setText("");
        txtNombreProducto.getStyleClass().remove("text-field-error");
        txtPUnit.getStyleClass().remove("text-field-error");
        txtPUnitOld.getStyleClass().remove("text-field-error");
        txtUtilidad.getStyleClass().remove("text-field-error");
        txtStock.getStyleClass().remove("text-field-error");
        txtStockOld.getStyleClass().remove("text-field-error");
        cbxMarca.getStyleClass().remove("text-field-error");
        cbxProovedor.getStyleClass().remove("text-field-error");
        cbxAlmacen.getStyleClass().remove("text-field-error");
    }

    @FXML
    public void validarFormulario() {
        formulario = new Producto();
        try {
            formulario.setNombre(txtNombreProducto.getText());
            formulario.setPu(new BigDecimal(txtPUnit.getText().isEmpty() ? "0" : txtPUnit.getText()));
            formulario.setPuOld(new BigDecimal(txtPUnitOld.getText().isEmpty() ? "0" : txtPUnitOld.getText()));
            formulario.setUtilidad(Double.parseDouble(txtUtilidad.getText().isEmpty() ? "0" : txtUtilidad.getText()));
            formulario.setStock(Double.parseDouble(txtStock.getText().isEmpty() ? "0" : txtStock.getText()));
            formulario.setStockOld(Double.parseDouble(txtStockOld.getText().isEmpty() ? "0" : txtStockOld.getText()));
            formulario.setMarca(ms.searchById(getSelectedKey(cbxMarca)));
            formulario.setProveedor(cs.searchById(getSelectedKey(cbxProovedor)));
            formulario.setAlmacen(ums.searchById(getSelectedKey(cbxAlmacen)));

            Set<ConstraintViolation<Producto>> violations = validator.validate(formulario);
            if (!violations.isEmpty()) {
                mostrarErrores(violations);
                return;
            }

            ps.save(formulario);
            listar();
            clearForm();
            Toast.showToast(stage, "Producto guardado correctamente.", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        } catch (Exception e) {
            lbnMsg.setText("Error en el formulario: " + e.getMessage());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        }
    }

    private Long getSelectedKey(ComboBox<ComboBoxOption> comboBox) {
        ComboBoxOption selectedOption = comboBox.getSelectionModel().getSelectedItem();
        return selectedOption != null ? Long.parseLong(selectedOption.getKey()) : 0L;
    }

    private void mostrarErrores(Set<ConstraintViolation<Producto>> violations) {
        limpiarError();
        violations.forEach(violacion -> {
            String campo = violacion.getPropertyPath().toString();
            String mensaje = violacion.getMessage();
            lbnMsg.setText(mensaje);
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            switch (campo) {

            }
        });
    }

    private void editForm(Producto producto) {
        // Método de edición del formulario
        idProductoCE = producto.getIdProducto();
        txtNombreProducto.setText(producto.getNombre());
        txtPUnit.setText(String.valueOf(producto.getPu()));
        txtPUnitOld.setText(String.valueOf(producto.getPuOld()));
        txtUtilidad.setText(String.valueOf(producto.getUtilidad()));
        txtStock.setText(String.valueOf(producto.getStock()));
        txtStockOld.setText(String.valueOf(producto.getStockOld()));

        cbxMarca.getSelectionModel().select(new ComboBoxOption(String.valueOf(producto.getMarca().getIdMarca()), producto.getMarca().getNombre()));
        cbxProovedor.getSelectionModel().select(new ComboBoxOption(String.valueOf(producto.getProveedor().getIdProveedor()), producto.getProveedor().getNombresRaso()));
        cbxAlmacen.getSelectionModel().select(new ComboBoxOption(String.valueOf(producto.getAlmacen().getIdAlmacen()), producto.getAlmacen().getNombre()));
    }
}
