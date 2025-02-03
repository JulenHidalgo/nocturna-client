/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import control.Sesion;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    AnchorPane panel;
    
    @FXML
    TextField tfnuevoDni;
     
    @FXML
    Label lblTotal;
    
    @FXML
    Label lblName;
    
    @FXML
    Label txName;
    
    @FXML
    Label txDni;
    
    @FXML
    Label txTotal;
    
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
    private void añadirDni(ActionEvent event){
      
        if(tfnuevoDni!=null){
            tfnuevoDni.setText(tfnuevoDni.getPromptText());  
        }
        
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
        
        ObservableList<String> observableDni = FXCollections.observableArrayList(listViewListaDni.getItems());
        
        ticket.setCantidad(cantCompra); 
        ticket.setDniComprador(((Client) user).getDni());
        ticket.setFechaCompra(valueOf(LocalDate.now()));   
        Set<String> listdni = new HashSet<>(listViewListaDni.getItems());
        String dns = listdni.stream().map(Object::toString).collect(Collectors.joining(", "));
        ticket.setDniAsistentes(dns);
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
        
        //modificar el numero de enradas que quedan en el evcento
        this.event.setNumEntradas(this.event.getNumEntradas()-cantCompra); 
        EventManagerFactory.get().edit_XML(this.event, this.event.getId().toString());
        new Alert(Alert.AlertType.CONFIRMATION, "Compra realizada con exito", ButtonType.OK).showAndWait();
            
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
        String currentStyle = panel.getStyle();

        if (tema) {
            lblName.setStyle("-fx-text-fill: black;");
            lblTotal.setStyle("-fx-text-fill: black;");
            txDni.setStyle("-fx-fill: black;");
            txName.setStyle("-fx-fill: black;");
            txTotal.setStyle("-fx-fill: black;");
            rdBtnBizum.setStyle("-fx-fill: black;");
            rdBtnTarjeta.setStyle("-fx-fill: black;");
            panel.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lblName.setStyle("-fx-text-fill: white;");
            lblTotal.setStyle("-fx-text-fill: white;");
            txDni.setStyle("-fx-fill: white;");
            txName.setStyle("-fx-fill: white;");
            txTotal.setStyle("-fx-fill: white;");
             rdBtnBizum.setStyle("-fx-fill: white;");
            rdBtnTarjeta.setStyle("-fx-fill: white;");
            panel.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }
    
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(panel, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
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
        user = Sesion.getUser();
        tema = Sesion.getTema();
        stage = Sesion.getStage();
        panel.requestFocus();
        
        changeTheme();
        
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Cambiar tema");
        item1.setOnAction(this::cambiarTema);  
        contextMenu.getItems().addAll(item1);
        panel.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
        
        
        tfnuevoDni.setPromptText(((Client) user).getDni());
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
