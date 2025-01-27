/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import exceptions.InternalServerErrorException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    private final Logger LOGGER = Logger.getLogger("Show all clubs view");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    public void initStage(Parent root) throws Exception {
        try {
            LOGGER.info("Initializing Show all clubs window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(this::closeAppFromX);
            //Si el usuario es null, significa que no ha entrado a la app todavía y 
            //está intentando registrarse, si no, significa que va a modificar su información.

            clubManager = ClubManagerFactory.get();

            clubs = getClubsInfo();
            LOGGER.info("Setting table properties and data.");
            columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            columnCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
            columnInstagram.setCellValueFactory(new PropertyValueFactory<>("instagram"));

            columnNombre.setCellFactory(column -> new ClubEditingCell());
            columnUbicacion.setCellFactory(column -> new ClubEditingCell());
            columnCiudad.setCellFactory(column -> new ClubEditingCell());
            columnInstagram.setCellFactory(column -> new ClubEditingCell());

            setTableData();
            if (user.getIsAdmin() == true) {
                btnCreate.setOnAction(this::createClub);
                btnDelete.setOnAction(this::deleteClub);
                btnSelect.setDisable(true);
                btnDelete.setDisable(true);
                tableClubs.setEditable(user.getIsAdmin());
                tableClubs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else {
                btnCreate.setVisible(user.getIsAdmin());
                btnDelete.setVisible(user.getIsAdmin());
                btnSelect.setDisable(true);
            }

            stage.show();
            LOGGER.info("Show all clubs window initialized.");

            btnSelect.setOnAction(this::masInfo);

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

            tableClubs.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Club>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Club> c) {
                    if (c.getList().isEmpty()) {
                        btnSelect.setDisable(true);
                        btnDelete.setDisable(true);
                    }else if (c.getList().size() == 1) {
                        btnSelect.setDisable(false);
                        btnDelete.setDisable(false);
                    }else if (c.getList().size() > 1) {
                        btnSelect.setDisable(true);
                        btnDelete.setDisable(false);
                    }
                }
            });

            dateFirst.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if (newValue != null) {
                        if(dateFirst.getValue() != null){
                            dateSecond.setVisible(true);
                        }
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception setting up de window.", e.getMessage());
            throw new Exception("ERROR INICIANDO LA VENTANA");
        }
    }
    
     private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION,"¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }
    
    private void deleteClub(ActionEvent event) {
        try {
            ObservableList<Club> obserbableClubs = 
                    tableClubs.getSelectionModel().getSelectedItems();
            for (Club club : obserbableClubs) {
                clubManager.remove(club.getId().toString());
            }
            clubs = getClubsInfo();
            setTableData();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception deleting club.", e.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR EN EL PROCESO DE ELIMINACIÓN");
        }
    }
    
    private void masInfo(ActionEvent event) {
        try {
            Club club = tableClubs.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showClubView.fxml"));
            
            Parent root = loader.load();
            
            ShowClubViewController controller = (ShowClubViewController) loader.getController();
            
            controller.setStage(stage);
            controller.setUser(user);
            controller.setClub(club);
            controller.initStage(root);
        } catch (Exception ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR EN EL PROCESO DE ELIMINACIÓN");
        }
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
            
            if (tableClubs.getSelectionModel() != null && !tableClubs.getItems().isEmpty()) {
                int lastIndex = tableClubs.getItems().size() - 1;
                tableClubs.getSelectionModel().clearAndSelect(lastIndex);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception creating club", ex.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR CREANDO USUARIO");
        }
    }
    
    private void setTableData() {
        try {
            ObservableList<Club> observableClubs = FXCollections.observableArrayList(clubs);
            tableClubs.setItems(observableClubs);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting table data", ex.getMessage());
        }
    }

    private List<Club> getClubsInfo() throws Exception {
        try {
            Club[] clubsArray = clubManager.findAll_XML(Club[].class);
            List<Club> clubes = new ArrayList<>();
            for (int i = 0; i < clubsArray.length; i++) {
                clubes.add(clubsArray[i]);
            }
            return clubes;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception getting clubs info", ex.getMessage());
            throw new Exception("ERROR RECOGIENDO CLUBES");
        }
    }

}
