package pe.edu.upeu.parisfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ParisfxApplication extends Application {

	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;

	public static void main(String[] args) {
		launch(args);  // Lanza la aplicación JavaFX
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ParisfxApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE); // Desactiva el servidor web
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));

		// Carga el archivo FXML y configura el controlador de Spring
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_paris.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent = fxmlLoader.load();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SysAlmacen Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		configurableApplicationContext.close();
	}
}
