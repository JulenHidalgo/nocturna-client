/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author 2dam
 */
public class CustomAlert {

    public static boolean throwAlertCustom(AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        if (tipo.equals(Alert.AlertType.CONFIRMATION)) {
            Optional<ButtonType> confirmar = alert.showAndWait();
            return confirmar.get() == ButtonType.OK;
        }
        alert.showAndWait();
        return false;
    }

    public static String throwAlertTextField(String titulo, String cabecera, String prompt) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);

        TextField textField = new TextField();
        textField.setPromptText(prompt);

        GridPane grid = new GridPane();
        grid.add(textField, 0, 0);

        alert.getDialogPane().setContent(grid);

        ButtonType btnAceptar = new ButtonType("Aceptar");
        ButtonType btnCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(btnAceptar, btnCancelar);

        Optional<ButtonType> confirmar = alert.showAndWait();

        if (confirmar.get() == btnAceptar) {
            return textField.getText();
        }

        return null;
    }
    
    public static void lanzarAlertResetPass(){
        
    }

}
