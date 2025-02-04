/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Admin;
import model.Artist;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class ShowEventViewController {
    
    @FXML
    AnchorPane bpPrincipal;
    
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
    Label lbArtist;
    
    @FXML
    Text txArtist;
    
    @FXML
    Text txSalas;
    
    @FXML
    Text txPrecio;
    
    @FXML
    Text txConsumicion;
    
    @FXML
    Text txCantEntradas;
    
    @FXML
    Button btnRestar;

    @FXML
    Button btnSumar;

    @FXML
    Button btnComprar;
    
   
    
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
           
            controller.setEvent(event);
            controller.setCantCompra(Integer.parseInt(lblNumEntradas.getText()));
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String currentStyle = bpPrincipal.getStyle();

        if (tema) {
            
            lbArtist.setStyle("-fx-text-fill: black;");
            lblNombreSala.setStyle("-fx-text-fill: black;");
            lblNombreEvento.setStyle("-fx-text-fill: black;");
            lblCantConsumiciones.setStyle("-fx-text-fill: black;");
            lblPrecio.setStyle("-fx-text-fill: black;");
            lblTipoMusica.setStyle("-fx-text-fill: black;");
            txArtist.setStyle("-fx-fill: black;");
            txCantEntradas.setStyle("-fx-fill: black;");
            txConsumicion.setStyle("-fx-fill: black;");
            txPrecio.setStyle("-fx-fill: black;");
            txSalas.setStyle("-fx-fill : black;");
            lblNumEntradas.setStyle("-fx-text-fill: black;");
            bpPrincipal.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            
            lbArtist.setStyle("-fx-text-fill: white;");
            lblNombreSala.setStyle("-fx-text-fill: white;");
            lblNombreEvento.setStyle("-fx-text-fill: white;");
            lblCantConsumiciones.setStyle("-fx-text-fill: white;");
            lblPrecio.setStyle("-fx-text-fill: white;");
            lblTipoMusica.setStyle("-fx-text-fill: white;");
            txArtist.setStyle("-fx-fill: white;");
            txCantEntradas.setStyle("-fx-fill: white;");
            txConsumicion.setStyle("-fx-fill: white;");
            txPrecio.setStyle("-fx-fill: white;");
            txSalas.setStyle("-fx-fill: white;");
            lblNumEntradas.setStyle("-fx-text-fill: white;");
            bpPrincipal.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }
    
     private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(bpPrincipal, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
    }
     
     
    public void setEvent(Event event){
        this.event = event;
    }
    
    public void initStage(Parent root) {
        
        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);
        user = Sesion.getUser();
        tema = Sesion.getTema();
        stage = Sesion.getStage();
        
        changeTheme();
        
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Cambiar tema");
        item1.setOnAction(this::cambiarTema);  
        contextMenu.getItems().addAll(item1);
        bpPrincipal.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
        
        if(Sesion.getUser().getIsAdmin()){
            btnComprar.setVisible(false);
            btnSumar.setVisible(false);
            btnRestar.setVisible(false);
        }
        
        lblNombreEvento.setText(event.getNombre());
        lblPrecio.setText(String.valueOf(event.getPrecioEntrada()));
        lblCantConsumiciones.setText(String.valueOf(event.getConsumicion()));
        lblNombreSala.setText(event.getClub().getNombre());
        
        if (event.getArtists() != null) {
            String nombres = event.getArtists().stream().map(Artist::getNombre).collect(Collectors.joining(", "));
            lbArtist.setText(nombres);
            String musicas = event.getArtists().stream().map(Artist::getTipoMusica).collect(Collectors.joining(", "));
            lblTipoMusica.setText(musicas);
        }

        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
        
    }
    
}
