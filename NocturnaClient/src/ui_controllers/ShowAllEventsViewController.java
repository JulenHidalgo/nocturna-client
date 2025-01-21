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
import java.util.List;
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

    private final Logger LOGGER = Logger.getLogger("crudbankjfxclient.view");
    
    @FXML
    private void deleteEvent(ActionEvent event) {
        Event selectedEvent = tablaEvent.getSelectionModel().getSelectedItem();
        EventManagerFactory.get().remove(selectedEvent.getId().toString());
        cargarTabla(recogerAllEvents());
    }
    
    
    private void cargarTabla(List<Event> tableEvents){
        initializeTableColumns();        
        // Convertir ArrayList a ObservableList
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(tableEvents);       
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
            return new SimpleStringProperty(musicas);
        });
        
     
        //
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
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }
    
    private List<Event> recogerAllEvents(){ 
      Event[] eventsArray = EventManagerFactory.get().findByDate_XML(Event[].class, LocalDate.now().toString());
      List<Event> events = Arrays.asList(eventsArray);
      return  events;
    }
    
    public void initStage(Parent root) {

        LOGGER.info("Initializing Bank Statement window.");
        Scene scene = new Scene(root);

        cargarTabla(recogerAllEvents());
        
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
                        List<Event> events = Arrays.asList(eventsArray);
                        cargarTabla(events);
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
                    EventManagerFactory.get().edit_XML(item, item.getId().toString());

                    cargarTabla(recogerAllEvents());
                    throw new UnsupportedOperationException("Not supported yet.");
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
                cargarTabla(recogerAllEvents());
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        tcSala.setOnEditCommit(event -> {
            // Recuperamos el evento (fila) que fue editado
            Event selectedEvent = event.getRowValue();
            Event evento = EventManagerFactory.get().find_XML(Event.class, selectedEvent.getId().toString());
            Club club = recogerAllClubs().stream().filter(c -> c.getNombre().equals(event.getNewValue())).findFirst().orElse(null);
            evento.setClub(club);
            EventManagerFactory.get().edit_XML(evento, selectedEvent.getId().toString());
            cargarTabla(recogerAllEvents());
            
          }); 
           /**tfBuscador.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    //onTextFieldChange(newValue); 
                    for(Event e : recogerAllEvents()){
                        if(tfBuscador.setText(newValue))
                    }
                } 
            });*/
        
        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("Bank Statement window initialized.");

    }

}
