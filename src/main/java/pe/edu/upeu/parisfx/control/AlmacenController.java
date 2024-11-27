package pe.edu.upeu.parisfx.control;

import jakarta.validation.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.parisfx.componente.ColumnInfo;
import pe.edu.upeu.parisfx.componente.TableViewHelper;
import pe.edu.upeu.parisfx.componente.Toast;
import pe.edu.upeu.parisfx.modelo.Almacen;
import pe.edu.upeu.parisfx.servicio.AlmacenService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class AlmacenController {

    @FXML
    private TextField txtNombreAlmacen, txtDescripcion;
    @FXML
    private TableView<Almacen> almacenTable;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;

    private Stage stage;

    @Autowired
    private AlmacenService fs;

    private Validator validator;
    private ObservableList<Almacen> listarAlmacen;
    private Long idAlmacenCE = 0L;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            if (miContenedor != null) {
                stage = (Stage) miContenedor.getScene().getWindow();
                if (stage != null) {
                    System.out.println("El título del stage es: " + stage.getTitle());
                } else {
                    System.out.println("Stage aún no disponible.");
                }
            } else {
                System.out.println("miContenedor aún no está inicializado.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Almacen> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idAlmacen", 60.0));
        columns.put("Nombre Almacen", new ColumnInfo("nombre", 200.0));
        columns.put("Descripción", new ColumnInfo("descripcion", 300.0));

        Consumer<Almacen> updateAction = this::editForm;
        Consumer<Almacen> deleteAction = almacen -> {
            fs.delete(almacen.getIdAlmacen());
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, width, height);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(almacenTable, columns, updateAction, deleteAction);

        almacenTable.setTableMenuButtonVisible(true);
        listar();
    }

    @FXML
    public void listar() {
        try {
            almacenTable.getItems().clear();
            listarAlmacen = FXCollections.observableArrayList(fs.list());
            almacenTable.getItems().addAll(listarAlmacen);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void limpiarError() {
        txtNombreAlmacen.getStyleClass().remove("text-field-error");
        txtDescripcion.getStyleClass().remove("text-field-error");
    }

    @FXML
    public void clearForm() {
        txtNombreAlmacen.clear();
        txtDescripcion.clear();
        idAlmacenCE = 0L;
        limpiarError();
    }

    @FXML
    public void validarFormulario() {
        String nombre = txtNombreAlmacen.getText();
        String descripcion = txtDescripcion.getText();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            try {
                Almacen nuevoAlmacen = new Almacen();
                nuevoAlmacen.setNombre(nombre);
                nuevoAlmacen.setDescripcion(descripcion);

                double width = stage.getWidth() / 1.5;
                double height = stage.getHeight() / 2;

                if (idAlmacenCE != 0L) {
                    nuevoAlmacen.setIdAlmacen(idAlmacenCE);
                    fs.update(nuevoAlmacen);
                    Toast.showToast(stage, "Se actualizó correctamente!", 2000, width, height);
                } else {
                    fs.save(nuevoAlmacen);
                    Toast.showToast(stage, "Se guardó correctamente!", 2000, width, height);
                }

                clearForm();
                listar();
                lbnMsg.setText("Formulario válido");
                lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                limpiarError();
            } catch (Exception e) {
                lbnMsg.setText("Error al guardar: " + e.getMessage());
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            }
        } else {
            lbnMsg.setText("Por favor, complete todos los campos");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    public void editForm(Almacen almacen) {
        txtNombreAlmacen.setText(almacen.getNombre());
        txtDescripcion.setText(almacen.getDescripcion());
        idAlmacenCE = almacen.getIdAlmacen();
        limpiarError();
    }
}
