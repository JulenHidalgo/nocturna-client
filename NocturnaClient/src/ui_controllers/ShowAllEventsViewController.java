/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import utils.EventEditingCell;
import model.Sesion;
import static model.Sesion.getTema;
import static model.Sesion.setTema;
import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import java.io.IOException;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logic.ArtistManagerFactory;
import logic.ClubManagerFactory;

import logic.EventManager;
import logic.EventManagerFactory;
import logic.UserManagerFactory;
import ui_controllers.ShowEventViewController;
import model.Artist;
import model.Client;
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
import utils.CustomAlert;

/**
 * Controlador para la vista de mostrar todos los eventos.
 * Permite gestionar eventos, podiendo crear, modificar, 
 * añadir artistas y eliminar.
 *  @author Erlantz
 */
public class ShowAllEventsViewController {
    
    /**Panel principal de la ventana*/
    @FXML
    AnchorPane bpPrincipal;
    
    /**Campo para filtrar eventos por nombre*/
    @FXML
    TextField tfBuscador;
    
    /**Campo para filtrar eventos por precio*/
    @FXML
    TextField tfPrecio;
    
    /**Campo para filtrar eventos posteriores a una fecha*/
    @FXML
    DatePicker dateFecha;
    
    /**Campo para filtrar eventos entre dos fechas*/
    @FXML
    DatePicker dateFechaHasta;
    
    /**Testo de informacion*/
    @FXML
    Label lbHasta;
    
    /**Testo de informacion del filtro buscar*/
    @FXML
    Label txBuscar;
    
    /**Testo de informacion del filtro precio*/
    @FXML
    Label txPrecio;
    
    /**Testo de informacion del filtro fecha*/
    @FXML
    Label txFecha;
    
    /**Tabla de eventos*/
    @FXML
    TableView<Event> tablaEvent;
    
    /**Columna con el nombre del evento*/
    @FXML
    TableColumn<Event, String> tcNombre;
    
    /**Columna con el nombre del club realzionado con el evento*/
    @FXML
    TableColumn<Event, String> tcSala;
    
    /**Columna con la fecha del evento*/
    @FXML
    TableColumn<Event, String> tcFecha;
    
    /**Columna con el precio del evento*/
    @FXML
    TableColumn<Event, Double> tcPrecio;
    
    /**Columna con el numero de entradas que quedan*/
    @FXML
    TableColumn<Event, Integer> tcNumEntradas;
    
    /**Columna con el tipo musica que hacen los artistas que actuan en ese evento*/
    @FXML
    TableColumn<Event, String> tcMusica;
    
    /**Columna con las consumiciones que incluye el evento*/
    @FXML
    TableColumn<Event, Integer> tcConsumicion;
    
    /**boton para crear eventos*/
    @FXML
    Button btnCrearEvento;
    
    /**boton para borrar uno o mas eventos*/
    @FXML
    Button btnBorrarEvento;
    
    /**boton para añadir artistas a un evento*/
    @FXML
    Button btnAñadirArtistas;
    
    /**boton ver un solo evento seleccionado*/
    @FXML
    Button btnSeleccionar;
    
    /**la escena*/
    private Stage stage;

    /**usuario que inia sesion*/
    private User user;
    
    /**cambia dependiendo de que tema(fondo) este puesto*/
    private boolean tema;
    
    /**lista de que recoge todos los eventos*/
    private List<Event> events = recogerAllEvents();
    
    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");

