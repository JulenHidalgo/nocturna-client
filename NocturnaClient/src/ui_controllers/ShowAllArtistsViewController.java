/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import exceptions.ReadException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
 * @author Julen Hidalgo
 */
public class ShowAllArtistsViewController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    HBox hbMenu;

    @FXML
    TextField tfFiltroNombre;

    @FXML
    TextField tfFiltroMusica;

    @FXML
    TableView<Artist> tvArtists;

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

    Set<Artist> seleccionados = new HashSet<>();

    List<Artist> seleccionadosBusqueda = new ArrayList<>();

    private int cantidadArtistas = 0;

    private final Logger LOGGER = Logger.getLogger("ShowAllArtistView.view");

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

    public Set<Artist> getSeleccionados() {
        return seleccionados;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing 'ShowAllArtists' window.");

        Scene scene = new Scene(root);

        changeTheme();
        initializeInfo();
        initializeControlListeners();

        stage.setScene(scene);
        stage.show();

        LOGGER.info("'ShowAllArtists' window initialized.");

    }

    private void initializeControlListeners() {
        stage.setOnCloseRequest(this::closeAppFromX);
        btnEliminar.setOnAction(this::deleteArtist);
        if (event == null) {
            btnSeleccionar.setOnAction(this::goToShowArtistView);
        } else {
            btnSeleccionar.setOnAction(this::seleccionarArtistasEvento);
        }

        tfFiltroNombre.textProperty().addListener((observable, oldValue, newValue) -> loadTableData());
        tfFiltroMusica.textProperty().addListener((observable, oldValue, newValue) -> loadTableData());

        if (event == null) {
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
        }

    }

    private void initializeInfo() {
        user = new User();
        user.setIsAdmin(true);
        if (event == null) {
            if (user.getIsAdmin()) {
                btnCrear.setOnAction(this::crearArtista);
                tvArtists.setEditable(true);
                tcEventos.setEditable(false);
                tvArtists.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tcNombre.setCellFactory(column -> new ArtistEditingCell(this));
                tcTipoMusica.setCellFactory(column -> new ArtistEditingCell(this));
                tcDescripcion.setCellFactory(column -> new ArtistEditingCell(this));
                btnSeleccionar.setDisable(true);
            } else {
                tvArtists.setEditable(false);
            }
            stage.setTitle("Visualizar artistas");
            tcEventos.setText("¿Tiene eventos?");
        } else {
            stage.setTitle("Selector de artistas");
            tcEventos.setText("¿Seleccionado?");
            tvArtists.setEditable(true);
            tcNombre.setEditable(false);
            tcTipoMusica.setEditable(false);
            tcDescripcion.setEditable(false);
            tcEventos.setEditable(true);
            tcEventos.setCellFactory(column -> new ArtistEditingCell(this));
            btnCrear.setVisible(false);
            btnEliminar.setVisible(false);
            try {
                seleccionadosBusqueda = Arrays.asList(ArtistManagerFactory.get().findByEvent_XML(Artist[].class, String.valueOf(event.getId())));
            } catch (ReadException e) {

            }

        }

        loadTableData();
    }

    private void loadTableData() {
        artists = Arrays.asList(ArtistManagerFactory.get().findAll_XML(Artist[].class));
        initializeTableColumns();
        applyFilters();
        // Convertir ArrayList a ObservableList
        ObservableList<Artist> observableArtist = FXCollections.observableArrayList(artistsCopia);
        // Cargar datos en la tabla
        tvArtists.setItems(observableArtist);
    }

    private void initializeTableColumns() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcTipoMusica.setCellValueFactory(new PropertyValueFactory<>("tipoMusica"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tcEventos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Artist, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Artist, String> param) {
                long id;
                id = param.getValue().getId();
                if (event == null) {
                    return new SimpleStringProperty(comprobarEventos(id));
                } else {
                    return new SimpleStringProperty(comprobarSeleccionado(id));
                }
            }
        });
    }

    private void applyFilters() {
        artistsCopia = artists;

        if (!tfFiltroNombre.getText().isEmpty()) {
            artistsCopia = artistsCopia.stream().filter(artist -> artist.getNombre().startsWith(tfFiltroNombre.getText())).collect(Collectors.toList());
        }

        if (!tfFiltroMusica.getText().isEmpty()) {
            artistsCopia = artistsCopia.stream().filter(artist -> artist.getTipoMusica().startsWith(tfFiltroMusica.getText())).collect(Collectors.toList());
        }

    }

    private void crearArtista(ActionEvent event) {
        Artist artist = new Artist();
        ArtistManagerFactory.get().create_XML(artist);
        loadTableData();
        int lastIndex = tvArtists.getItems().size() - 1;
        tvArtists.getSelectionModel().clearAndSelect(lastIndex);
    }

    private void deleteArtist(ActionEvent event) {
        ObservableList<Artist> selectedArtist = tvArtists.getSelectionModel().getSelectedItems();
        selectedArtist.forEach(item -> {
            ArtistManagerFactory.get().remove(item.getId().toString());
        });
        loadTableData();
    }

    private String comprobarSeleccionado(Long id) {
        cantidadArtistas++;
        for (Artist ar : seleccionadosBusqueda) {
            if (ar.getId().equals(id)) {
                if (cantidadArtistas <= artistsCopia.size() + 1) {
                    this.seleccionados.add(ar);
                }
                return "Sí";
            }
        }
        return "No";

    }

    private String comprobarEventos(Long id) {
        try {
            EventManagerFactory.get().findByArtist_XML(Event[].class, String.valueOf(id));
            return "Sí";
        } catch (ReadException e) {
            return "No";
        }
    }

    public void seleccionarArtistasEvento(ActionEvent event) {
        this.event.setArtists(seleccionados);
        EventManagerFactory.get().edit_XML(this.event, this.event.getId().toString());
        CustomAlert.throwAlertCustom(Alert.AlertType.INFORMATION, "La información se ha guardado correctamente");
        goToShowEventView();
    }

    private void goToShowEventView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));

            Parent root = loader.load();

            ShowEventViewController controller = (ShowEventViewController) loader.getController();

            controller.setStage(this.stage);
            controller.setTema(this.tema);
            controller.setUser(this.user);
            controller.setEvent(this.event);

            controller.initStage(root);
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error con la sincronización de las ventanas, intentalo más tarde");
        }
    }

    private void goToShowArtistView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showArtistView.fxml"));

            Parent root = loader.load();

            ShowArtistViewController controller = (ShowArtistViewController) loader.getController();

            controller.setStage(stage);
            controller.setTema(tema);
            controller.setArtist((Artist) tvArtists.getSelectionModel().getSelectedItem());
            controller.setUser(user);

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
        if (tema) {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }

}
