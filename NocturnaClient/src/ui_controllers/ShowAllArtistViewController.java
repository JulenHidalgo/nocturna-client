/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import exceptions.SignInException;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.AdminManagerFactory;
import logic.UserManagerFactory;
import model.Admin;
import model.Event;
import model.User;
import utils.CustomAlert;

/**
 *
 * @author 2dam
 */
public class ShowAllArtistViewController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    HBox hbMenu;

    @FXML
    TextField tfFiltroNombre;

    @FXML
    TextField tfFiltroMusica;

    @FXML
    TableView tvArtists;

    @FXML
    TableColumn tcNombre;

    @FXML
    TableColumn tcTipoMusica;

    @FXML
    TableColumn tcDescripcion;

    @FXML
    Button btnSeleccionar;

    @FXML
    Button btnCrear;

    @FXML
    Button btnEliminar;

    private Stage stage;

    private User user;

    private Event event;

    private boolean tema;

    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing 'ShowAllArtists' window.");
        Scene scene = new Scene(root);

        if (event == null) {
            stage.setTitle("Visualizar artistas");
            vbDepartamento.setVisible(false);
            vbPass2.setVisible(false);
            hbMenu.setVisible(false);
            btnRegistrarAdmin.setVisible(false);
        } else {
            stage.setTitle("Selector de artistas");
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
        LOGGER.info("'ShowAllArtists' window initialized.");

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