    /**
     * Elimina el evento o los eventos seleccionados pidiendo al usuario que confirme
     * @param event
     */
    @FXML
    private void deleteEvent(ActionEvent event) {
        try {
            //poner alerta con opciones para que tenga que confirmar para borrar el evento o eventos
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Seguro que quieres borrar el evento ", ButtonType.YES, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    ObservableList<Event> selectedEvent = tablaEvent.getSelectionModel().getSelectedItems();
                    selectedEvent.forEach(item -> {
                        try {
                            EventManagerFactory.get().remove(item.getId().toString());
                        } catch (InternalServerErrorException ex) {
                            LOGGER.severe("An error occurred while deleting an artist");
                            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error en el servidor");
                        }

                    });
                    cargarTabla(recogerAllEvents());
                    new Alert(Alert.AlertType.INFORMATION, "Evento borrado", ButtonType.OK).showAndWait();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "El evento se mantiene", ButtonType.OK).showAndWait();
                }
            });
            
        } catch (Exception ex) {
            LOGGER.severe("An error occurred");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error");
        }
    }
    
    /**
    * metodo para crear nuevos eventos y añadirlo a la tabla
    * @param event 
    */
    @FXML
    private void createEvent(ActionEvent event) {
        try {
            Event evento = new Event();
             EventManagerFactory.get().create_XML(evento);
            events = recogerAllEvents();
            cargarTabla(events);
            int lastIndex = tablaEvent.getItems().size() - 1;
            tablaEvent.getSelectionModel().clearAndSelect(lastIndex);
        }catch (InternalServerErrorException ex) {
            LOGGER.severe("The server throwed an InternalServerErrorException");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("The server throwed an Exception");
            CustomAlert.throwAlertCustom(Alert.AlertType.ERROR, "Ha sucedido un error, intentalo de nuevo más tarde");
        }
    }
    
    /**
    * ir a la ventana showAllArtistsView para hacer la relacion de vento y artista
    * @param event 
    */
    @FXML
    private void añadirAtists(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllArtistsView.fxml"));
            Parent root = loader.load();

            ShowAllArtistsViewController controller = (ShowAllArtistsViewController) loader.getController();
            controller.setEvent(tablaEvent.getSelectionModel().getSelectedItem());
            controller.initStage(root);

        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * ir a la ventana showEventView con el evento seleccionado
     * @param event 
     */
    @FXML
    private void irShowEventView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showEventView.fxml"));

            Parent root = loader.load();

            ShowEventViewController controller = (ShowEventViewController) loader.getController();

            controller.setEvent(tablaEvent.getSelectionModel().getSelectedItem());

            controller.initStage(root);

        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Carga los datos de la tabla con los eventos
     * @param eventTable 
     */
    private void cargarTabla(List<Event> eventTable) {
        initializeTableColumns();
        // Convertir ArrayList a ObservableList
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(eventTable);
        // Cargar datos en la tabla
        tablaEvent.setItems(observableEvents);

    }
    
    /**
     * inicializa las columnas de la tabla 
     */
    private void initializeTableColumns() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcSala.setCellValueFactory(new PropertyValueFactory<>("club"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precioEntrada"));
        tcNumEntradas.setCellValueFactory(new PropertyValueFactory<>("NumEntradas"));
        tcMusica.setCellValueFactory(new PropertyValueFactory<>("Musica"));
        tcConsumicion.setCellValueFactory(new PropertyValueFactory<>("Consumicion"));

        tcMusica.setCellValueFactory(cellData -> {
            try {
                Artist[] artista = ArtistManagerFactory.get().findByEvent_XML(Artist[].class, cellData.getValue().getId().toString());
                List<Artist> artistas = Arrays.asList(artista);
                String musicas = artistas.stream().map(Artist::getTipoMusica).collect(Collectors.joining(", "));
                if (musicas.isEmpty()) {
                    return new SimpleStringProperty("No hay artistas");
                }
                return new SimpleStringProperty(musicas);
            } catch (ReadException ex) {
                Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InternalServerErrorException ex) {
                Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        });

        //Poner que la columna Sala sea un combobox y cargarle los datos
        List<String> nombresClubs = recogerAllClubs().stream().map(Club::getNombre).collect(Collectors.toList());
        ObservableList<String> clubs = FXCollections.observableArrayList(nombresClubs);
        tcSala.setCellFactory(ChoiceBoxTableCell.forTableColumn(clubs));

    }
    
    /**
     * pone diferentes filtros a los eventos y carga la tabla con los eventos filtrados
     */
    private void aplicarFiltros() {
        events = recogerAllEvents();
        List<Event> eventos = new ArrayList<>();
        
        /**Filtra los eventos por su nombre*/
        if (!tfBuscador.getText().isEmpty()) {
            events = events.stream().filter(event -> event.getNombre().toLowerCase().startsWith(tfBuscador.getText().toLowerCase())).collect(Collectors.toList());
        }
            /**Filtra los eventos a partir de una fecha */
        if (dateFechaHasta.getValue() != null) {
            
            Event[] eventosArray = EventManagerFactory.get().findByDates_XML(Event[].class, dateFecha.getValue().toString(), dateFechaHasta.getValue().toString());
            for (Event e : Arrays.asList(eventosArray)) {
                if (events.contains(e)) {
                    eventos.add(e);
                }
            }
            events = eventos;
         /**Filtra los eventos entre dos fechas */
        } else if (dateFecha.getValue() != null) {
            dateFechaHasta.setVisible(true);
            lbHasta.setVisible(true);
            Event[] eventsArray = EventManagerFactory.get().findByDate_XML(Event[].class, dateFecha.getValue().toString());
            for (Event e : Arrays.asList(eventsArray)) {
                if (events.contains(e)) {
                    eventos.add(e);
                }
        }
            events = eventos;
        }else{
             dateFechaHasta.setVisible(false);
            lbHasta.setVisible(false);
        }
        
        /**Filtra los eventos que tengan el mismo precio */
        if (!tfPrecio.getText().isEmpty()) {
            events = events.stream()
                    .filter(event -> event.getPrecioEntrada() >= Double.valueOf(tfPrecio.getText()) && event.getPrecioEntrada() < Double.valueOf(tfPrecio.getText()) + 1)
                    .collect(Collectors.toList());

        }

        cargarTabla(events);
    }
    
    /**
     * devuelve una lista de todos los clubs
     * @return List<Club>
     */
    private List<Club> recogerAllClubs() {
        Club[] clubArray = ClubManagerFactory.get().findAll_XML(Club[].class);
        List<Club> listClubs = Arrays.asList(clubArray);
        return listClubs;

    }
    
    /**
     * devuelve una lista de todos los eventos que su fecha sea posterior a la de hoy
     * @return List<Event>  
     */
    private List<Event> recogerAllEvents() {
        Event[] eventsArray = EventManagerFactory.get().findByDate_XML(Event[].class, LocalDate.now().toString());
        List<Event> events = Arrays.asList(eventsArray);
        return events;
    }
    
    /**
     * cambia el boolean de tema y llama al metodo changeTheme() para cambiar el fondo
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
     * Cambia el estilo del fondno y los textos
     */
    private void changeTheme() {
        String currentStyle = bpPrincipal.getStyle();

        if (tema) {
            lbHasta.setStyle("-fx-text-fill: black;");
            txBuscar.setStyle("-fx-text-fill: black;");
            txPrecio.setStyle("-fx-text-fill: black;");
            txFecha.setStyle("-fx-text-fill: black;");
            bpPrincipal.setStyle(currentStyle.replaceAll("-fx-background-image: url\\('[^']+'\\);", "-fx-background-image: url('/img/fondogris.jpg');"));
        } else {
            lbHasta.setStyle("-fx-text-fill: white;");
            txBuscar.setStyle("-fx-text-fill: white;");
            txPrecio.setStyle("-fx-text-fill: white;");
            txFecha.setStyle("-fx-text-fill: white;");
            
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
        //Se comprueba si se hace clic con el borón derecho del ratón.
        if (event.getButton() == MouseButton.SECONDARY) {
            //Si es así se abre el menú contextual.
            menu.show(bpPrincipal, event.getScreenX(), event.getScreenY());
        } else {
            //Si no, se cierra el mismo.
            menu.hide();
        }
    }

    /**
    * cuando el usuario le da a la  "X" pide confirmacion y sale de la aplicacion
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
    * Genera e imprime un report con los datos de la tabla de eventos
    * @param event
    */
    private void imprimirTabla(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport("src/reports/EventReport.jrxml");

            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Event>) this.tablaEvent.getItems());

            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(ShowAllClubsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * Inicializa la ventana segun el usuario.
    * 
    * @param root 
    */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Initializing Bank Statement window.");
            Scene scene = new Scene(root);
            user = Sesion.getUser();
            tema = Sesion.getTema();
            stage = Sesion.getStage();

            cargarTabla(events);

            ContextMenu contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("Cambiar tema");
            item1.setOnAction(this::cambiarTema);
            MenuItem item2 = new MenuItem("Imprimir los datos de la tabla");
            item2.setOnAction(this::imprimirTabla);
            contextMenu.getItems().addAll(item1, item2);

            bpPrincipal.setOnMouseClicked(event -> controlMenuConceptual(event, contextMenu));

            if (!user.getIsAdmin()) {
                btnAñadirArtistas.setVisible(false);
                btnBorrarEvento.setVisible(false);
                btnCrearEvento.setVisible(false);
            } else {
                tablaEvent.setEditable(true);
                tablaEvent.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tcMusica.setEditable(false);
            }

            tablaEvent.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Event>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Event> e) {
                    if (e.getList().isEmpty()) {
                        btnAñadirArtistas.setDisable(true);
                        btnBorrarEvento.setDisable(true);
                        btnSeleccionar.setDisable(true);
                    } else if (e.getList().size() == 1) {
                        btnAñadirArtistas.setDisable(false);
                        btnBorrarEvento.setDisable(false);
                        btnSeleccionar.setDisable(false);
                    } else if (e.getList().size() > 1) {
                        btnAñadirArtistas.setDisable(true);
                        btnSeleccionar.setDisable(true);
                    }
                }
            });
            
            
            dateFecha.valueProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });


            dateFecha.setDayCellFactory(picker -> {
                return new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        // Establecer como no seleccionable cualquier fecha posterior a hoy
                        setDisable(item.isBefore(LocalDate.now()));
                    }
                };
            });
            
            dateFechaHasta.valueProperty().addListener((observable, oldValue, newValue) -> {  
                    aplicarFiltros();              
               
            });

            dateFechaHasta.setDayCellFactory(picker -> {
                return new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        //Establecer como no seleccionable fechas anteriores a la otra fehca
                        setDisable(item.isBefore(dateFecha.getValue()));
                    }
                };
            });

            //hacer la modificacion de las columnas
            tcPrecio.setCellFactory(column -> new EventEditingCell());
            tcConsumicion.setCellFactory(column -> new EventEditingCell());
            tcNumEntradas.setCellFactory(column -> new EventEditingCell());
            tcNombre.setCellFactory(column -> new EventEditingCell());
            tcSala.setCellFactory(column -> new EventEditingCell());
            tcFecha.setCellFactory(column -> new EventEditingCell());

            //Filrar los eventos por lo que escriba en el buscador
            tfBuscador.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    //onTextFieldChange(newValue); 
                    aplicarFiltros();
                }
            });

            //Filtrar los eventos que tengan el precio que ha introducido
            tfPrecio.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    //onTextFieldChange(newValue); 
                    aplicarFiltros();
                }
            });
            
            changeTheme();
            stage.show();
            stage.setScene(scene);
            LOGGER.info("Bank Statement window initialized.");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception setting up de window.", e.getMessage());
            try {
                throw new Exception("ERROR INICIANDO LA VENTANA");
            } catch (Exception ex) {
                Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
