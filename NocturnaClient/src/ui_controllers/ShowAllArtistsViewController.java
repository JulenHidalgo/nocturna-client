/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import javafx.util.Callback;
import logic.ArtistManagerFactory;
import logic.EventManagerFactory;
import model.Artist;
import model.Event;
import model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import utils.ArtistEditingCell;
import utils.CustomAlert;

/**
 * Controlador de la ventana que muestra todos los artista dejando, crearlos,
 * modificarlos y borrarlos
 *
 * @author Julen Hidalgo
 */
public class ShowAllArtistsViewController {

    /**
     * Panel principal de la interfaz.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * Barra de menú horizontal.
     */
    @FXML
    private HBox hbMenu;

    /**
     * Filtro por nombre del artista.
     */
    @FXML
    private TextField tfFiltroNombre;

    /**
     * Filtro por tipo de música.
     */
    @FXML
    private TextField tfFiltroMusica;

    /**
     * Tabla que muestra los artistas.
     */
    @FXML
    private TableView<Artist> tvArtists;

    /**
     * Columna de nombres en la tabla.
     */
    @FXML
    private TableColumn tcNombre;

    /**
     * Columna de tipo musical.
     */
    @FXML
    private TableColumn tcTipoMusica;

    /**
     * Columna de descripción.
     */
    @FXML
    private TableColumn tcDescripcion;

    /**
     * Columna de eventos.
     */
    @FXML
    private TableColumn tcEventos;

    /**
     * Botón para seleccionar artista.
     */
    @FXML
    private Button btnSeleccionar;

    /**
     * Botón para crear nuevo artista.
     */
    @FXML
    private Button btnCrear;

    /**
     * Botón para eliminar artista.
     */
    @FXML
    private Button btnEliminar;

    /**
     * controlador del menú
     */
    @FXML
    private MenuController menuIncludeController;

    /**
     * Escenario actual de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario autenticado.
     */
    private User user;

    /**
     * Evento en curso.
     */
    private Event event;

    /**
     * Indicador del tema visual.
     */
    private boolean tema;

    /**
     * Lista de artistas disponibles.
     */
    private List<Artist> artists;

    /**
     * Copia de seguridad de la lista de artistas.
     */
    private List<Artist> artistsCopia;

    /**
     * Artistas seleccionados.
     */
    private Set<Artist> seleccionados = new HashSet<>();

    /**
     * Resultados de búsqueda filtrada.
     */
    private List<Artist> seleccionadosBusqueda = new ArrayList<>();

    /**
     * Contador de artistas totales.
     */
    private int cantidadArtistas = 0;

    /**
     * Logger del controlador.
     */
    private final Logger LOGGER = Logger.getLogger("ShowAllArtistView.view");

    /**
     * Recoge la ventana.
     *
     * @param stage Ventana de la aplicacion
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Recoge el usuario autenticado.
     *
     * @param user Usuario de la sesion
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Recoge el evento seleccionado.
     *
     * @param event Evento del artista
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Recoge el tema oscuro o claro.
     *
     * @param tema Tema de la aplicacion
     */
    public void setTema(boolean tema) {
        this.tema = tema;
    }

    /**
     * Obtiene los artistas seleccionados.
     *
     * @return Set&lt;model.Artist&gt; Lista de artistas dados de alta en la app
     */
    public Set<Artist> getSeleccionados() {
        return seleccionados;
    }

    /**
     * Inicializa la ventana
     *
     * @param root El nodo raíz que se agrega a la escena.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing 'ShowAllArtists' window.");

        Scene scene = new Scene(root);

        initializeInfo();
        changeTheme();
        initializeControlListeners();

        menuIncludeController.checkAdmin(user.getIsAdmin());

        stage.setScene(scene);
        stage.show();

        LOGGER.info("'ShowAllArtists' window initialized.");

    }

    /**
     * Inicializa los eventos y listeners de la interfaz.
     */
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

