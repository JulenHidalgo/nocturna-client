/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.ClubManager;
import logic.ClubManagerFactory;
import model.Club;
import model.User;
import utils.CustomAlert;

/**
 *
 * @author 2dam
 */
public class ShowClubViewController {

    private Stage stage;

    private User user;

    private boolean tema;
    
    private Club club;

    private ClubManager clubManager;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextField txtCiudad;
    
    @FXML
    private TextField txtUbicacion;
    
    @FXML
    private ImageView imgRedes;
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");

    public void setClub(Club club) {
        this.club = club;
    }
    
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
        LOGGER.info("Initializing Show all clubs window.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        txtNombre.setText(club.getNombre());
        txtCiudad.setText(club.getCiudad());
        txtUbicacion.setText(club.getUbicacion());
        imgRedes.setOnMouseReleased(this::clickRedes);
                
        stage.show();
    }
    
    private void clickRedes(MouseEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(club.getInstagram()));
        } catch (Exception e) {
            
            new Alert(Alert.AlertType.ERROR, "No sse ha podido abrir la URL.").showAndWait();
        }
    }

}
