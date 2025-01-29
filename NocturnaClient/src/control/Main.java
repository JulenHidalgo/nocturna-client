/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.UserManagerFactory;
import model.Client;
import model.User;
import ui_controllers.MenuController;
import ui_controllers.ShowAllEventsViewController;
import ui_controllers.SignUpViewController;


public class Main extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception {
        
    
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));
        
        Parent root = loader.load();
                
        ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();

        //controller.setStage(stage);
       
        
        Sesion.setStage(stage);
        
        
        controller.initStage(root);
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
