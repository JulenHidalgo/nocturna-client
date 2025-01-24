/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.Artist;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class MenuController {

    @FXML
    MenuBar menuBar;

    @FXML
    Menu menuVer;

    @FXML
    MenuItem miTickets;

    @FXML
    MenuItem miEvents;

    @FXML
    MenuItem miClubs;

    @FXML
    MenuItem miArtists;

    @FXML
    Menu menuPerfil;

    @FXML
    MenuItem miPerfil;

    @FXML
    MenuItem miSignOut;

    @FXML
    Menu menuHelp;

    @FXML
    MenuItem miAbout;

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
        LOGGER.info("Initializing the 'Menu'.");
        Scene scene = new Scene(root);

        /**
        if (event == null) {
            if (user.getIsAdmin()) {
                tvArtists.setEditable(true);
            }
            stage.setTitle("Visualizar artistas");

            tcEventos.setText("¿Tiene eventos?");

        } else {
            stage.setTitle("Selector de artistas");
            tcEventos.setText("¿Seleccionado?");
        }

        cargarTabla();

        recogerArtistas();
        cambiarTema();

        stage.setOnCloseRequest(this::closeAppFromX);

        tfFiltroNombre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cargarTabla();
            }
        });

        tfFiltroMusica.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cargarTabla();
            }
        });
        */
        stage.show();
        stage.setScene(scene);
        LOGGER.info("'ShowAllArtists' window initialized.");

    }

}
