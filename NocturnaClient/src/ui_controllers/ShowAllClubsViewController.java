/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ClubManager;
import logic.ClubManagerFactory;
import logic.EventManager;
import logic.EventManagerFactory;
import model.Club;
import model.Event;
import model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import utils.ClubEditingCell;
import utils.CustomAlert;

/**
 * Controlador de la vista para mostrar y gestionar todos los clubes. Maneja la
 * interfaz de usuario y las operaciones CRUD sobre clubes.
 *
 * @author Adrian Rocha
 */
public class ShowAllClubsViewController {

    /**
     * Etapa principal de la aplicación.
     */
    private Stage stage;

    /**
     * Usuario actualmente conectado al sistema.
     */
    private User user;

    /**
     * Indicador del tema actual (claro u oscuro).
     */
    private boolean tema;

    /**
     * Lista de clubes disponibles en el sistema.
     */
    private List<Club> clubs;

    /**
     * Gestor de operaciones sobre clubes.
     */
    private ClubManager clubManager;

    /**
     * Etiqueta que muestra el título de la vista.
     */
    @FXML
    private Label lblTitulo;

    /**
     * Panel principal que contiene todos los elementos de la interfaz.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * Campo de texto para filtrar clubes por nombre.
     */
    @FXML
    private TextField txtNameFilter;

    /**
     * Selector de fecha para filtrar eventos anteriores.
     */
    @FXML
    private DatePicker dateFirst;

    /**
     * Selector de fecha para filtrar eventos posteriores.
     */
    @FXML
    private DatePicker dateSecond;

    /**
     * Tabla que muestra la lista de clubes.
     */
    @FXML
    private TableView<Club> tableClubs;

    /**
     * Columna que muestra los nombres de los clubes.
     */
    @FXML
    private TableColumn<Club, String> columnNombre;

    /**
     * Columna que muestra las ubicaciones de los clubes.
     */
    @FXML
    private TableColumn<Club, String> columnUbicacion;

    /**
     * Columna que muestra las ciudades de los clubes.
     */
    @FXML
    private TableColumn<Club, String> columnCiudad;

    /**
     * Columna que muestra los enlaces de Instagram de los clubes.
     */
    @FXML
    private TableColumn<Club, String> columnInstagram;

    /**
     * Botón para eliminar clubes seleccionados.
     */
    @FXML
    private Button btnDelete;

    /**
     * Botón para crear nuevos clubes.
     */
    @FXML
    private Button btnCreate;

    /**
     * Botón para ver más información de un club seleccionado.
     */
    @FXML
    private Button btnSelect;

    /**
     * controlador del menú
     */
    @FXML
    private MenuController menuIncludeController;

    /**
     * Logger para registrar eventos y errores del sistema.
     */
    private final Logger LOGGER = Logger.getLogger("Show all clubs view");

    /**
     * Inicializa la ventana con todos los componentes necesarios. Configura la
     * interfaz de usuario y establece los manejadores de eventos.
     *
     * @param root raíz de la escena a mostrar
     * @throws Exception si ocurre un error durante la inicialización
     */
    public void initStage(Parent root) throws Exception {
        try {
            LOGGER.info("Initializing Show all clubs window.");
            Scene scene = new Scene(root);
            user = Sesion.getUser();
            tema = Sesion.getTema();
            stage = Sesion.getStage();

            changeTheme();

            stage.setScene(scene);
            stage.setOnCloseRequest(this::closeAppFromX);
            //Si el usuario es null, significa que no ha entrado a la app todavía y 
            //está intentando registrarse, si no, significa que va a modificar su información.

            clubManager = ClubManagerFactory.get();

            clubs = getClubsInfo();
            LOGGER.info("Setting table properties and data.");
            columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            columnCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
            columnInstagram.setCellValueFactory(new PropertyValueFactory<>("instagram"));

            columnNombre.setCellFactory(column -> new ClubEditingCell());
            columnUbicacion.setCellFactory(column -> new ClubEditingCell());
            columnCiudad.setCellFactory(column -> new ClubEditingCell());
            columnInstagram.setCellFactory(column -> new ClubEditingCell());

            ContextMenu contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("Cambiar tema");
            item1.setOnAction(this::cambiarTema);
            MenuItem item2 = new MenuItem("Imprimir los datos de la tabla");
            item2.setOnAction(this::imprimirTabla);
            contextMenu.getItems().addAll(item1, item2);

            anchorPane.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));

            menuIncludeController.checkAdmin(user.getIsAdmin());

