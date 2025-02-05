/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import control.Sesion;
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
 * Controlador de la vista para mostrar detalles de un evento.
 * si el usuario es cliente permite seleccionar cuantas entradas quiere comprar
 * 
 * @author Erlantz
 */
public class ShowEventViewController {
  
    /**Panel principal de la interfaz.*/
    @FXML
    AnchorPane bpPrincipal;

    /** Etiqueta para mostrar el nombre del evento */
    @FXML
    Label lblNombreEvento;

    /**
     * Etiqueta para mostrar el tipo de música 
     * de los artistas que actuan en el evento.
     */
    @FXML
    Label lblTipoMusica;

    /**Etiqueta para mostrar el nombre de la sala del evento.*/
    @FXML
    Label lblNombreSala;

    /**Etiqueta para mostrar el precio de la entrada al evento.*/
    @FXML
    Label lblPrecio;

    /**Etiqueta para mostrar la cantidad de consumiciones incluidas en el evento.*/
    @FXML
    Label lblCantConsumiciones;

    /**Etiqueta para mostrar el número de entradas seleccionadas.*/
    @FXML
    Label lblNumEntradas;

    /**Etiqueta para mostrar el nombre de los artistas*/
    @FXML
    Label lbArtist;

    /**Texto para mostrar detalles del artista. */
    @FXML
    Text txArtist;

    /**Texto para mostrar detalles de la sala.*/
    @FXML
    Text txSalas;

    /**Texto para mostrar detalles del precio.*/
    @FXML
    Text txPrecio;

    /**Texto para mostrar detalles de las consumiciones. */
    @FXML
    Text txConsumicion;

    /**Texto para mostrar la cantidad de entradas disponibles.*/
    @FXML
    Text txCantEntradas;

    /** Botón para restar entradas seleccionadas*/
    @FXML
    Button btnRestar;

    /**Botón para sumar entradas seleccionadas */
    @FXML
    Button btnSumar;

    /**Botón para comprar entradas.*/
    @FXML
    Button btnComprar;

    /**Escenario principal de la aplicación*/
    private Stage stage;

    /**Usuario actual de la sesión.*/
    private User user;

    /**Indicador del tema actual (claro u oscuro).*/
    private boolean tema;

    /**Evento actual mostrado en la interfaz*/
    private Event event = new Event();

    /**Logger para registrar eventos e información de depuración*/
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");


    /**
     * Resta el numero de entradas que quiere comprar
     * siendo 1 el minimo permitido 
     * muestra alert si intenta bajar del 1
     * @param event 
     */
    @FXML
    private void restarTicket(ActionEvent event){
        if(Integer.parseInt(lblNumEntradas.getText())>1){
            lblNumEntradas.setText(String.valueOf(Integer.parseInt(lblNumEntradas.getText())-1));
        }else{
            new Alert(Alert.AlertType.INFORMATION, "No puedes comprar menos de una entrada ", ButtonType.OK).showAndWait();
        }
    }
    
    /**
     * Suma el numero de entradas que quiere comprar
     * Muestra una alerta si el número de entradas qu equiere intenta superar 
     * el numero de entradas que quedan en el evento(NumEntradas).
     * @param event 
     */
    @FXML
    private void sumarTicket(ActionEvent event){
        if(Integer.parseInt(lblNumEntradas.getText())<this.event.getNumEntradas()){
            lblNumEntradas.setText(String.valueOf(Integer.parseInt(lblNumEntradas.getText())+1));
        }else{
            new Alert(Alert.AlertType.INFORMATION, "No quedan mas de "+this.event.getNumEntradas()+" entradas", ButtonType.OK).showAndWait();
        }
        
    }
    
    /**
     * Navega a la vista de compra de entradas.
     * Inicializa el controlador de la vista de compra de entradas con el evento actual 
     * y la cantidad de entradas que quiere comprar.
     * @param aevent 
     */
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
    
    /**
     * Cambia el tema de la aplicación.
     * Alterna entre el tema claro y oscuro.
     * @param event 
     */
    private void cambiarTema(ActionEvent event) {
        if (tema) {
            Sesion.setTema(false);
        } else {
            Sesion.setTema(true);
        }
        tema = Sesion.getTema();
        changeTheme();
    }

    /**
     * Aplica el tema actual a la interfaz gráfica.
     * Cambia los estilos de los componentes de la interfaz según el tema seleccionado.
     */
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
    
    /**
     * Controla el menú contextual en la interfaz.
     * Muestra el menú contextual cuando se hace clic derecho en la interfaz.
     * @param event 
     * @param menu 
     */
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        if (event.getButton() == MouseButton.SECONDARY) {
            menu.show(bpPrincipal, event.getScreenX(), event.getScreenY());
        } else {
            menu.hide();
        }
    }
     
    /**
     * Establece el evento actual.
     * @param event (evento sseleccionado en la ventana anterior).
     */
    public void setEvent(Event event){
        this.event = event;
    }
    
    /**
     * Inicializa la escena de la ventana.
     * Configura la ventana, el tema y los componentes de la interfaz.
     * @param root 
     */
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

