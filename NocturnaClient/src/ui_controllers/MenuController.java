/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import control.Sesion;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import logic.UserManagerFactory;
import model.Artist;
import model.Client;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class MenuController {

    @FXML
    MenuBar menuBar;

    @FXML
    Menu menuVer;

    @FXML
    MenuItem miTickets;

    @FXML
    MenuItem miEvents;

    @FXML
    MenuItem miClubs;

    @FXML
    MenuItem miArtists;

    @FXML
    Menu menuPerfil;

    @FXML
    MenuItem miPerfil;

    @FXML
    MenuItem miSignOut;

    @FXML
    Menu menuHelp;

    @FXML
    MenuItem miAbout;

    private Stage stage;

    private User user;

    private Event event;

    private boolean tema;

    private final Logger LOGGER = Logger.getLogger("SignInViewController.view");
    
    
    @FXML
    private void irShowAllEventsView(ActionEvent event) throws Exception{
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));
            
            Parent root = loader.load();
            
            ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();
           
           
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void irShowAllClubsView(ActionEvent event) throws Exception{
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllClubsView.fxml"));
            
            Parent root = loader.load();
            
            ShowAllClubsViewController controller = (ShowAllClubsViewController) loader.getController();
    
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    private void irShowAllArtistView(ActionEvent event) {
         try {
            stage=Sesion.getStage();
            tema=Sesion.getTema();
            user=Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllArtistsView.fxml"));
            
            Parent root = loader.load();
            
            ShowAllArtistsViewController controller = (ShowAllArtistsViewController) loader.getController();
                 
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            controller.setEvent(null);
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void irShowAllTicketsView(ActionEvent event) throws Exception{
         try {
            stage=Sesion.getStage();
            tema=Sesion.getTema();
            user=Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllTicketsView.fxml"));
            
            Parent root = loader.load();
            
            ShowAllTicketsViewController controller = (ShowAllTicketsViewController) loader.getController();
           
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      
    @FXML
    private void cerrarSesion(ActionEvent event) throws Exception{
         try {
            stage=Sesion.getStage();
            tema=Sesion.getTema();
            user=Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signInView.fxml"));
            
            Parent root = loader.load();
            
            SignInViewController controller = (SignInViewController) loader.getController();
           
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            Sesion.setUser(null);
          
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void irVerPerfil(ActionEvent event) throws Exception{
         try {
            stage=Sesion.getStage();
            tema=Sesion.getTema();
            user=Sesion.getUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signUpView.fxml"));
            
            Parent root = loader.load();
            
            SignUpViewController controller = (SignUpViewController) loader.getController();
           
            controller.setStage(stage);
            controller.setTema(tema);
            controller.setUser(user);
            Sesion.setUser(null);
          
            controller.initStage(root);
            
        } catch (IOException ex) {
            Logger.getLogger(ShowAllEventsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing the 'Menu'.");
        Scene scene = new Scene(root);
        stage=Sesion.getStage();
        
        stage.show();
        stage.setScene(scene);
        LOGGER.info("'ShowAllArtists' window initialized.");

    }

}
