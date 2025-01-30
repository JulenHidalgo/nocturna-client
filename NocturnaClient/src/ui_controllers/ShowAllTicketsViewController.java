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
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void initStage(Parent root) {
        
        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);
        user = Sesion.getUser();
        tema = Sesion.getTema();
        stage = Sesion.getStage();
        
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