    /**
     * Carga los datos en la tabla de artistas.
     */
    private void initializeInfo() {
        try {
            user = Sesion.getUser();
            stage = Sesion.getStage();
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
                    btnCrear.setVisible(false);
                    btnEliminar.setVisible(false);
                    tvArtists.setEditable(false);
                }
                stage.setTitle("Visualizar artistas");
                tcEventos.setText("¿Tiene eventos?");

                ContextMenu contextMenu = new ContextMenu();
                MenuItem item1 = new MenuItem("Cambiar tema");
                item1.setOnAction(a -> {
                    Sesion.setTema(!Sesion.getTema());
                    changeTheme();
                });
                MenuItem item2 = new MenuItem("Imprimir los datos de la tabla");
                item2.setOnAction(this::imprimirTabla);
                contextMenu.getItems().addAll(item1, item2);

                anchorPane.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));
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
                seleccionadosBusqueda = Arrays.asList(ArtistManagerFactory.get().findByEvent_XML(Artist[].class, String.valueOf(event.getId())));

            }
        } catch (ReadException e) {
            LOGGER.info("The Event does not have any Artist related");
        } catch (InternalServerErrorException e) {
            LOGGER.severe("An InternalServerErrorException ocurred while initializing the info of the view");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, e.getMessage());
        }

        loadTableData();
    }

    /**
     * Carga el report de la tabla de artistas
     *
     * @param event
     */
    private void imprimirTabla(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/ArtistReport.jrxml"));

            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Artist>) this.tvArtists.getItems());

            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(ShowAllClubsViewController.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Carga los datos en la tabla de artistas.
     */
    private void loadTableData() {
        artists = Arrays.asList(ArtistManagerFactory.get().findAll_XML(Artist[].class
        ));
        initializeTableColumns();
        applyFilters();
        // Convertir ArrayList a ObservableList
        ObservableList<Artist> observableArtist = FXCollections.observableArrayList(artistsCopia);
        // Cargar datos en la tabla
        tvArtists.setItems(observableArtist);
    }

    /**
     * Inicializa las columnas de la tabla.
     */
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

    /**
     * Aplica filtros a la lista de artistas en base a los campos de búsqueda.
     */
    private void applyFilters() {
        artistsCopia = artists;

        if (!tfFiltroNombre.getText().isEmpty()) {
            artistsCopia = artistsCopia.stream().filter(artist -> artist.getNombre().startsWith(tfFiltroNombre.getText())).collect(Collectors.toList());
        }

        if (!tfFiltroMusica.getText().isEmpty()) {
            artistsCopia = artistsCopia.stream().filter(artist -> artist.getTipoMusica().startsWith(tfFiltroMusica.getText())).collect(Collectors.toList());
        }

    }

    /**
     * Crea un artista y lo añade a la tabla
     *
     * @param event
     */
    private void crearArtista(ActionEvent event) {
        try {
            Artist artist = new Artist();
            ArtistManagerFactory.get().create_XML(artist);
            loadTableData();
            int lastIndex = tvArtists.getItems().size() - 1;
            tvArtists.getSelectionModel().clearAndSelect(lastIndex);
        } catch (InternalServerErrorException ex) {
            LOGGER.severe("The server throwed an InternalServerErrorException");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("The server throwed an Exception");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error, intentalo de nuevo más tarde");
        }

    }

    /**
     * Borra el artista seleccionado o seleccionados
     *
     * @param event
     */
    private void deleteArtist(ActionEvent event) {
        try {
            ObservableList<Artist> selectedArtist = tvArtists.getSelectionModel().getSelectedItems();
            selectedArtist.forEach(item -> {
                try {
                    ArtistManagerFactory.get().remove(item.getId().toString());
                } catch (InternalServerErrorException ex) {
                    LOGGER.severe("An error occurred while deleting an artist");
                    CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error en el servidor");
                }
            });
            loadTableData();
        } catch (Exception ex) {
            LOGGER.severe("An error occurred");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error");
        }

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
            EventManagerFactory.get().findByArtist_XML(Event[].class,
                    String.valueOf(id));
            return "Sí";
        } catch (ReadException e) {
            return "No";
        }
    }

    /**
     * guarda los nuevos artistas seleccionado en el evento
     *
     * @param event Evento del cuual se recogen los artistas
     */
    public void seleccionarArtistasEvento(ActionEvent event) {
        this.event.setArtists(seleccionados);
        EventManagerFactory.get().edit_XML(this.event, this.event.getId().toString());
        CustomAlert.throwAlertCustom(Alert.AlertType.INFORMATION, "La información se ha guardado correctamente");
        goToShowEventView();
    }

    /**
     * Muestra la ventana de visualización de un artista seleccionado.
     *
     * @param event Evento de acción.
     */
    private void goToShowEventView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));

            Parent root = loader.load();

            ShowEventViewController controller = (ShowEventViewController) loader.getController();

            controller.setEvent(this.event);

            controller.initStage(root);
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error con la sincronización de las ventanas, intentalo más tarde");
        }
    }

    /**
     * va a la ventana de ver un artista seleccionado
     *
     * @param event
     */
    private void goToShowArtistView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showArtistView.fxml"));

            Parent root = loader.load();

            ShowArtistViewController controller = (ShowArtistViewController) loader.getController();

            controller.setArtist((Artist) tvArtists.getSelectionModel().getSelectedItem());

            controller.initStage(root);
        } catch (IOException e) {
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error con la sincronización de las ventanas, intentalo más tarde");
        }

    }

    /**
     * cuando el usuario le da a la "X" pide confirmacion y sale de la
     * aplicacion
     *
     * @param event
     */
    private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    /**
     * Cambia el estilo del fondno y los textos
     */
    private void changeTheme() {
        String currentStyle = anchorPane.getStyle();
        if (Sesion.getTema()) {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
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
