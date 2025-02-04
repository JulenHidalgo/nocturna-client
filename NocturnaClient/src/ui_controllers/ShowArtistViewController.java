/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import exceptions.ReadException;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.EventManagerFactory;
import model.Artist;
import model.Event;
import model.User;
import utils.CustomAlert;

/**
 *
 * @author Julen Hidalgo
 */
public class ShowArtistViewController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    HBox hbMenu;

    @FXML
    Label lblNombre;

    @FXML
    Label lblTipoMusica;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Label lblDescripcion;

    @FXML
    TextField tfBusqueda;

    @FXML
    ComboBox cbFiltrar;

    @FXML
    DatePicker dpDesde;

    @FXML
    DatePicker dpHasta;

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Event, String> tcNombreEvento;

    @FXML
    TableColumn<Event, String> tcNombreSala;

    @FXML
    TableColumn<Event, Date> tcFechaEvento;

    @FXML
    Button btnSeleccionar;

    private Stage stage;

    private User user;

    private Artist artist;

    private boolean tema;

    private List<Event> events;

    private List<Event> eventsCopia;

    private final Logger LOGGER = Logger.getLogger("ShowArtistViewController.view");

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void initStage(Parent root) throws IOException {
        LOGGER.info("Initializing 'ShowArtistView' window.");

        Scene scene = new Scene(root);

        initializeInfo();
        changeTheme();
        initializeControlListeners();

        stage.setScene(scene);
        stage.show();

        LOGGER.info("'ShowArtistView' window initialized.");

    }

    private void initializeControlListeners() {
        tfBusqueda.textProperty().addListener((observable, oldValue, newValue) -> loadTableData());

        cbFiltrar.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tfBusqueda.setDisable(false);
            loadTableData();
        });

        dpDesde.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dpDesde.getValue() == null) {
                dpHasta.setVisible(false);
            } else {
                dpHasta.setVisible(true);
            }
            loadTableData();
        });

        dpHasta.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadTableData();
        });

        tableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Artist>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Artist> c) {
                if (c.getList().isEmpty()) {
                    btnSeleccionar.setDisable(true);
                } else {
                    btnSeleccionar.setDisable(false);
                }
            }
        });
        btnSeleccionar.setOnAction(this::goToShowEventView);
        stage.setOnCloseRequest(this::closeAppFromX);
    }

    private void initializeInfo() {
        user = Sesion.getUser();
        stage = Sesion.getStage();
        tema = Sesion.getTema();
        if (artist != null) {
            lblNombre.setText(artist.getNombre());
            lblDescripcion.setText(artist.getDescripcion());
            lblTipoMusica.setText(artist.getTipoMusica());

            cbFiltrar.getItems().add("Nombre");
            cbFiltrar.getItems().add("Sala");

            loadTableData();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("Cambiar tema");
            item1.setOnAction(a -> {
                Sesion.setTema(!Sesion.getTema());
                changeTheme();
            });
            contextMenu.getItems().addAll(item1);

            anchorPane.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
        }
    }

    private void loadTableData() {
        try {
            events = Arrays.asList(EventManagerFactory.get().findByArtist_XML(Event[].class, artist.getId().toString()));
            initializeTableColumns();
            applyFilters();
            // Convertir ArrayList a ObservableList
            ObservableList<Event> observableArtist = FXCollections.observableArrayList(eventsCopia);
            // Cargar datos en la tabla
            tableView.setItems(observableArtist);
        } catch (ReadException e) {

        }
    }

    private void initializeTableColumns() {
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tcNombreEvento.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcFechaEvento.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tcNombreSala.setCellValueFactory(cellData -> {
            String nombreClub = cellData.getValue().getClub().getNombre();
            return new SimpleStringProperty(nombreClub != null ? nombreClub : "Sin Club");
        });
    }

    private void applyFilters() {
        eventsCopia = events;
        if (!tfBusqueda.getText().isEmpty()) {
            if (cbFiltrar.getSelectionModel().getSelectedIndex() == 0) {
                eventsCopia = eventsCopia.stream().filter(event -> event.getNombre().startsWith(tfBusqueda.getText())).collect(Collectors.toList());
            } else {
                eventsCopia = eventsCopia.stream().filter(event -> event.getClub().getNombre().startsWith(tfBusqueda.getText())).collect(Collectors.toList());
            }
        }

        if (dpDesde.getValue() != null) {
            Date fechaConvertidaDesde = Date.from(dpDesde.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (dpHasta.isVisible() && dpHasta.getValue() != null) {
                eventsCopia = eventsCopia.stream()
                        .filter(event -> event.getFecha().after(fechaConvertidaDesde))
                        .collect(Collectors.toList());
            } else {
                eventsCopia = eventsCopia.stream()
                        .filter(event -> event.getFecha().equals(fechaConvertidaDesde))
                        .collect(Collectors.toList());
            }
        }

        if (dpHasta.isVisible() && dpHasta.getValue() != null) {
            Date fechaConvertidaHasta = Date.from(dpHasta.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            eventsCopia = eventsCopia.stream()
                    .filter(event -> event.getFecha().before(fechaConvertidaHasta))
                    .collect(Collectors.toList());

        }
    }

    private void goToShowEventView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));

            Parent root = loader.load();

            ShowEventViewController controller = (ShowEventViewController) loader.getController();

            controller.setEvent((Event) tableView.getSelectionModel().getSelectedItem());

            controller.initStage(root);
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error con la sincronización de las ventanas, intentalo más tarde");
        }
    }

    private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    private void changeTheme() {
        String currentStyle = anchorPane.getStyle();
        if (Sesion.getTema()) {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }

    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(anchorPane, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
    }

}
