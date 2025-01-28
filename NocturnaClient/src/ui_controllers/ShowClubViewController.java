/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.EventManager;
import logic.EventManagerFactory;
import model.Club;
import model.Event;
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
    
    private List<Event> events;

    private EventManager eventManager;
    
    @FXML
    AnchorPane anchorPane;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private Button btnInfo;
    
    @FXML
    private TextField txtCiudad;
    
    @FXML
    private TextField txtUbicacion;
    
    @FXML
    private ImageView imgRedes;
    
    @FXML
    private TableView<Event> tableEvents;
    
    @FXML
    private TableColumn<Event, String> columnNombre;
    
    @FXML
    private TableColumn<Event, String> columnFecha;
    
    private final Logger LOGGER = Logger.getLogger("Show club view");

    public void setClub(Club club) {
        this.club = club;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean getTema() {
        return this.tema;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    public void initStage(Parent root) throws Exception {
        try {
            LOGGER.info("Initializing Show all clubs window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            txtNombre.setText(club.getNombre());
            txtCiudad.setText(club.getCiudad());
            txtUbicacion.setText(club.getUbicacion());
            imgRedes.setOnMouseReleased(this::clickRedes);
            
            eventManager = EventManagerFactory.get();

            events = getEventsInfo();
            LOGGER.info("Setting table properties and data.");
            columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            setTableData();
            btnInfo.setDisable(true);
            changeTheme();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("Cambiar tema");
            item1.setOnAction(this::cambiarTema);
            MenuItem item2 = new MenuItem("Imprimir los datos de la tabla");
            item2.setOnAction(this::imprimirTabla);
            contextMenu.getItems().addAll(item1, item2);

            anchorPane.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
            
            stage.show();
            
            btnInfo.setOnAction(this::masInfo);
            
            tableEvents.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Event>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Event> c) {
                    if (c.getList().isEmpty()) {
                        btnInfo.setDisable(true);
                    }else if (c.getList().size() == 1) {
                        btnInfo.setDisable(false);
                    }else if (c.getList().size() > 1) {
                        btnInfo.setDisable(true);
                    }
                }
            });
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting up the window", ex.getMessage());
            throw new Exception("ERROR INICIALIZANDO LA VENTANA");
        }
    }
    
    private void imprimirTabla(ActionEvent event) {
        
    }
    
    private void cambiarTema(ActionEvent event) {
        if (getTema()) {
            setTema(false);
        } else {
            setTema(true);
        }
        changeTheme();
    }
    
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(anchorPane, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
    }
    
    private void clickRedes(MouseEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(club.getInstagram()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception logging up .", e.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR CREANDO USUARIO");
        }
    }
    
    private void setTableData() {
        try {
            ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
            if (observableEvents.size() > 0) {
                tableEvents.setItems(observableEvents);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting table data", ex.getMessage());
        }
    }
    
    private void masInfo(ActionEvent event) {
        try {
            Event eventTable = tableEvents.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));
            
            Parent root = loader.load();
            
            ShowEventViewController controller = (ShowEventViewController) loader.getController();
            
            controller.setStage(stage);
            controller.setUser(user);
            controller.setEvent(eventTable);
            controller.initStage(root);
        } catch (Exception ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void changeTheme() {
        String currentStyle = anchorPane.getStyle();
        if (tema) {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }
    
    private List<Event> getEventsInfo() throws Exception {
        try {
            Event[] eventsArray = eventManager.findAll_XML(Event[].class);
            List<Event> eventsList = new ArrayList<>();
            for (int i = 0; i < eventsArray.length; i++) {
                if (Objects.equals(club.getId(), eventsArray[i].getClub().getId())) {
                    eventsList.add(eventsArray[i]);
                }
            }
            return eventsList;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception getting clubs info", ex.getMessage());
            throw new Exception("ERROR RECOGIENDO CLUBES");
        }
    }

}
