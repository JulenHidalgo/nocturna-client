/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import model.Event;
import model.Ticket;
import model.User;
import model.Client;
import model.FormaPago;

/**
 * Controlador de la vista para comprar entradas. realiza la compra creando los
 * tickets y modificando el evento
 *
 * @author Erlantz Rey
 */
public class buyTicketsViewController {

    /**
     * Panel principal de la interfaz.
     */
    @FXML
    AnchorPane panel;

    /**
     * Campo de texto para introducir un nuevo DNI.
     */
    @FXML
    TextField tfnuevoDni;

    /**
     * Etiqueta para mostrar el total de la compra.
     */
    @FXML
    Label lblTotal;

    /**
     * Etiqueta para mostrar el nombre del evento.
     */
    @FXML
    Label lblName;

    /**
     * Etiqueta para mostrar el DNI del usuario.
     */
    @FXML
    Label txDni;

    /**
     * Etiqueta para mostrar el total de la compra.
     */
    @FXML
    Label txTotal;

    /**
     * Grupo de botones para seleccionar la forma de pago.
     */
    @FXML
    ButtonGroup btnGourp;

    /**
     * Botón de radio para seleccionar Bizum como forma de pago.
     */
    @FXML
    RadioButton rdBtnBizum;

    /**
     * Botón de radio para seleccionar tarjeta como forma de pago.
     */
    @FXML
    RadioButton rdBtnTarjeta;

    /**
     * Botón para agregar un nuevo DNI.
     */
    @FXML
    Button btnAgregarDni;

    /**
     * Botón para realizar la compra.
     */
    @FXML
    Button btnComprar;

    /**
     * Lista de vista para mostrar la lista de DNIs.
     */
    @FXML
    ListView listViewListaDni;

    /**
     * controlador del menú
     */
    @FXML
    private MenuController menuIncludeController;

    /**
     * Grupo de alternancia para los botones de radio.
     */
    ToggleGroup toggleGroup = new ToggleGroup();

    private Stage stage;
    private User user;
    private boolean tema;
    private Event event = new Event();
    private int dnisIntroducidos = 0;
    private int cantCompra;
    private Ticket ticket = new Ticket();
    private List<String> DniList = new ArrayList<>();
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");

    /**
     * Añade un nuevo DNI a la lista de DNIs. Valida el formato del DNI antes de
     * agregarlo.
     *
     * @param event
     */
    @FXML
    private void añadirDni(ActionEvent event){
        if( DniList.isEmpty()){
            tfnuevoDni.setText(tfnuevoDni.getPromptText());  
        }
        
        if (tfnuevoDni.getText().isEmpty() || !tfnuevoDni.getText().matches("^\\d{8}[A-Za-z]$")) {

            LOGGER.warning("DNI validation error, pattern incorrect");
            new Alert(Alert.AlertType.ERROR, "Ese DNI no es correcto", ButtonType.OK).showAndWait();
        } else {
            dnisIntroducidos++;
            DniList.add(tfnuevoDni.getText());
            ObservableList<String> observableDni = FXCollections.observableArrayList(DniList);
            
            listViewListaDni.setItems(observableDni);
            String cant = String.valueOf(dnisIntroducidos) + " / " + String.valueOf(cantCompra);
            btnAgregarDni.setText(cant);
            tfnuevoDni.setText("");
            tfnuevoDni.setPromptText("");
            if (dnisIntroducidos == cantCompra) {
                btnAgregarDni.setDisable(true);
                if (toggleGroup.getSelectedToggle() != null) {
                    btnComprar.setDisable(false);
                }
            }
        }
    }

    /**
     * Realiza la compra de entradas. Agrega los detalles de la compra crea los
     * tickets y actualiza el evento.
     *
     * @param event
     */
    @FXML
    private void comprarTickets(ActionEvent event) {
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

        //modificar el numero de enradas que quedan en el evento
        this.event.setNumEntradas(this.event.getNumEntradas() - cantCompra);
        EventManagerFactory.get().edit_XML(this.event, this.event.getId().toString());
        new Alert(Alert.AlertType.CONFIRMATION, "Compra realizada con exito", ButtonType.OK).showAndWait();
    }

    /**
     * Cambia el tema de la aplicación. Alterna entre el tema claro y oscuro.
     *
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
     * Aplica el tema actual a la interfaz gráfica. Cambia los estilos de los
     * componentes de la interfaz según el tema seleccionado.
     */
    private void changeTheme() {
        String currentStyle = panel.getStyle();

        if (tema) {
            lblName.setStyle("-fx-text-fill: black;");
            lblTotal.setStyle("-fx-text-fill: black;");
            txDni.setStyle("-fx-fill: black;");
            txTotal.setStyle("-fx-fill: black;");
            rdBtnBizum.setStyle("-fx-fill: black;");
            rdBtnTarjeta.setStyle("-fx-fill: black;");
            panel.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lblName.setStyle("-fx-text-fill: white;");
            lblTotal.setStyle("-fx-text-fill: white;");
            txDni.setStyle("-fx-fill: white;");
            txTotal.setStyle("-fx-fill: white;");
            rdBtnBizum.setStyle("-fx-fill: white;");
            rdBtnTarjeta.setStyle("-fx-fill: white;");
            panel.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }

    /**
     * Controla el menú contextual en la interfaz. Muestra el menú contextual
     * cuando se hace clic derecho en la interfaz.
     *
     * @param event
     * @param menu
     */
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        if (event.getButton() == MouseButton.SECONDARY) {
            menu.show(panel, event.getScreenX(), event.getScreenY());
        } else {
            menu.hide();
        }
    }

    /**
     * Establece el evento actual.
     *
     * @param event El evento de las entradas.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Establece la cantidad de entradas a comprar.
     *
     * @param cantCompra Cantidad de entradas a comprar.
     */
    public void setCantCompra(Integer cantCompra) {
        this.cantCompra = cantCompra;
    }

    /**
     * Inicia la ventana con los datos necesarios
     *
     * @param root El nodo raíz que se agrega a la escena.
     */
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

        menuIncludeController.checkAdmin(user.getIsAdmin());

        tfnuevoDni.setPromptText(((Client) user).getDni());
        
        rdBtnBizum.setToggleGroup(toggleGroup);
        rdBtnTarjeta.setToggleGroup(toggleGroup);
        lblName.setText(event.getNombre());
        lblTotal.setText(String.valueOf(event.getPrecioEntrada() * cantCompra));
        String cant = String.valueOf(dnisIntroducidos) + " / " + String.valueOf(cantCompra);
        btnAgregarDni.setText(cant);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (dnisIntroducidos == cantCompra) {
                    btnComprar.setDisable(false);
                }
            }
        });

        stage.setTitle("Comprar entradas");
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
    }
}