/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.ButtonGroup;
import model.Event;
import model.User;

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
    ListView listViewListaDni;
    
    private Stage stage;

    private User user;

    private boolean tema;
    
    private Event event = new Event();
    
    private int dnisIntroducidos = 0;
    
    private int cantCompra;
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    @FXML
    private void comprarEntradas(ActionEvent event){
        
        
    }
    
    @FXML
    private void añadirDni(ActionEvent event){
        if(dnisIntroducidos==cantCompra){
            btnAgregarDni.setDisable(true);
        }
        
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
        lblName.setText(event.getNombre());
        lblTotal.setText(String.valueOf(event.getPrecioEntrada()*cantCompra));
        String cant = String.valueOf(dnisIntroducidos) + " / " + String.valueOf(cantCompra);
        btnAgregarDni.setText(cant);
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
    }
}
