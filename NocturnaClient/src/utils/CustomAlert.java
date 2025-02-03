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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.UserManagerFactory;

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

    public static String lanzarAlertResetPass() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Cambio de contraseña");
        alert.setHeaderText("Introduce la contraseña anterior y la nueva para modificarla, pulsa cancelar para salir");

        PasswordField pfVieja = new PasswordField();
        pfVieja.setPromptText("Contraseña anterior");

        PasswordField pfNueva1 = new PasswordField();
        pfNueva1.setPromptText("Nueva contraseña");

        PasswordField pfNueva2 = new PasswordField();
        pfNueva2.setPromptText("Repite la nueva contraseña");

        GridPane grid = new GridPane();
        grid.add(pfVieja, 0, 0);
        grid.add(pfNueva1, 0, 1);
        grid.add(pfNueva2, 0, 2);

        alert.getDialogPane().setContent(grid);

        ButtonType btnAceptar = new ButtonType("Aceptar");
        ButtonType btnCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(btnAceptar, btnCancelar);

        Optional<ButtonType> confirmar = alert.showAndWait();

        if (confirmar.get() == btnAceptar) {
            if (!pfNueva1.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")) {
                CustomAlert.throwAlertCustom(AlertType.ERROR, "La contraseña tiene que tener al menos 6 caracteres, una mayúscula, una minúscula y un número");
            }
            if (!pfNueva1.getText().equals(pfNueva2.getText())) {
                CustomAlert.throwAlertCustom(AlertType.ERROR, "Las contraseñas no coinciden");
            }
            return pfNueva1.getText();
        }
        return null;
    }

}
