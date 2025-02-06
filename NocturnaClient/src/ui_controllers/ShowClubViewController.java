/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.EventManager;
import logic.EventManagerFactory;
import model.Club;
import model.Event;
import model.User;
import utils.CustomAlert;

/**
 * Controlador para la vista que muestra la información detallada de un club y
 * sus eventos asociados.
 *
 * @author Adrian Rocha
 */
public class ShowClubViewController {

    /**
     * Escenario principal de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario actual.
     */
    private User user;

    /**
     * Indica el tema actual de la interfaz (claro/oscuro).
     */
    private boolean tema;

    /**
     * Club que se está mostrando.
     */
    private Club club;

    /**
     * Lista de eventos asociados al club.
     */
    private List<Event> events;

    /**
     * Gestor de eventos.
     */
    private EventManager eventManager;

    /**
     * Panel principal de la vista.
     */
    @FXML
    private AnchorPane anchorPane;
    
    /**
     * Etiqueta para mostrar el nombre del club.
     */
    @FXML
    private Label lblNom;

    /**
     * Campo de texto para mostrar el nombre del club.
     */
    @FXML
    private Label lblNombre;

    /**
     * Botón para obtener más información sobre un evento seleccionado.
     */
    @FXML
    private Button btnInfo;
    
    /**
     * Etiqueta para mostrar la ciudad del club.
     */
    @FXML
    private Label lblCiu;

    /**
     * Campo de texto para mostrar la ciudad del club.
     */
    @FXML
    private Label lblCiudad;
    
    /**
     * Etiqueta para mostrar la ubicación del club.
     */
    @FXML
    private Label lblUbi;

    /**
     * Campo de texto para mostrar la ubicación del club.
     */
    @FXML
    private Label lblUbicacion;
    
    /**
     * Etiqueta para mostrar las redes del club.
     */
    @FXML
    private Label lblRedes;
    
    /**
     * Etiqueta para mostrar el evento club.
     */
    @FXML
    private Label lblEvento;

    /**
     * Imagen que representa las redes sociales del club.
     */
    @FXML
    private ImageView imgRedes;

    /**
     * Tabla para mostrar los eventos del club.
     */
    @FXML
    private TableView<Event> tableEvents;

    /**
     * Columna de la tabla para mostrar el nombre del evento.
     */
    @FXML
    private TableColumn<Event, String> columnNombre;

    /**
     * Columna de la tabla para mostrar la fecha del evento.
     */
    @FXML
    private TableColumn<Event, String> columnFecha;

    /**
     * controlador del menú
     */
    @FXML
    private MenuController menuIncludeController;

    /**
     * Logger para registrar eventos.
     */
    private final Logger LOGGER = Logger.getLogger("Show club view");

    /**
     * Establece el club que se va a mostrar en la vista.
     *
     * @param club El club que se va a mostrar.
     */
    public void setClub(Club club) {
        this.club = club;
    }

    /**
     * Establece el escenario principal de la aplicación.
     *
     * @param stage El escenario principal.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece el usuario actual.
     *
     * @param user El usuario actual.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Obtiene el tema actual de la interfaz.
     *
     * @return boolean
     */
    public boolean getTema() {
        return this.tema;
    }

    /**
     * Establece el tema de la interfaz.
     *
     * @param tema true para tema oscuro, false para tema claro.
     */
    public void setTema(boolean tema) {
        this.tema = tema;
    }

