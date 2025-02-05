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
import model.Artist;
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
import ui_controllers.ShowAllArtistsViewController;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShowAllArtistViewControllerTest extends ApplicationTest{
    
     /**tabla de artistas*/
    private TableView<Artist> table;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllArtistsView.fxml"));
         
        Parent root = loader.load();
        ShowAllArtistsViewController controller = (ShowAllArtistsViewController) loader.getController();
        User user = new Admin();
        user = UserManagerFactory.get().find_XML(Admin.class, "1");
        Sesion.setUser(user);
        Sesion.setStage(stage);
        controller.initStage(root);
        verifyThat("#tvArtists", isVisible());
        table = lookup("#tvArtists").query();  
        
    }
    
     @After
    public void esperar(){
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            Logger.getLogger(ShowAlleventsViewControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * comprueba que el artista se crean bien en la tabla 
    */
    @Test
    public void test_A_CreateArtist(){     
        int rowCount=table.getItems().size();       
        clickOn("#btnCrear");
        assertEquals("Artista creado correctamente",rowCount+1,table.getItems().size());       
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
        write("DJPrueba");
        push(KeyCode.ENTER);
        assertEquals("Nombre Modificado","DJPrueba", table.getItems().get(rowCount - 1).getNombre());
        
        //modificar Tipo Musica
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 1).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("Hardcore");
        push(KeyCode.ENTER);
        assertEquals("Tipo Musica Modificado","Hardcore", table.getItems().get(rowCount - 1).getTipoMusica());
        
        //modificar la descripcion
        cell = lookup(".table-cell").nth((rowCount - 1) * table.getColumns().size() + 2).query();
        doubleClickOn(cell);        
        clickOn(cell);
        write("el mejor Dj de techno hardcore");
        push(KeyCode.ENTER);
        assertEquals("Descripcion Modificada","el mejor Dj de techno hardcore", table.getItems().get(rowCount - 1).getDescripcion());
              
    }
    /**
    * Selecciona un artista y va a la otra ventana
    */
    @Test
    public void test_C_seleccionarArtist(){
        int rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnSeleccionar");  
        verifyThat("#tableView", isVisible());
    }
    
    /**
    * Comprueba si el artista se elimina
    */
    @Test
    public void test_G_DeleteArtist(){
  
        int rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnEliminar");
  
        assertEquals("Evento borrado",rowCount-1,table.getItems().size()); 
        
    }
}
