/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.Sesion;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ui_controllers.SignInViewController;

/**
 * Clase principal de la aplicación que inicia la interfaz gráfica de JavaFX.
 * Esta clase es responsable de cargar el archivo FXML y configurar la escena
 * inicial de la aplicación.
 * 
 * @author Julen Hidalgo
 */
public class Main extends javafx.application.Application {

    /**
     * Método principal que inicia la aplicación JavaFX. Este método es llamado
     * automáticamente por el lanzador de JavaFX cuando se inicia la aplicación.
     * 
     * @param stage El escenario (Stage) principal de la aplicación donde se
     *             mostrará la interfaz gráfica
     * @throws Exception Si ocurre un error al cargar el archivo FXML o
     *                  inicializar la escena
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signInView.fxml"));
        Parent root = loader.load();
        SignInViewController controller = (SignInViewController) loader.getController();
        Sesion.setStage(stage);
        controller.initStage(root);
    }

    /**
     * Punto de entrada principal de la aplicación. Este método es requerido por
     * JavaFX y es el que inicia el proceso de lanzamiento de la aplicación.
     * 
     * @param args Los argumentos de línea de comandos pasados a la aplicación
     */
    public static void main(String[] args) {
        launch(args);
    }

}