    /**
     * Inicializa la ventana y configura los componentes de la interfaz.
     *
     * @param root El nodo raíz que se agrega a la escena.
     * @throws Exception Si ocurre un error durante la inicialización.
     */
    public void initStage(Parent root) throws Exception {
        try {
            LOGGER.info("Initializing Show all clubs window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            lblNombre.setText(club.getNombre());
            lblCiudad.setText(club.getCiudad());
            lblUbicacion.setText(club.getUbicacion());
            imgRedes.setOnMouseReleased(this::clickRedes);

            eventManager = EventManagerFactory.get();

            events = getEventsInfo();
            LOGGER.info("Setting table properties and data.");
            columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            setTableData();
            btnInfo.setDisable(true);
            changeTheme();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("Cambiar tema");
            item1.setOnAction(this::cambiarTema);
            MenuItem item2 = new MenuItem("Imprimir los datos de la tabla");
            contextMenu.getItems().addAll(item1, item2);

            anchorPane.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));

            menuIncludeController.checkAdmin(user.getIsAdmin());

            stage.setTitle("Vissualizar club");
            stage.show();

            btnInfo.setOnAction(this::masInfo);

            tableEvents.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Event>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Event> c) {
                    if (c.getList().isEmpty()) {
                        btnInfo.setDisable(true);
                    } else if (c.getList().size() == 1) {
                        btnInfo.setDisable(false);
                    } else if (c.getList().size() > 1) {
                        btnInfo.setDisable(true);
                    }
                }
            });
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting up the window", ex.getMessage());
            throw new Exception("ERROR INICIALIZANDO LA VENTANA");
        }
    }

    /**
     * Cambia el tema de la interfaz entre claro y oscuro.
     *
     * @param event
     */
    private void cambiarTema(ActionEvent event) {
        if (getTema()) {
            setTema(false);
        } else {
            setTema(true);
        }
        changeTheme();
    }

    /**
     * Controla la apertura y cierre del menú contextual.
     *
     * @param event
     * @param menu El menú contextual a controlar.
     */
    private void controlMenuConceptual(MouseEvent event, ContextMenu menu) {
        if (event.getButton() == MouseButton.SECONDARY) {
            menu.show(anchorPane, event.getScreenX(), event.getScreenY());
        } else {
            menu.hide();
        }
    }

    /**
     * Abre la página de Instagram del club en el navegador.
     *
     * @param event
     */
    private void clickRedes(MouseEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(club.getInstagram()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception logging up .", e.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR CREANDO USUARIO");
        }
    }

    /**
     * Establece los datos en la tabla de eventos.
     */
    private void setTableData() {
        try {
            ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
            if (observableEvents.size() > 0) {
                tableEvents.setItems(observableEvents);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting table data", ex.getMessage());
        }
    }

    /**
     * Muestra más información sobre un evento seleccionado.
     *
     * @param event El evento de acción que desencadena este método.
     */
    private void masInfo(ActionEvent event) {
        try {
            Event eventTable = tableEvents.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));

            Parent root = loader.load();

            ShowEventViewController controller = (ShowEventViewController) loader.getController();

            controller.setEvent(eventTable);
            controller.initStage(root);
        } catch (Exception ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cambia el tema de la interfaz gráfica.
     */
    private void changeTheme() {
        String currentStyle = anchorPane.getStyle();
        if (tema) {
            lblCiu.setStyle("-fx-text-fill: black;");
            lblCiudad.setStyle("-fx-text-fill: black;");
            lblEvento.setStyle("-fx-text-fill: black;");
            lblNom.setStyle("-fx-text-fill: black;");
            lblNombre.setStyle("-fx-text-fill: black;");
            lblRedes.setStyle("-fx-text-fill: black;");
            lblUbi.setStyle("-fx-text-fill: black;");
            lblUbicacion.setStyle("-fx-text-fill: black;");
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lblCiu.setStyle("-fx-text-fill: white;");
            lblCiudad.setStyle("-fx-text-fill: white;");
            lblEvento.setStyle("-fx-text-fill: white;");
            lblNom.setStyle("-fx-text-fill: white;");
            lblNombre.setStyle("-fx-text-fill: white;");
            lblRedes.setStyle("-fx-text-fill: white;");
            lblUbi.setStyle("-fx-text-fill: white;");
            lblUbicacion.setStyle("-fx-text-fill: white;");
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }

    /**
     * Obtiene la lista de eventos asociados al club.
     *
     * @return List<Event>
     * @throws Exception Si ocurre un error al obtener los eventos.
     */
    private List<Event> getEventsInfo() throws Exception {
        try {
            Event[] eventsArray = eventManager.findAll_XML(Event[].class);
            List<Event> eventsList = new ArrayList<>();
            for (int i = 0; i < eventsArray.length; i++) {
                if (Objects.equals(club.getId(), eventsArray[i].getClub().getId())) {
                    eventsList.add(eventsArray[i]);
                }
            }
            return eventsList;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception getting clubs info", ex.getMessage());
            throw new Exception("ERROR RECOGIENDO CLUBES");
        }
    }
}
