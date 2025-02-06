/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import model.Ticket;
import model.User;

/**
 *
 * @author 2dam
 */
public class ShowAllTicketsViewController {

    /**
     * Panel principal de la interfaz.
     */
    @FXML
    AnchorPane bpPanel;

    /**
     * Etiqueta que muestra el título "Entradas".
     */
    @FXML
    Label lbEntradas;

    /**
     * Tabla que muestra la lista de entradas.
     */
    @FXML
    TableView<Ticket> tableEntradas;

    /**
     * Columna DNI de la tabla.
     */
    @FXML
    TableColumn<Ticket, String> columnDni;

    /**
     * Columna nombre de la tabla.
     */
    @FXML
    TableColumn<Ticket, String> columnNombre;

    /**
     * Columna fecha de la tabla.
     */
    @FXML
    TableColumn<Ticket, String> columnFecha;

    /**
     * Escenario actual de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario actual.
     */
    private User user;

    /**
     * Estado del tema actual (claro/oscuro).
     */
    private boolean tema;

    /**
     * Logger para registros del sistema.
     */
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");

    /**
     * Lista de entradas disponibles.
     */
    private List<Ticket> tickets;

    /**
     * Carga los datos de los tickets en la tabla
     */
    private void cargarTabla() {
        initializeTableColumns();
        // Convertir ArrayList a ObservableList
        ObservableList<Ticket> observableEvents = FXCollections.observableArrayList(tickets);
        // Cargar datos en la tabla
        tableEntradas.setItems(observableEvents);
    }

    /**
     * Inicializa las columnas de la tabla con sus fábricas de valores.
     */
    private void initializeTableColumns() {
        columnDni.setCellValueFactory(new PropertyValueFactory<>("dniAsistentes"));
        columnNombre.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getEvent() != null
                        ? cellData.getValue().getEvent().getNombre()
                        : "Sin Evento")
        );
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
    }

    /**
     * Cambia el tema visual de la interfaz entre oscuro y claro.
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
     * Aplica el tema seleccionado a la interfaz.
     */
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

    /**
     * Gestiona el menú contextual al hacer clic derecho.
     */
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

    /**
     * Inicializa el escenario y carga los datos de la tabla.
     */
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
        if (!user.getIsAdmin()) {
            Ticket[] ticketList = TicketManagerFactory.get().findTicketByUser_XML(Ticket[].class, ((Client) user).getDni());
            tickets = Arrays.asList(ticketList);
        } else {
            Ticket[] ticketList = TicketManagerFactory.get().findAll_XML(Ticket[].class);
            tickets = Arrays.asList(ticketList);
        }
        cargarTabla();

        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");
    }

}
