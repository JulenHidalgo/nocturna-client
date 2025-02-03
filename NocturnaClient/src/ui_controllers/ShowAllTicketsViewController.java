/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import control.Sesion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.TicketManagerFactory;
import model.Client;
import model.Club;
import model.Event;
import model.Ticket;
import model.User;
import org.eclipse.persistence.sessions.Session;

/**
 *
 * @author 2dam
 */
public class ShowAllTicketsViewController {
    
    
    @FXML
    AnchorPane bpPanel;
    
    @FXML
    Label lbEntradas;
    
    @FXML
    TableView<Ticket> tableEntradas;
    
    @FXML
    TableColumn<Ticket, String> columnDni;
    
    @FXML
    TableColumn<Ticket, String> columnNombre;
    
    @FXML
    TableColumn<Ticket, String> columnFecha;
    
    
    private Stage stage;

    private User user;

    private boolean tema;
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    private List<Ticket> tickets;
  
    
    private void cargarTabla(){
        initializeTableColumns();        
        // Convertir ArrayList a ObservableList
        ObservableList<Ticket> observableEvents = FXCollections.observableArrayList(tickets);       
        // Cargar datos en la tabla
        tableEntradas.setItems(observableEvents);
    }
            
    
    private void initializeTableColumns() {
        columnDni.setCellValueFactory(new PropertyValueFactory<>("dniAsistentes"));

        // Configurar la columna Nombre (nombre del evento)
        columnNombre.setCellValueFactory(cellData -> {
            String eventName = (cellData.getValue().getEvent() != null) ? cellData.getValue().getEvent().getNombre() : "Sin Evento";
            return new SimpleStringProperty(eventName);
        });
        
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
    }
    
    private void cambiarTema(ActionEvent event) {
        if (tema) {
            Sesion.setTema(false);
        } else {
            Sesion.setTema(true);
        }
        tema = Sesion.getTema();
        changeTheme();
    }

    private void changeTheme() {
        String currentStyle = bpPanel.getStyle();

        if (tema) {
            lbEntradas.setStyle("-fx-text-fill: black;");         
            bpPanel.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lbEntradas.setStyle("-fx-text-fill: white;");
            bpPanel.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }
    
    
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(bpPanel, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
    }
    
    public void initStage(Parent root) {
        
        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);
        user = Sesion.getUser();
        tema = Sesion.getTema();
        stage = Sesion.getStage();
        
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Cambiar tema");
        item1.setOnAction(this::cambiarTema);  
        contextMenu.getItems().addAll(item1);
        bpPanel.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
        if(user instanceof Client){
            Ticket[] ticketList = TicketManagerFactory.get().findTicketByUser_XML(Ticket[].class, ((Client)user).getDni());
            tickets = Arrays.asList(ticketList);  
        }else{
            Ticket[] ticketList = TicketManagerFactory.get().findAll_XML(Ticket[].class);
            tickets = Arrays.asList(ticketList);  
        }       
        cargarTabla();
        
        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
    }
    
}
