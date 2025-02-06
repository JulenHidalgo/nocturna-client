/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import logic.UserManagerFactory;
import model.Admin;
import model.Club;
import model.Sesion;
import model.User;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import ui_controllers.ShowAllClubsViewController;

/**
 *  Hace los test para comprobar que la clase ShowAllClubsViewController
 * funciona correctamente
 * @author Adrian Rocha
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShowAllClubsViewControllerTest extends ApplicationTest{
    
    /**tabla de clubs*/
    private TableView<Club> table;
    
     @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllClubsView.fxml"));
         
        Parent root = loader.load();
        ShowAllClubsViewController controller = (ShowAllClubsViewController) loader.getController();
        User user = new Admin();
        user = UserManagerFactory.get().find_XML(Admin.class, "1");
        Sesion.setUser(user);
        Sesion.setStage(stage);
        controller.initStage(root);
        verifyThat("#tableClubs", isVisible());
        table = lookup("#tableClubs").query();  
        
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
    * comprueba si la tabla editable funciona bien
    */
    @Test
    public void test_B_UpdateEvent() {
        int rowCount = table.getItems().size();
        
        //modificar el nombre
        Node cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size()).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("Sonora");
        push(KeyCode.ENTER);
        assertEquals("Nombre Modificado","Sonora", table.getItems().get(rowCount - 1).getNombre());
        
        //modificar la ubicacion
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 1).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("Ribera de Axpe Etorbidea, 27");
        push(KeyCode.ENTER);
        assertEquals("Ubicacion Modificado","Ribera de Axpe Etorbidea, 27", table.getItems().get(rowCount - 1).getUbicacion());
        
        //modificar la ciudad
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 2).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("Erandio");
        push(KeyCode.ENTER);
        assertEquals("Ciudad Modificada","Erandio", table.getItems().get(rowCount - 1).getCiudad());
        
        //modificar Instagram
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 3).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("https://www.instagram.com/salasonorabilbao/");
        push(KeyCode.ENTER);
        assertEquals("Instagram Modificada","https://www.instagram.com/salasonorabilbao/", table.getItems().get(rowCount - 1).getInstagram());
        
        //Comprobar que el Link funciona
        clickOn(cell);
              
    }
    
    
    /**
    * comprueba que el Club se crean bien y lo añade en la tabla 
    */
    @Test
    public void test_A_CreateClub(){     
        int rowCount=table.getItems().size();       
        clickOn("#btnCreate");
        assertEquals("Club creado correctamente",rowCount+1,table.getItems().size());       
    }
    
    /**
    * Comprueba si el club se elimina
    */
    @Test
    public void test_G_DeleteClub(){
  
        int rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnDelete");
  
        assertEquals("Club borrado",rowCount-1,table.getItems().size()); 
        
    }
    
    
}
