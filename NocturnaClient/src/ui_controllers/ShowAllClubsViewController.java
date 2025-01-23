/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
public class ShowAllClubsViewController {

    private Stage stage;

    private User user;

    private boolean tema;
    
    private List<Club> clubs;
    
    private ClubManager clubManager;
    
    @FXML
    private TextField txtNameFilter;
    
    @FXML
    private DatePicker dateFirst;
    
    @FXML
    private DatePicker dateSecond;
    
    @FXML
    private TableView<Club> tableClubs;
    
    @FXML
    private TableColumn<Club, String> columnNombre;
    
    @FXML
    private TableColumn<Club, String> columnUbicacion;
    
    @FXML
    private TableColumn<Club, String> columnCiudad;
    
    @FXML
    private TableColumn<Club, String> columnInstagram;
    
    @FXML
    private Button btnDelete;
    
    @FXML
    private Button btnCreate;
    
    @FXML
    private Button btnSelect;

    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");

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
        //Si el usuario es null, significa que no ha entrado a la app todavía y 
        //está intentando registrarse, si no, significa que va a modificar su información.
       
        clubManager = ClubManagerFactory.get();
        
        clubs = getClubsInfo();
        
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        columnCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        columnInstagram.setCellValueFactory(new PropertyValueFactory<>("instagram"));
        
        setTableData();
        if (user.getIsAdmin() == true) {
            btnCreate.setOnAction(this::createClub);
            btnDelete.setOnAction(this::deleteClub);
            btnSelect.setDisable(true);
            //btnDelete.setDisable(true);
            tableClubs.setEditable(user.getIsAdmin());
        } else {
            btnCreate.setVisible(user.getIsAdmin());
            btnDelete.setVisible(user.getIsAdmin());
            btnSelect.setDisable(true);
        }
        
        stage.show();
        LOGGER.info("Show all clubs window initialized.");
                
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Club> clubFilter = new ArrayList<>();
            if (!newValue.isEmpty()) {
                for (Club club : clubs) {
                    if (club.getNombre().contains(newValue)){
                        clubFilter.add(club);
                    }
                    ObservableList<Club> observableClubs = FXCollections.observableArrayList(clubFilter);
                    tableClubs.setItems(observableClubs);
            }
            } else {
                setTableData();
            }
        });
        tableClubs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //tableClubs.getSelectionModel().selectedItemProperty().addListener(this::selectionNumberChange);

    }
    
    private void selectionNumberChange(ObservableValue<? extends Club> observable, String oldValue, String newValue) {
        
    }
    
    private void deleteClub(ActionEvent event) {
        ObservableList<Club> obserbableClubs = tableClubs.getSelectionModel().getSelectedItems();
        for (Club club : obserbableClubs) {
            clubManager.remove(club.getId().toString());
        }
        clubs = getClubsInfo();
        setTableData();
    }

    private void createClub(ActionEvent event) {
        try {
            Club club = new Club();
            club.setCiudad("");
            club.setInstagram("");
            club.setNombre("");
            club.setUbicacion("");

            clubManager.create_XML(club);

            clubs.add(club);
            
            clubs = getClubsInfo();
            setTableData();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR CREANDO USUARIO");
        }
    }
    
    private void setTableData() {
        ObservableList<Club> observableClubs = FXCollections.observableArrayList(clubs);
        tableClubs.setItems(observableClubs);
    }

    private List<Club> getClubsInfo() {
        Club[] clubsArray = clubManager.findAll_XML(Club[].class);
        List<Club> clubes = new ArrayList<>();
        for (int i = 0; i < clubsArray.length; i++) {
            clubes.add(clubsArray[i]);
        }
        return clubes;
    }

}
