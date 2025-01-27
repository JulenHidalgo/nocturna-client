/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import exceptions.ReadException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import logic.ArtistManagerFactory;
import logic.EventManagerFactory;
import model.Artist;
import model.Event;
import model.User;
import utils.ArtistEditingCell;
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
        try {
            LOGGER.info("Initializing 'ShowAllArtists' window.");

            Scene scene = new Scene(root);

            initializeComponents();

            stage.setOnCloseRequest(this::closeAppFromX);
            btnEliminar.setOnAction(this::deleteArtist);
            btnSeleccionar.setOnAction(this::irShowArtist);

            tvArtists.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Artist>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Artist> c) {
                    if (c.getList().isEmpty()) {
                        btnSeleccionar.setDisable(true);
                        btnEliminar.setDisable(true);
                    } else if (c.getList().size() == 1) {
                        btnSeleccionar.setDisable(false);
                        btnEliminar.setDisable(false);
                    } else if (c.getList().size() > 1) {
                        btnSeleccionar.setDisable(true);
                        btnEliminar.setDisable(false);
                    }
                }
            });

            tfFiltroNombre.textProperty().addListener((observable, oldValue, newValue) -> cargarTabla());
            tfFiltroMusica.textProperty().addListener((observable, oldValue, newValue) -> cargarTabla());

            stage.setScene(scene);
            stage.show();

            LOGGER.info("'ShowAllArtists' window initialized.");

        } catch (Exception e) {
            LOGGER.severe("Error while initializing 'ShowAllArtists' window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        user = new User();
        user.setIsAdmin(true);
        if (event == null) {
            if (user.getIsAdmin()) {
                btnCrear.setOnAction(this::crearArtista);
                tvArtists.setEditable(true);
                tcEventos.setEditable(false);
                tvArtists.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tcNombre.setCellFactory(column -> new ArtistEditingCell());
                tcTipoMusica.setCellFactory(column -> new ArtistEditingCell());
                tcDescripcion.setCellFactory(column -> new ArtistEditingCell());
            } else {
                tvArtists.setEditable(false);
            }
            stage.setTitle("Visualizar artistas");
            tcEventos.setText("¿Tiene eventos?");
        } else {
            stage.setTitle("Selector de artistas");
            tcEventos.setText("¿Seleccionado?");
            tvArtists.setEditable(false);
            if (user.getIsAdmin()) {
                tcEventos.setEditable(true);
            }
        }

        tcEventos.setEditable(false);
        cargarTabla();
        cambiarTema();
    }

    private void recogerArtistas() {
        Artist[] artistArray = ArtistManagerFactory.get().findAll_XML(Artist[].class);
        artists = Arrays.asList(artistArray);
    }

    private void crearArtista(ActionEvent event) {
        Artist artist = new Artist();
        ArtistManagerFactory.get().create_XML(artist);
        cargarTabla();
        int lastIndex = tvArtists.getItems().size() - 1;
        tvArtists.getSelectionModel().clearAndSelect(lastIndex);
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

    private void irShowArtist(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showArtistView.fxml"));

        Parent root = loader.load();

        ShowArtistView controller = (ShowArtistView) loader.getController();

        controller.setStage(stage);
        controller.setTema(tema);
        controller.setArtist(tvArtists.getSelectionModel().getSelectedItem());
        controller.setUser(user);

        controller.initStage(root);
    }

    private void cargarTabla() {
        recogerArtistas();
        initializeTableColumns();
        aplicarFiltros();
        // Convertir ArrayList a ObservableList
        ObservableList<Artist> observableArtist = FXCollections.observableArrayList(artistsCopia);
        // Cargar datos en la tabla
        tvArtists.setItems(observableArtist);

    }

    private void deleteArtist(ActionEvent event) {
        ObservableList<Artist> selectedArtist = tvArtists.getSelectionModel().getSelectedItems();
        selectedArtist.forEach(item -> {
            ArtistManagerFactory.get().remove(item.getId().toString());
        });
        cargarTabla();
    }

    private void initializeTableColumns() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcTipoMusica.setCellValueFactory(new PropertyValueFactory<>("tipoMusica"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tcEventos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Artist, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Artist, Boolean> param) {
                long id;
                id = param.getValue().getId();
                if (event == null) {
                    return new SimpleBooleanProperty(comprobarEventos(id));
                } else {
                    return new SimpleBooleanProperty(comprobarSeleccionado(id));
                }
            }
        });

        tcEventos.setCellFactory(CheckBoxTableCell.forTableColumn(tcEventos));

    }

    private boolean comprobarSeleccionado(Long id) {
        try {
            Artist[] seleccionados = ArtistManagerFactory.get().findByEvent_XML(Artist[].class, String.valueOf(id));

            for (Artist ar : seleccionados) {
                return this.artists.contains(ar);
            }
        } catch (ReadException e) {
            return false;
        }
        return false;

    }

    private boolean comprobarEventos(Long id) {
        try {
            EventManagerFactory.get().findByArtist_XML(Event[].class, String.valueOf(id));
            return true;
        } catch (ReadException e) {
            return false;
        }
    }

    private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    private void cambiarTema() {
        if (tema) {

        } else {

        }
    }

}