            setTableData(clubs);
            if (user.getIsAdmin() == true) {
                btnCreate.setOnAction(this::createClub);
                btnDelete.setOnAction(this::deleteClub);
                btnSelect.setDisable(true);
                btnDelete.setDisable(true);
                tableClubs.setEditable(user.getIsAdmin());
                tableClubs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else {
                btnCreate.setVisible(user.getIsAdmin());
                btnDelete.setVisible(user.getIsAdmin());
                btnSelect.setDisable(true);
            }

            stage.show();
            LOGGER.info("Show all clubs window initialized.");

            btnSelect.setOnAction(this::masInfo);

            txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });

            tableClubs.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Club>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Club> c) {
                    if (c.getList().isEmpty()) {
                        btnSelect.setDisable(true);
                        btnDelete.setDisable(true);
                    } else if (c.getList().size() == 1) {
                        btnSelect.setDisable(false);
                        btnDelete.setDisable(false);
                    } else if (c.getList().size() > 1) {
                        btnSelect.setDisable(true);
                        btnDelete.setDisable(false);
                    }
                }
            });

            dateFirst.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    aplicarFiltros();
                }
            });

            dateSecond.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    aplicarFiltros();
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception setting up de window.", e.getMessage());
            throw new Exception("ERROR INICIANDO LA VENTANA");
        }
    }

    /**
     * Aplica los filtros de búsqueda a la tabla de clubes. Considera el nombre
     * del club y los rangos de fecha seleccionados.
     */
    private void aplicarFiltros() {
        List<Club> clubsFilter = new ArrayList<>();

        if (!txtNameFilter.getText().isEmpty()) {
            for (Club club : clubs) {
                if (club.getNombre().contains(txtNameFilter.getText())) {
                    clubsFilter.add(club);
                }
            }
        } else {
            clubsFilter = clubs;
        }

        if (dateFirst.getValue() != null) {
            if (dateSecond.isVisible() && dateSecond.getValue() != null) {
                clubsFilter = setSecondDateFilter(clubsFilter);
            } else {
                clubsFilter = setFirstDateFilter(clubsFilter);
                dateSecond.setVisible(true);
            }
        } else {
            dateSecond.setVisible(false);
        }

        if (clubsFilter.size() > 0) {
            setTableData(clubsFilter);
        } else {
            tableClubs.getItems().clear();
        }
    }

    /**
     * Filtra clubes por un rango de fechas específico.
     *
     * @param clubList lista de clubes a filtrar
     * @return lista de clubes que tienen eventos en el rango de fechas
     */
    private List<Club> setSecondDateFilter(List<Club> clubList) {
        EventManager eventManager = EventManagerFactory.get();
        List<Event> events = new ArrayList<>();
        List<Club> clubsFilter = new ArrayList<>();
        Date fechaConvertidaHasta = Date.from(dateSecond.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaConvertidaDesde = Date.from(dateFirst.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        boolean first = true;

        Event[] eventsArray = eventManager.findAll_XML(Event[].class);
        for (int i = 0; i < eventsArray.length; i++) {
            if (eventsArray[i].getFecha().after(fechaConvertidaDesde)
                    && eventsArray[i].getFecha().before(fechaConvertidaHasta)) {
                events.add(eventsArray[i]);
            }
        }

        for (Event event : events) {
            if (first) {
                for (Club club : clubList) {
                    if (club.getId() == event.getClub().getId()) {
                        clubsFilter.add(club);
                    }
                }
            } else {
                for (Club club : clubList) {
                    if (club.getId() == event.getClub().getId()) {
                        for (Club club1 : clubsFilter) {
                            if (club1.getId() != club.getId()) {
                                clubsFilter.add(club);
                            }
                        }
                    }
                }
            }
        }
        return clubsFilter;
    }

    /**
     * Filtra clubes por una fecha específica.
     *
     * @param clubList lista de clubes a filtrar
     * @return lista de clubes que tienen eventos en la fecha seleccionada
     */
    private List<Club> setFirstDateFilter(List<Club> clubList) {
        EventManager eventManager = EventManagerFactory.get();
        List<Event> events = new ArrayList<>();
        List<Club> clubsFilter = new ArrayList<>();
        Date fechaConvertidaDesde = Date.from(dateFirst.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        boolean first = true;

        Event[] eventsArray = eventManager.findAll_XML(Event[].class);
        for (int i = 0; i < eventsArray.length; i++) {
            if (eventsArray[i].getFecha().equals(fechaConvertidaDesde)) {
                events.add(eventsArray[i]);
            }
        }

        for (Event event : events) {
            if (first) {
                for (Club club : clubList) {
                    if (club.getId() == event.getClub().getId()) {
                        clubsFilter.add(club);
                    }
                }
            } else {
                for (Club club : clubList) {
                    if (club.getId() == event.getClub().getId()) {
                        for (Club club1 : clubsFilter) {
                            if (club1.getId() != club.getId()) {
                                clubsFilter.add(club);
                            }
                        }
                    }
                }
            }
        }
        return clubsFilter;
    }

    /**
     * Genera un reporte impreso de los datos mostrados en la tabla.
     *
     * @param event evento que activa la impresión
     */
    private void imprimirTabla(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport("src/reports/ClubReport.jrxml");

            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Club>) this.tableClubs.getItems());

            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Alterna entre el tema claro y oscuro de la interfaz.
     *
     * @param event evento que activa el cambio de tema
     */
    private void cambiarTema(ActionEvent event) {
        if (tema) {
            Sesion.setTema(false);
        } else {
            Sesion.setTema(true);
        }
        changeTheme();
    }

    /**
     * Aplica el tema actual (claro u oscuro) a la interfaz.
     */
    private void changeTheme() {
        String currentStyle = anchorPane.getStyle();
        tema = Sesion.getTema();
        if (tema) {
            lblTitulo.setStyle("-fx-text-fill: black;");
            Sesion.setTema(tema);
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lblTitulo.setStyle("-fx-text-fill: white;");
            Sesion.setTema(tema);
            anchorPane.setStyle(currentStyle.replaceAll("-fx-background-image: [^;]+;", "-fx-background-image: url('/img/fondo.jpg');"));
        }
    }

    /**
     * Maneja la visualización del menú contextual al hacer clic derecho.
     *
     * @param event evento del mouse
     * @param menu menú contextual a mostrar
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

    /**
     * Maneja el cierre de la aplicación desde el botón X.
     *
     * @param event evento de cierre
     */
    private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    /**
     * Elimina los clubes seleccionados del sistema.
     *
     * @param event evento que activa la eliminación
     */
    private void deleteClub(ActionEvent event) {
        try {
            ObservableList<Club> obserbableClubs
                    = tableClubs.getSelectionModel().getSelectedItems();
            for (Club club : obserbableClubs) {
                clubManager.remove(club.getId().toString());
            }
            clubs = getClubsInfo();
            setTableData(clubs);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception deleting club.", e.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR EN EL PROCESO DE ELIMINACIÓN");
        }
    }

    /**
     * Muestra más información sobre el club seleccionado.
     *
     * @param event evento que activa la visualización
     */
    private void masInfo(ActionEvent event) {
        try {
            Club club = tableClubs.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showClubView.fxml"));

            Parent root = loader.load();

            ShowClubViewController controller = (ShowClubViewController) loader.getController();

            controller.setStage(stage);
            controller.setUser(user);
            controller.setClub(club);
            controller.initStage(root);
        } catch (Exception ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR EN EL PROCESO DE ELIMINACIÓN");
        }
    }

    /**
     * Crea un nuevo club en el sistema.
     *
     * @param event evento que activa la creación
     */
    private void createClub(ActionEvent event) {
        try {
            Club club = new Club();
            club.setCiudad("");
            club.setInstagram("");
            club.setNombre("");
            club.setUbicacion("");

            clubManager.create_XML(club);

            clubs.add(club);

            clubs = getClubsInfo();
            setTableData(clubs);

            if (tableClubs.getSelectionModel() != null && !tableClubs.getItems().isEmpty()) {
                int lastIndex = tableClubs.getItems().size() - 1;
                tableClubs.getSelectionModel().clearAndSelect(lastIndex);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception creating club", ex.getMessage());
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "ERROR CREANDO USUARIO");
        }
    }

    /**
     * Establece los datos en la tabla de clubes.
     *
     * @param clubes lista de clubes a mostrar
     */
    private void setTableData(List<Club> clubes) {
        try {
            ObservableList<Club> observableClubs = FXCollections.observableArrayList(clubes);
            if (observableClubs.size() > 0) {
                tableClubs.setItems(observableClubs);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception setting table data", ex.getMessage());
        }
    }

    /**
     * Obtiene la información de todos los clubes disponibles en el sistema.
     *
     * @return lista de clubes encontrados
     * @throws Exception si ocurre un error al obtener la información
     */
    private List<Club> getClubsInfo() throws Exception {
        try {
            Club[] clubsArray = clubManager.findAll_XML(Club[].class);
            List<Club> clubes = new ArrayList<>();
            for (int i = 0; i < clubsArray.length; i++) {
                clubes.add(clubsArray[i]);
            }
            return clubes;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception getting clubs info", ex.getMessage());
            throw new Exception("ERROR RECOGIENDO CLUBES");
        }
    }

}
