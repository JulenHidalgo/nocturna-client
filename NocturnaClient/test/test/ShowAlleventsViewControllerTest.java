package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import model.Sesion;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import logic.UserManagerFactory;
import model.Artist;
import model.Event;
import model.User;
import org.junit.After;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ui_controllers.ShowAllEventsViewController;
import model.Admin;

/**
 * Hace los test para comprobar que la clase ShowAlleventsViewController
 * funciona correctamente
 * @author Erlantz Rey
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShowAlleventsViewControllerTest  extends ApplicationTest{
    /**
     * tabla de eventos
     */
    private TableView<Event> table;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));
         
        Parent root = loader.load();
        ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();
        User user = new Admin();
        user = UserManagerFactory.get().find_XML(Admin.class, "1");
        Sesion.setUser(user);
        Sesion.setStage(stage);
        controller.initStage(root);
        verifyThat("#tablaEvent", isVisible());
        table = lookup("#tablaEvent").query();            
    }
    
    /**
     * esperar un poco despues de cada test para ver bien los resultados
     */
    @After
    public void esperar(){
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            Logger.getLogger(ShowAlleventsViewControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    /**
    * comprueba que los evento se crean bien en la tabla 
    */
    @Test
    public void test_A_CreateEvent(){     
        int rowCount=table.getItems().size();       
        clickOn("#btnCrearEvento");
        assertEquals("Evento creado correctamente",rowCount+1,table.getItems().size());       
    }
    
    /**
    * comprueba si la tabla editable funciona bien
    */
    @Test
    public void test_B_UpdateEvent() {

        int rowCount = table.getItems().size();
        
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
    public void test_C_AñadirArtist(){    
        /**selecciona el ultimo de la tabla que es el creado antes*/
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
    * comprueba si el filtrado posterior a una fecha funcione bien
    */
    @Test
    public void test_D_FindByDate(){     

        /**Comprobar que desparece la fila por que no es posterior a la fecha introducida*/
        int rowCount = table.getItems().size();
        clickOn("#dateFecha");
        LocalDate fecha = LocalDate.now().plusDays(2);
        String formattedDate = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        write(formattedDate);
        push(KeyCode.ENTER);
        
         // Verificar que solo los elementos posteriores a la fecha permanecen
        for (Event item : table.getItems()) {
            assertTrue(item.getFecha().after(valueOf(fecha)) || item.getFecha().equals(fecha));
        }

    }
    
    /**
    * comprueba si el filtrado entre fechas funciona bien
    */
    @Test
    public void test_F_FindByDates(){   

        int rowCount = table.getItems().size();
        
        /**Ponemos la fecha de hoy y la de dentro de dos dias para ver que salga el evento que tiene la fecha de malana*/   
        LocalDate fechaHoy = LocalDate.now();
        String formatDate = fechaHoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        clickOn("#dateFecha");
        write(formatDate);
        push(KeyCode.ENTER);
        
        clickOn("#dateFechaHasta");
        LocalDate fechaHasta = LocalDate.now().plusDays(3);
        formatDate = fechaHasta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        write(formatDate);
        push(KeyCode.ENTER);
        
        rowCount = table.getItems().size();
      
         for (Event item : table.getItems()) {
            assertTrue((item.getFecha().after(valueOf(fechaHoy)) || item.getFecha().equals(valueOf(fechaHoy)))
                            && (item.getFecha().before(valueOf(fechaHasta)) || item.getFecha().equals(valueOf(fechaHoy))));
        }
        //assertEquals("Despues de una fecha", rowCount, 1);
        
    }
    
    /**
    * comprueba si la tabla editable funciona bien
    */
    @Test
    public void test_G_DeleteEvent(){
  
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
