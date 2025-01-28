/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javax.swing.ButtonGroup;
import logic.EventManagerFactory;
import logic.TicketManagerFactory;
import logic.UserManagerFactory;
import model.Event;
import model.Ticket;
import model.User;
import model.Client;
import model.FormaPago;

/**
 *
 * @author 2dam
 */
public class buyTicketsViewController {
    
    @FXML
    TextField tfnuevoDni;
     
    @FXML
    Label lblTotal;
    
    @FXML
    Label lblName;
    
    @FXML
    ButtonGroup btnGourp;
    
    @FXML
    RadioButton rdBtnBizum;
    
    @FXML
    RadioButton rdBtnTarjeta;
    
    @FXML
    Button btnAgregarDni;
    
    @FXML
    Button btnComprar;
    
    @FXML
    ListView listViewListaDni;
    
    ToggleGroup toggleGroup = new ToggleGroup();
    
    
    private Stage stage;

    private User user;

    private boolean tema;
    
    private Event event = new Event();
    
    private int dnisIntroducidos = 0;
    
    private int cantCompra;
    
    private Ticket ticket = new Ticket();
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    @FXML
    private void comprarEntradas(ActionEvent event){
        
        
    }
    
    @FXML
    private void añadirDni(ActionEvent event){
        if (!tfnuevoDni.getText().matches("^\\d{8}[A-Za-z]$")) {
            LOGGER.warning("DNI validation error, pattern incorrect");
            new Alert(Alert.AlertType.ERROR, "Ese DNI no es correcto", ButtonType.OK).showAndWait();
        }else{
            dnisIntroducidos++;
            ObservableList<String> observableDni = FXCollections.observableArrayList(tfnuevoDni.getText());
            listViewListaDni.setItems(observableDni);
            String cant = String.valueOf(dnisIntroducidos) + " / " + String.valueOf(cantCompra);
            btnAgregarDni.setText(cant);
            tfnuevoDni.setText(null);
            if (dnisIntroducidos == cantCompra) {
                btnAgregarDni.setDisable(true);
                if(toggleGroup.getSelectedToggle()!=null)
                    btnComprar.setDisable(false);
            }
        }    
    }
    
    @FXML
    private void comprarTickets(ActionEvent event){
        this.event.setNumEntradas(this.event.getNumEntradas()-cantCompra); 
        EventManagerFactory.get().edit_XML(this.event, this.event.getId().toString());
        ObservableList<String> observableDni = FXCollections.observableArrayList(listViewListaDni.getItems());
        
        user = new Client();
        user = UserManagerFactory.get().find_XML(Client.class, "2");
        ticket.setCantidad(cantCompra); 
        ticket.setDniComprador(((Client) user).getDni());
        ticket.setFechaCompra(valueOf(LocalDate.now()));   
        Set<String> listdni = new HashSet<>(listViewListaDni.getItems());
        ticket.setDniAsistentes(listdni);
        ticket.setImporteCompra(this.event.getPrecioEntrada() * cantCompra);
        ticket.setEvent(this.event);
        ticket.setUser(user);
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        String selectedText = selectedRadioButton.getText();
        if (selectedText.equalsIgnoreCase("Bizum")) {
            ticket.setFormapago(FormaPago.BIZUM);
        } else {
            ticket.setFormapago(FormaPago.TARJETA);
        }
        TicketManagerFactory.get().create_XML(ticket);
            new Alert(Alert.AlertType.CONFIRMATION, "Compra realizada con exito", ButtonType.OK).showAndWait();
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
    
    public void setEvent(Event event){
        this.event = event;
    }
    
    public void setCantCompra(Integer cantCompra){
        this.cantCompra = cantCompra;
    }
    
    public void initStage(Parent root) {
        
        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);
        
        rdBtnBizum.setToggleGroup(toggleGroup);
        rdBtnTarjeta.setToggleGroup(toggleGroup);
        lblName.setText(event.getNombre());
        lblTotal.setText(String.valueOf(event.getPrecioEntrada()*cantCompra));
        String cant = String.valueOf(dnisIntroducidos) + " / " + String.valueOf(cantCompra);
        btnAgregarDni.setText(cant);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { 
                if (dnisIntroducidos == cantCompra) {
                    btnComprar.setDisable(false);
                }
            } 
        });
        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
    }
}
