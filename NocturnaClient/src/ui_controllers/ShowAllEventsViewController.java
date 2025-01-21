/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logic.ArtistManagerFactory;
import logic.ClubManagerFactory;

import logic.EventManager;
import logic.EventManagerFactory;
import model.Artist;
import model.Client;
import model.Club;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class ShowAllEventsViewController {
    
    @FXML
    AnchorPane bpPrincipal;
    
    @FXML
    TextField tfBuscador;
    
    @FXML
    TextField tfPrecio;
    
    @FXML
    DatePicker dateFecha;
    
    @FXML
    DatePicker dateFechaHasta;
    
    @FXML
    Label lbHasta;
    
    @FXML
    TableView<Event> tablaEvent;
    
    @FXML
    TableColumn<Event, String> tcNombre;
    
    @FXML
    TableColumn<Event, String> tcSala;
    
    @FXML
    TableColumn<Event, String> tcFecha;
    
    @FXML
    TableColumn<Event, Double> tcPrecio;
    
    @FXML
    TableColumn<Event, Integer> tcNumEntradas;
    
    @FXML
    TableColumn<Event, String> tcMusica;
    
    @FXML
    TableColumn<Event, Integer> tcConsumicion;
    
    @FXML
    Button btnCrearEvento;
    
    @FXML
    Button btnBorrarEvento;
    
    @FXML
    Button btnAñadirArtistas;
    
    @FXML
    Button btnSeleccionar;
    
 

    
    private Stage stage;

    private User user;

    private boolean tema;
    
    private List<Event> events = recogerAllEvents();

    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    @FXML
    private void deleteEvent(ActionEvent event) {
        Event selectedEvent = tablaEvent.getSelectionModel().getSelectedItem();
        EventManagerFactory.get().remove(selectedEvent.getId().toString());
        cargarTabla();
    }
    
    
    private void cargarTabla(){
        initializeTableColumns();        
        // Convertir ArrayList a ObservableList
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);       
        // Cargar datos en la tabla
        tablaEvent.setItems(observableEvents);
        
    }
    
    private void initializeTableColumns() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcSala.setCellValueFactory(new PropertyValueFactory<>("club"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precioEntrada"));
        tcNumEntradas.setCellValueFactory(new PropertyValueFactory<>("NumEntradas"));
        tcMusica.setCellValueFactory(new PropertyValueFactory<>("Musica"));
        tcConsumicion.setCellValueFactory(new PropertyValueFactory<>("Consumicion"));
       
       tcMusica.setCellValueFactory(cellData -> {
            Artist[] artista = ArtistManagerFactory.get().findByEvent_XML(Artist[].class, cellData.getValue().getId().toString());
            List<Artist> artistas = Arrays.asList(artista);
            String musicas = artistas.stream().map(Artist::getTipoMusica).collect(Collectors.joining(", "));
            if(musicas.isEmpty())
                return null;
            return new SimpleStringProperty(musicas);
        });
      
        //Poner que la columna Sala sea un combobox y cargarle los datos
        List<String> nombresClubs = recogerAllClubs().stream().map(Club::getNombre).collect(Collectors.toList());
        ObservableList<String> clubs = FXCollections.observableArrayList(nombresClubs);
        //tcSala.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(nombresClubs)));
        tcSala.setCellFactory(ChoiceBoxTableCell.forTableColumn(clubs));
        
    }
    
    private void irShowEventView(){
        
    }
    
    private List<Club> recogerAllClubs(){
        Club[] clubArray = ClubManagerFactory.get().findAll_XML(Club[].class);
        List<Club> listClubs = Arrays.asList(clubArray);   
        return listClubs;
                
    }
    
     private List<Event> recogerAllEvents(){ 
      Event[] eventsArray = EventManagerFactory.get().findByDate_XML(Event[].class, LocalDate.now().toString());
      List<Event> events = Arrays.asList(eventsArray);
      return  events;
    }
     
     
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }
    
    public void initStage(Parent root) {

        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);

        cargarTabla();
        
        if(user instanceof Client){
            btnAñadirArtistas.setVisible(false);
            btnBorrarEvento.setVisible(false);
            btnCrearEvento.setVisible(false);
        }else{
            tablaEvent.setEditable(true);
            tcMusica.setEditable(false);
        }
        
        tablaEvent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                if (newValue != null) {
                    btnAñadirArtistas.setDisable(false);
                    btnBorrarEvento.setDisable(false);
                    btnCrearEvento.setDisable(false);
                    btnSeleccionar.setDisable(false);
                }else{
                    btnAñadirArtistas.setDisable(true);
                    btnBorrarEvento.setDisable(true);
                    btnCrearEvento.setDisable(true);
                    btnSeleccionar.setDisable(true);
                }
            }
        });
        
        dateFecha.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    if(dateFecha.getValue() != null){
                        lbHasta.setVisible(true);
                        dateFechaHasta.setVisible(true);
                        Event[] eventsArray = EventManagerFactory.get().findByDate_XML(Event[].class, dateFecha.getValue().toString());
                        events = Arrays.asList(eventsArray);
                        cargarTabla();
                    }
                }
            }
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
   
        dateFechaHasta.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    if (dateFecha.getValue() != null) {
                        Event[] eventsArray = EventManagerFactory.get().findByDates_XML(Event[].class, dateFecha.getValue().toString(),dateFechaHasta.getValue().toString());
                        events = Arrays.asList(eventsArray);
                        cargarTabla();
                    }
                }
            }
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
                  
       tcPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tcPrecio.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Event, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Event, Double> eventCell) {
                try {
                    Event item = eventCell.getRowValue();
                    Double newValue = eventCell.getNewValue();
                    if (newValue == null) {
                        throw new Exception();
                    }
                    item.setPrecioEntrada(newValue);
                    Artist[] artista = ArtistManagerFactory.get().findByEvent_XML(Artist[].class, item.getId().toString());
            List<Artist> artistas = Arrays.asList(artista);
            Set<Artist> conjunto = new HashSet<>(artistas);
            item.setArtists(conjunto);
                    EventManagerFactory.get().edit_XML(item, item.getId().toString());
                    events = recogerAllEvents();
                    cargarTabla();
                } catch (Exception ex) {
                    Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
  
        
        tcNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        tcNombre.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Event, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Event, String> eventCell) {
                 try {
                    Event item = eventCell.getRowValue();
                    String newValue = eventCell.getNewValue();
                    if (newValue == null || newValue.isEmpty()) {
                        throw new Exception();
                    }
                    item.setNombre(newValue);
                    EventManagerFactory.get().edit_XML(item, item.getId().toString());
                    events = recogerAllEvents();
                    cargarTabla();
                    
                } catch (Exception ex) {
                    Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        tcSala.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Event, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Event, String> eventCell) {
                Event item = eventCell.getRowValue();
                //recoger el club del nuevo valor
                Club club = recogerAllClubs().stream().filter(c -> c.getNombre().equals(eventCell.getNewValue())).findFirst().orElse(null);
                item.setClub(club);
                EventManagerFactory.get().edit_XML(item, item.getId().toString());
                cargarTabla();
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        //Filrar los eventos por lo que escriba en el buscador
        tfBuscador.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //onTextFieldChange(newValue); 
                List<Event> eventos = new ArrayList<>();
                for (Event e : events) {
                    if (e.getNombre().toLowerCase().equalsIgnoreCase(newValue.toLowerCase()) || e.getNombre().toLowerCase().startsWith(newValue.toLowerCase())) {
                        eventos.add(e);
                    }
                }
                events=eventos;
                cargarTabla();
            }
        });
        
        //Filtrar los eventos que tengan el precio que ha introducido
        tfPrecio.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //onTextFieldChange(newValue); 
                List<Event> eventos = new ArrayList<>();
                Double precio = Double.parseDouble(newValue);
                for (Event e : events) {
                    if (e.getPrecioEntrada()>=precio || e.getPrecioEntrada()<precio+1) {
                        eventos.add(e);
                    }
                }
                events=eventos;
                cargarTabla();
            }
        });

        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");

    }

}
