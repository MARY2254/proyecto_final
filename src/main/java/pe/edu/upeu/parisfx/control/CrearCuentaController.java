package pe.edu.upeu.parisfx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.parisfx.componente.ColumnInfo;
import pe.edu.upeu.parisfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.parisfx.componente.TableViewHelper;
import pe.edu.upeu.parisfx.dto.ComboBoxOption;
import pe.edu.upeu.parisfx.modelo.Usuario;
import pe.edu.upeu.parisfx.servicio.PerfilService;
import pe.edu.upeu.parisfx.servicio.UsuarioService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pe.edu.upeu.parisfx.componente.Toast.showToast;

@Component
public class CrearCuentaController {

    @FXML
    TextField txtNombre, txtApellido, txtDNI, txtTelf, txtContra, txtUserName, txtFiltroDato;

    @FXML
    Label lbnMsg;

    @FXML
    ComboBox<ComboBoxOption> cbxRol;

    @FXML
    private TableView<Usuario> tableView;

    @FXML
    private AnchorPane miContenedor;

    Stage stage;

    @Autowired
    PerfilService ps;

    @Autowired
    UsuarioService us;

    private Validator validator;
    ObservableList<Usuario> listarUsuario;
    Usuario formulario;
    Long idUsuarioCE = 0L;

    public void initialize() {
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

        cbxRol.setTooltip(new Tooltip());
        cbxRol.getItems().addAll(ps.listaPerfilCombobox());
        cbxRol.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxRol.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxRol);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 60.0));
        columns.put("Nombre", new ColumnInfo("nombre", 180.0));
        columns.put("Apellido", new ColumnInfo("apellido", 180.0));
        columns.put("DNI", new ColumnInfo("dni", 100.0));
        columns.put("UserName", new ColumnInfo("user", 180.0));
        columns.put("Rol", new ColumnInfo("Perfil.nombre", 120.0));
        columns.put("Telf", new ColumnInfo("telf", 100.0));
        columns.put("Contraseña", new ColumnInfo("clave", 180.0));

        // Definir las acciones de actualizar y eliminar
        Consumer<Usuario> updateAction = (Usuario usuario) -> {
            System.out.println("Actualizar: " + usuario);
            editForm(usuario);
        };
        Consumer<Usuario> deleteAction = (Usuario usuario) -> {
            System.out.println("Eliminar: " + usuario);
            us.delete(usuario.getIdUsuario());
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            showToast(stage, "Se eliminó correctamente!!", 2000, width, height);
            listar();
        };

        // Usar el helper para agregar las columnas en el orden correcto
        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUsuario = FXCollections.observableArrayList(us.list());
            tableView.getItems().addAll(listarUsuario);
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarProductos(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpiarError() {
        txtNombre.getStyleClass().remove("text-field-error");
        txtApellido.getStyleClass().remove("text-field-error");
        txtContra.getStyleClass().remove("text-field-error");
        txtTelf.getStyleClass().remove("text-field-error");
        txtUserName.getStyleClass().remove("text-field-error");
        txtDNI.getStyleClass().remove("text-field-error");
        cbxRol.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtContra.setText("");
        txtTelf.setText("");
        txtUserName.setText("");
        txtDNI.setText("");
        cbxRol.getSelectionModel().select(null);
        idUsuarioCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Usuario> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("nombre")) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNombre.getStyleClass().add("text-field-error");
            } else if (campo.equals("apellido")) {
                erroresOrdenados.put("apellido", violacion.getMessage());
                txtApellido.getStyleClass().add("text-field-error");
            } else if (campo.equals("user")) {
                erroresOrdenados.put("user", violacion.getMessage());
                txtUserName.getStyleClass().add("text-field-error");
            } else if (campo.equals("telf")) {
                erroresOrdenados.put("telf", violacion.getMessage());
                txtTelf.getStyleClass().add("text-field-error");
            } else if (campo.equals("clave")) {
                erroresOrdenados.put("clave", violacion.getMessage());
                txtContra.getStyleClass().add("text-field-error");
            } else if (campo.equals("Perfil")) {
                erroresOrdenados.put("Perfil", violacion.getMessage());
                cbxRol.getStyleClass().add("text-field-error");
            }
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");


        }
    }

    @FXML
    public void validarFormulario() {
        formulario = new Usuario();
        formulario.setNombre(txtNombre.getText());
        formulario.setApellido(txtApellido.getText());
        formulario.setUser(txtUserName.getText());
        formulario.setClave(txtContra.getText());
        formulario.setDni(txtDNI.getText().isEmpty() ? "0" : txtDNI.getText());
        formulario.setTelf(txtTelf.getText().isEmpty() ? "0" : txtTelf.getText());

        ComboBoxOption selectedRole = cbxRol.getSelectionModel().getSelectedItem();
        String idxM = (selectedRole == null) ? "0" : selectedRole.getKey();
        formulario.setPerfil(ps.searchById(Long.parseLong(idxM)));

        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(formulario);

        List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;

            if (idUsuarioCE != 0L && idUsuarioCE > 0L) {
                formulario.setIdUsuario(idUsuarioCE);
                us.update(formulario);
                showToast(stage, "Se actualizó correctamente!", 2000, width, height);
                clearForm();
            } else {
                us.save(formulario);
                showToast(stage, "Se guardó correctamente!", 2000, width, height);
                clearForm();
            }
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    private void filtrarProductos(String filtro) {
        tableView.getItems().clear();
        List<Usuario> listaFiltrada = listarUsuario.stream()
                .filter(usuario -> usuario.getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || usuario.getApellido().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());
        tableView.getItems().addAll(listaFiltrada);
    }

    public void editForm(Usuario usuario) {
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtUserName.setText(usuario.getUser());
        txtDNI.setText(usuario.getDni());
        txtTelf.setText(usuario.getTelf());
        txtContra.setText(usuario.getClave());
        cbxRol.getSelectionModel().select(
                cbxRol.getItems().stream()
                        .filter(option -> option.getKey().equals(String.valueOf(usuario.getPerfil().getIdPerfil())))
                        .findFirst()
                        .orElse(null)
        );

        idUsuarioCE = usuario.getIdUsuario();
        limpiarError();
        lbnMsg.setText("");
    }
}
