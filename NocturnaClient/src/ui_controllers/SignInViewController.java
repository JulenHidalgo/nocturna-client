/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import control.Sesion;
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
 *
 * @author 2dam
 */
public class SignInViewController {

    @FXML
    AnchorPane anchorpane;

    @FXML
    HBox hbMenu;

    @FXML
    TextField tfMail;

    @FXML
    VBox vbDepartamento;

    @FXML
    TextField tfDepartamento;

    @FXML
    PasswordField pfPass;

    @FXML
    VBox vbPass2;

    @FXML
    PasswordField pfPass2;

    @FXML
    HBox hbRecuperar;

    @FXML
    Hyperlink hlRecuperarPass;

    @FXML
    HBox hbSignUp;

    @FXML
    Hyperlink hlSignUp;

    @FXML
    Button btnSignIn;

    @FXML
    Button btnRegistrarAdmin;

    private Stage stage;

    private User user;

    private boolean tema;

    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

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

    public void resetPass(ActionEvent event) {
        try {
            String mail = CustomAlert.throwAlertTextField("Reseteo de contraseña", "Introduce tu correo electrónico para que te enviemos tu nueva contraseña", "Correo electrónico");

            UserManagerFactory.get().resetPassword_XML(User.class, mail);

            CustomAlert.throwAlertCustom(Alert.AlertType.INFORMATION, "Te hemos enviado un correo electronico con tu nueva contraseña.");
        } catch (Exception e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "No hy ninguna cuenta asociada a ese correo electronico");
        }

    }

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

    public void registrarAdmin(ActionEvent event) {
        user = new Admin();
        user.setMail(tfMail.getText());
        user.setPasswd(pfPass.getText());
        ((Admin) user).setDepartamento(tfDepartamento.getText());

        AdminManagerFactory.get().create_XML(user);

        CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "Admin creado correctamente");

        btnRegistrarAdmin.setDisable(true);

    }

    public void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    public void cambiarTema() {
        if (tema) {

        } else {

        }
    }
}
