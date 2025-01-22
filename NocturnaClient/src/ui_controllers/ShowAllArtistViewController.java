/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ArtistManagerFactory;
import logic.EventManager;
import logic.EventManagerFactory;
import model.Artist;
import model.Club;
import model.Event;
import model.User;
import utils.CustomAlert;

/**
 *
 * @author 2dam
 */
public class ShowAllArtistViewController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    HBox hbMenu;

    @FXML
    TextField tfFiltroNombre;

    @FXML
    TextField tfFiltroMusica;

    @FXML
    TableView tvArtists;

    @FXML
    TableColumn tcNombre;

    @FXML
    TableColumn tcTipoMusica;

    @FXML
    TableColumn tcDescripcion;

    @FXML
    TableColumn tcEventos;

    @FXML
    Button btnSeleccionar;

    @FXML
    Button btnCrear;

    @FXML
    Button btnEliminar;

    private Stage stage;

    private User user;

    private Event event;

    private boolean tema;

    List<Artist> artists;

    List<Artist> artistsCopia;

    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing 'ShowAllArtists' window.");
        Scene scene = new Scene(root);

        if (event == null) {
            if (user.getIsAdmin()) {
                tvArtists.setEditable(true);
            }
            stage.setTitle("Visualizar artistas");

            tcEventos.setText("¿Tiene eventos?");

        } else {
            stage.setTitle("Selector de artistas");
            tcEventos.setText("¿Seleccionado?");
        }

        recogerArtistas();
        cambiarTema();

        stage.setOnCloseRequest(this::closeAppFromX);

        tfFiltroNombre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                aplicarFiltros();
            }
        });

        tfFiltroMusica.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                aplicarFiltros();
            }
        });

        stage.show();
        stage.setScene(scene);
        LOGGER.info("'ShowAllArtists' window initialized.");

    }

    private void recogerArtistas() {
        artists = Arrays.asList(ArtistManagerFactory.get().findAll_XML(Artist[].class));
    }

    private void aplicarFiltros() {
        artistsCopia = artists;

        if (!tfFiltroNombre.getText().isEmpty()) {
            artistsCopia = artists.stream().filter(artist -> artist.getNombre().startsWith(tfFiltroNombre.getText())).collect(Collectors.toList());
        }

        if (!tfFiltroMusica.getText().isEmpty()) {
            artistsCopia = artists.stream().filter(artist -> artist.getTipoMusica().startsWith(tfFiltroMusica.getText())).collect(Collectors.toList());
        }

    }

    private void cargarTabla(List<Event> tableEvents) {
        initializeTableColumns();
        // Convertir ArrayList a ObservableList
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(tableEvents);
        // Cargar datos en la tabla
        tablaEvent.setItems(observableEvents);

    }

    private void initializeTableColumns() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcTipoMusica.setCellValueFactory(new PropertyValueFactory<>("tipoMusica"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tcEventos.setCellValueFactory(cellData -> {
            Long artistId = cellData.getValue().getId(); // Obtener el ID del artista actual
            boolean tieneEventos = comprobarEventos(artistId);
            return new SimpleBooleanProperty(tieneEventos);
        });
        tcEventos.setCellFactory(CheckBoxTableCell.forTableColumn(tcEventos));

    }

    private boolean comprobarEventos(Long id) {
        if (EventManagerFactory.get().findByArtist_XML(Event.class, String.valueOf(id)) != null) {
            return true;
        }
        return false;
    }

    public void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    public void cambiarTema() {
        if (tema) {

        } else {

        }
    }

}
