/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import exceptions.SignInException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.AdminManagerFactory;
import logic.ClientManagerFactory;
import logic.UserManagerFactory;
import model.Admin;
import model.Client;
import model.User;
import security.AsimetricEncrypt;
import utils.CustomAlert;

/**
 * Controlador para la vista de inicio de sesión el registro de usuarios y la
 * recuperación de contraseñas.
 *
 * @author Julen Hidalgo
 */
public class SignInViewController {

    /**
     * Panel principal de la vista.
     */
    @FXML
    private AnchorPane anchorpane;

    /**
     * Contenedor horizontal para el menú.
     */
    @FXML
    private HBox hbMenu;

    /**
     * Campo de texto para el correo electrónico.
     */
    @FXML
    private TextField tfMail;

    /**
     * Contenedor vertical para el campo de departamento.
     */
    @FXML
    private VBox vbDepartamento;

    /**
     * Campo de texto para el departamento (solo para administradores).
     */
    @FXML
    private TextField tfDepartamento;

    /**
     * Campo de contraseña.
     */
    @FXML
    private PasswordField pfPass;

    /**
     * Contenedor vertical para el campo de confirmación de contraseña.
     */
    @FXML
    private VBox vbPass2;

    /**
     * Campo de confirmación de contraseña.
     */
    @FXML
    private PasswordField pfPass2;

    /**
     * Contenedor horizontal para el enlace de recuperación de contraseña.
     */
    @FXML
    private HBox hbRecuperar;

    /**
     * Enlace para recuperar la contraseña.
     */
    @FXML
    private Hyperlink hlRecuperarPass;

    /**
     * Contenedor horizontal para el enlace de registro.
     */
    @FXML
    private HBox hbSignUp;

    /**
     * Enlace para registrarse.
     */
    @FXML
    private Hyperlink hlSignUp;

    /**
     * Botón para iniciar sesión.
     */
    @FXML
    private Button btnSignIn;

    /**
     * Botón para registrar un administrador.
     */
    @FXML
    private Button btnRegistrarAdmin;

    /**
     * Escenario principal de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario actual.
     */
    private User user;

    /**
     * Indica el tema actual de la interfaz (claro/oscuro).
     */
    private boolean tema;

    /**
     * Logger para registrar eventos.
     */
    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");

    /**
     * Establece el escenario principal de la aplicación.
     *
     * @param stage El escenario principal.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece el usuario actual.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Establece el tema de la interfaz.
     *
     * @param tema
     */
    public void setTema(boolean tema) {
        this.tema = tema;
    }

    /**
     * Inicializa la ventana y configura los componentes de la interfaz.
     *
     * @param root
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing 'SignIn' window.");
        stage = Sesion.getStage();
        Scene scene = new Scene(root);

        if (user == null) {
            stage.setTitle("Iniciar sesión");
            vbDepartamento.setVisible(false);
            vbPass2.setVisible(false);
            hbMenu.setVisible(false);
            btnRegistrarAdmin.setVisible(false);
        } else {
            stage.setTitle("Creación de admins");
            hlSignUp.setVisible(false);
            hlRecuperarPass.setVisible(false);
            btnSignIn.setVisible(false);
        }

        btnSignIn.setOnAction(this::signIn);
        btnRegistrarAdmin.setOnAction(this::registrarAdmin);
        hlRecuperarPass.setOnAction(this::resetPass);
        hlSignUp.setOnAction(this::signUp);
        stage.setOnCloseRequest(this::closeAppFromX);

        cambiarTema();
        stage.show();
        stage.setScene(scene);
        LOGGER.info("'SignIn' window initialized.");
    }

    /**
     * Maneja la solicitud de recuperación de contraseña.
     *
     * @param event
     */
    public void resetPass(ActionEvent event) {
        try {
            String mail = CustomAlert.throwAlertTextField("Reseteo de contraseña", "Introduce tu correo electrónico para que te enviemos tu nueva contraseña", "Correo electrónico");

            UserManagerFactory.get().resetPassword_XML(User.class, mail);

            CustomAlert.throwAlertCustom(Alert.AlertType.INFORMATION, "Te hemos enviado un correo electronico con tu nueva contraseña.");
        } catch (Exception e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "No hay ninguna cuenta asociada a ese correo electronico");
        }
    }

    /**
     * Maneja la navegación a la vista de registro de usuarios.
     *
     * @param event
     */
    public void signUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signUpView.fxml"));

            Parent root = loader.load();

            SignUpViewController controller = (SignUpViewController) loader.getController();

            controller.setStage(stage);
            controller.setTema(tema);

            controller.initStage(root);
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error al cargar la ventana, intentalo más tarde");
        }
    }

    /**
     * Maneja el proceso de inicio de sesión.
     *
     * @param event
     */
    public void signIn(ActionEvent event) {
        try {
            if (tfMail.getText().isEmpty() || pfPass.getText().isEmpty()) {
                throw new Exception();
            }
            user = new User();
            user.setMail(URLEncoder.encode(AsimetricEncrypt.encrypt(tfMail.getText()), "UTF-8"));
            user.setPasswd(URLEncoder.encode(AsimetricEncrypt.encrypt(pfPass.getText()), "UTF-8"));

            LOGGER.log(Level.INFO, user.getMail());
            user = UserManagerFactory.get().login_XML(User.class, user.getMail(), user.getPasswd());
            Long id = user.getId();
            if (user.getIsAdmin()) {
                user = new Admin();
                user = AdminManagerFactory.get().find_XML(Admin.class, id.toString());
                user.setIsAdmin(true);
            } else {
                user = new Client();
                user = ClientManagerFactory.get().find_XML(Client.class, id.toString());
                user.setIsAdmin(false);
            }

            Sesion.setUser(user);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));

            Parent root = loader.load();

            ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();

            controller.initStage(root);
        } catch (SignInException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, e.getMessage());
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error al cargar la ventana, intentalo más tarde");
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    /**
     * Maneja el registro de un nuevo administrador.
     *
     * @param event
     */
    public void registrarAdmin(ActionEvent event) {
        user = new Admin();
        user.setMail(tfMail.getText());
        user.setPasswd(pfPass.getText());
        ((Admin) user).setDepartamento(tfDepartamento.getText());

        AdminManagerFactory.get().create_XML(user);

        CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "Admin creado correctamente");

        btnRegistrarAdmin.setDisable(true);
    }

    /**
     * Maneja el cierre de la aplicación desde la ventana de inicio de sesión.
     *
     * @param event
     */
    public void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    /**
     * Cambia el tema de la interfaz gráfica.
     */
    public void cambiarTema() {
        if (tema) {
            // Lógica para cambiar a tema oscuro.
        } else {
            // Lógica para cambiar a tema claro.
        }
    }
}
