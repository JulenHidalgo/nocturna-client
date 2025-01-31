package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import control.Main;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Event;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShowAlleventsViewControllerTest  extends ApplicationTest{
    
    private TableView<Event> table;
    
    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage);
        table = lookup("#tablaEvent").query(); 
        verifyThat("#btnAñadirArtistas",  isVisible());
        verifyThat("#btnBorrarEvento",  isVisible());
        verifyThat("#btnCrearEvento",  isVisible());
    }
    
   /** @Test
    public void testUserAdmin(){
        
        clickOn("#tfMail");
        write("carlos@gamil.com");
        
        clickOn("#pfPass");
        write("Password123");
        
        clickOn("#btnSignIn");
        
        verifyThat("#btnAñadirArtistas",  isVisible());
        verifyThat("#btnBorrarEvento",  isVisible());
        verifyThat("#btnCrearEvento",  isVisible());
    }*/
    
    /**
     * comprueba que los evento se crean bien en la tabla 
     */
    @Test
    public void test_A_CreateEvent(){
        int rowCount=table.getItems().size();
        
        clickOn("#btnCrearEvento");
        assertEquals("Evento creado correctamente",rowCount+1,table.getItems().size());
        
    }
    
    /** @Test
    public void test_B_AñadirArtist(){
        int rowCount=table.getItems().size();
        
        clickOn("#btnAñadirArtistas");
        
        
    }*/
    
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
        
    }
    
     /**
     * comprueba si la tabla editable funciona bien
     */
    @Test
    public void test_D_DeleteEvent(){
        
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
    
    /**public void testUserClient(){
        
        clickOn("#hbSignUp");
        
        clickOn("#tfNombre");
        write("Carlos");

        clickOn("#tfApellido");
        write("Perez");

        clickOn("#tfTelefono");
        write("123456789");

        clickOn("#dpFechaNac");
        write("2000-01-01");

        clickOn("#tfCiudad");
        write("Madrid");

        clickOn("#tfDni");
        write("12345678A");

        clickOn("#tfMail");
        write("carlos@gamil.com");

        clickOn("#pfPass");
        write("Password123");

        clickOn("#pfPass2");
        write("Password123");
        
        clickOn("#btnSignUp");
        
        verifyThat("#btnAñadirArtistas",  isInvisible());
        verifyThat("#btnBorrarEvento",  isInvisible());
        verifyThat("#btnCrearEvento",  isInvisible());
    }
    */
}
