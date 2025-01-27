/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Artist;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class ShowEventViewController {
    
    @FXML
    Label lblNombreEvento;

    @FXML
    Label lblTipoMusica;

    @FXML
    Label lblNombreSala;

    @FXML
    Label lblPrecio;

    @FXML
    Label lblCantConsumiciones;

    @FXML
    Label lblNumEntradas;
    
    @FXML
    Button btnRestar;

    @FXML
    Button btnSumar;

    @FXML
    Button btnComprar;
    
    @FXML
    ListView artistsList;
    
    private Stage stage;

    private User user;

    private boolean tema;
    
    private Event event = new Event();
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    
    
    @FXML
    private void restarTicket(ActionEvent event){
        if(Integer.parseInt(lblNumEntradas.getText())>1){
            lblNumEntradas.setText(String.valueOf(Integer.parseInt(lblNumEntradas.getText())-1));
        }else{
            new Alert(Alert.AlertType.INFORMATION, "No puedes comprar menos de una entrada ", ButtonType.OK).showAndWait();
        }
    }
    
    @FXML
    private void sumarTicket(ActionEvent event){
        if(Integer.parseInt(lblNumEntradas.getText())<this.event.getNumEntradas()){
            lblNumEntradas.setText(String.valueOf(Integer.parseInt(lblNumEntradas.getText())+1));
        }else{
            new Alert(Alert.AlertType.INFORMATION, "No quedan mas de "+this.event.getNumEntradas()+" entradas", ButtonType.OK).showAndWait();
        }
        
    }
    
    @FXML
    private void irBuyTicketsView(ActionEvent aevent){
           try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/buyTicketsView.fxml"));
            
            Parent root = loader.load();
            
            buyTicketsViewController controller = (buyTicketsViewController) loader.getController();
           
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setEvent(event);
            controller.setCantCompra(Integer.parseInt(lblNumEntradas.getText()));
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void initStage(Parent root) {
        
        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);
        
        lblNombreEvento.setText(event.getNombre());
        lblPrecio.setText(String.valueOf(event.getPrecioEntrada()));
        lblCantConsumiciones.setText(String.valueOf(event.getConsumicion()));
        lblNombreSala.setText(event.getClub().getNombre());
        List<String>  nombres = event.getArtists().stream().map(Artist::getNombre).collect(Collectors.toList());
        ObservableList<String> observableArtist = FXCollections.observableArrayList(nombres); 
        artistsList.setItems(observableArtist);
        String musicas = event.getArtists().stream().map(Artist::getTipoMusica).collect(Collectors.joining(", "));
        lblTipoMusica.setText(musicas);
        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
        
    }
    
}
