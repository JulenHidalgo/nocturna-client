package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import control.Main;
import control.Sesion;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Artist;
import model.Client;
import model.Event;
import model.User;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShowAlleventsViewControllerTest  extends ApplicationTest{
    /**
     * tabla de eventos
     */
    private TableView<Event> table;
    
    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage);
        /**
         * clickOn("#tfMail");
         * write("");
         * clickOn("#pfPass");
         * write("");   
         */
        table = lookup("#tablaEvent").query();      
    }
    
    /**
     * test para comprobar que tipo de usuario es 
     */
    @Test
    public void test_A_User() {
        User user = Sesion.getUser();
        if (user.getIsAdmin()) {
            verifyThat("#btnAñadirArtistas", isVisible());
            verifyThat("#btnBorrarEvento", isVisible());
            verifyThat("#btnCrearEvento", isVisible());
        } else {
            verifyThat("#btnAñadirArtistas", isInvisible());
            verifyThat("#btnBorrarEvento", isInvisible());
            verifyThat("#btnCrearEvento", isInvisible());
        }
    }
     
    /**
    * comprueba que los evento se crean bien en la tabla 
    */
    @Test
    public void test_B_CreateEvent(){
        int rowCount=table.getItems().size();       
        clickOn("#btnCrearEvento");
        assertEquals("Evento creado correctamente",rowCount+1,table.getItems().size());       
    }
    
    /**
    * comprueba si la tabla editable funciona bien
    */
    @Test
    public void test_C_UpdateEvent() {
        
        int rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        
        //modificar el nombre
        Node cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size()).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("Viernes de Templo");
        push(KeyCode.ENTER);
        assertEquals("Nombre Modificado","Viernes de Templo", table.getItems().get(rowCount - 1).getNombre());
        
        // Abrir el ChoiceBox y seleccionar una opción
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 1).query();
        doubleClickOn(cell);
        clickOn(cell);
        Node choiceBox = lookup(".choice-box").query();
        clickOn(choiceBox);
        clickOn("moma"); 
        push(KeyCode.ENTER);
        assertEquals("choiceBox modificado","moma", table.getItems().get(rowCount - 1).getClub().getNombre());

        // Modificar la fecha y poner la de mañana
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String formattedDate = tomorrow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 2).query();
        doubleClickOn(cell);
        clickOn(cell);
        Node datePicker = lookup(".date-picker").query();
        clickOn(datePicker);
        eraseText(10);
        write(formattedDate);
        push(KeyCode.ENTER);
        assertEquals("Fecha modificada", valueOf(tomorrow), table.getItems().get(rowCount - 1).getFecha());
        
        //modificar el precio
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size()+3).query();
        doubleClickOn(cell);
        clickOn(cell);
        eraseText(4);
        write("18.5");
        push(KeyCode.ENTER);
        assertEquals("Precio modificado", 18.5, table.getItems().get(rowCount - 1).getPrecioEntrada(), 0.00);
        
        //modificar el numero de entradas
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size()+4).query();
        doubleClickOn(cell);
        clickOn(cell);
        eraseText(1);
        write("200");
        push(KeyCode.ENTER);
        assertEquals("Numero de entradas modificado", 200, table.getItems().get(rowCount - 1).getNumEntradas());
        
        //modificar la cantidad de consumiciones
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size()+6).query();
        doubleClickOn(cell);
        clickOn(cell);
        eraseText(1);
        write("2");
        push(KeyCode.ENTER);
        assertEquals("Consumiciones modificado", 2, table.getItems().get(rowCount - 1).getConsumicion());         
    }
    
    /**
    * test añadir artista a un evento
    */
    @Test
    public void test_D_AñadirArtist(){    
        int rowCountE = table.getItems().size();
        Node rowE = lookup(".table-row-cell").nth(rowCountE - 1).query();
        clickOn(rowE);
        clickOn("#btnAñadirArtistas");  
        TableView<Artist> tvArtists = lookup("#tvArtists").query();
        int rowCount = tvArtists.getItems().size();
        int columnCount = tvArtists.getColumns().size();
        if (rowCount != 0) {
            Node cell = lookup(".table-cell").nth((columnCount - 1)).query();
            doubleClickOn(cell);
            clickOn(cell);
            Node checkBox = lookup(".check-box").query();
            clickOn(checkBox);
            clickOn("#btnSeleccionar");
            clickOn("Aceptar");
            //comprobar que le tipoMusica a cambiado y ya no esta vacio
            assertNotEquals("El Artista ya no es nulo", "No hay artistas", table.getItems().get(rowCount - 1 * table.getColumns().size()+5).getNombre());
        }
    }
    
    /**
    * comprueba si la tabla editable funciona bien
    */
    @Test
    public void test_F_DeleteEvent(){
        
        int rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnBorrarEvento");
        clickOn("No");
        clickOn("Aceptar");
        assertEquals("Evento No borrado",rowCount,table.getItems().size());
        
        
        row= lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnBorrarEvento");
        clickOn("Sí");
        clickOn("Aceptar");
        assertEquals("Evento borrado",rowCount-1,table.getItems().size()); 
        
    }
   
}
