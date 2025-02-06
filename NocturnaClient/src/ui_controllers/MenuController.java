/**
 * Controlador para la ventana del menú principal en la aplicación JavaFX.
 * Gestiona la navegación entre diferentes vistas y la sesión del usuario.
 */
package ui_controllers;

import model.Sesion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.Event;
import model.User;

/**
 * Controlador para gestiona el menu de la aplicacion para moverse entre las
 * ventanas
 *
 * @author Erlantz Rey
 */
public class MenuController {

    /**
     * Barra de menú principal.
     */
    @FXML
    private MenuBar menuBar;

    /**
     * Menús dentro de la barra de menú.
     */
    @FXML
    private Menu menuVer, menuPerfil;

    /**
     * Elementos dentro de los menús.
     */
    @FXML
    private MenuItem miTickets, miEvents, miClubs, miArtists, miPerfil, miSignOut, crearAdmins;

    /**
     * Ventana principal de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario actual que a iniciado sesion
     */
    private User user;

    /**
     * Evento seleccionado en la aplicación.
     */
    private Event event;

    /**
     * Indica si el tema oscuro está activado.
     */
    private boolean tema;

    /**
     * Logger para registrar eventos y errores.
     */
    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");

    /**
     * Navega a la vista de todos los eventos.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void irShowAllEventsView(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));
            Parent root = loader.load();
            ShowAllEventsViewController controller = loader.getController();
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Navega a la vista de todos los clubes.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void irShowAllClubsView(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllClubsView.fxml"));
            Parent root = loader.load();
            ShowAllClubsViewController controller = loader.getController();
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Navega a la vista de todos los artistas.
     *
     * @param event
     */
    @FXML
    private void irShowAllArtistView(ActionEvent event) {
        try {
            stage = Sesion.getStage();
            tema = Sesion.getTema();
            user = Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllArtistsView.fxml"));
            Parent root = loader.load();
            ShowAllArtistsViewController controller = loader.getController();
            controller.setEvent(null);
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Navega a la vista de todos los tickets.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void irShowAllTicketsView(ActionEvent event) throws Exception {
        try {
            stage = Sesion.getStage();
            tema = Sesion.getTema();
            user = Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllTicketsView.fxml"));
            Parent root = loader.load();
            ShowAllTicketsViewController controller = loader.getController();
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cierra la sesión del usuario y vuelve a la ventana de inicio de sesión.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void cerrarSesion(ActionEvent event) throws Exception {
        try {
            Sesion.setUser(null);
            stage = Sesion.getStage();
            tema = Sesion.getTema();
            user = Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signInView.fxml"));
            Parent root = loader.load();
            SignInViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            Sesion.setUser(null);
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Navega a la vista de perfil del usuario.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void irVerPerfil(ActionEvent event) throws Exception {
        try {
            stage = Sesion.getStage();
            tema = Sesion.getTema();
            user = Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signUpView.fxml"));
            Parent root = loader.load();
            SignUpViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Navega a la vista de perfil del usuario.
     *
     * @param event
     * @throws Exception Si ocurre un error en la carga de la vista.
     */
    @FXML
    private void crearAdmin(ActionEvent event) throws Exception {
        try {
            stage = Sesion.getStage();
            tema = Sesion.getTema();
            user = Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signInView.fxml"));
            Parent root = loader.load();
            SignInViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void checkAdmin(Boolean isAdmin) {
        if (isAdmin != null) {
            if (isAdmin) {
                miPerfil.setVisible(false);
            } else {
                crearAdmins.setVisible(false);
            }
        }
    }

    /**
     * Establece la ventana de la aplicación.
     *
     * @param stage Ventana principal.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece el usuario actual.
     *
     * @param user Usuario autenticado.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Establece el evento actual.
     *
     * @param event Evento seleccionado.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Establece el tema de la aplicación.
     *
     * @param tema Tema de la interfaz.
     */
    public void setTema(boolean tema) {
        this.tema = tema;
    }

    /**
     * Inicializa la escena del menú.
     *
     * @param root Nodo raíz de la escena.
     */
    public void initStage(Parent root) {
        LOGGER.info("Inicializando la ventana del menú.");
        Scene scene = new Scene(root);
        stage = Sesion.getStage();
        stage.setScene(scene);
        if (Sesion.getUser().getIsAdmin()) {
            menuPerfil.getItems().remove(miPerfil);
        } else {
            menuPerfil.getItems().remove(crearAdmins);
        }
        stage.show();
        LOGGER.info("Ventana del menú inicializada.");
    }
}
