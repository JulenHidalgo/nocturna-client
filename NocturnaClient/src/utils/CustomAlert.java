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

/**
 * Clase utilitaria que proporciona métodos para mostrar alertas personalizadas 
 * y recoger información del usuario a través de cuadros de diálogo.
 *  @author Julen Hidalgo
 */
public class CustomAlert {

    /**
     * Muestra una alerta personalizada con un tipo específico de alerta y mensaje.
     * Si el tipo de alerta es de confirmación, se espera que el usuario seleccione 
     * entre "Aceptar" o "Cancelar".
     * 
     * @param tipo El tipo de alerta a mostrar.
     * @param mensaje El mensaje que se muestra en la alerta.
     * @return {@code true} si el usuario selecciona "Aceptar" en una alerta de confirmación, 
     *         {@code false} en otros casos o si se cierra la alerta sin una acción.
     */
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

    /**
     * Muestra una alerta con un campo de texto para que el usuario ingrese un valor.
     * La alerta incluye un título, un encabezado y un texto de ayuda (prompt) para el campo de texto.
     * 
     * @param titulo El título de la alerta.
     * @param cabecera El texto que aparece como encabezado de la alerta.
     * @param prompt El texto de sugerencia que aparece en el campo de texto.
     * @return El texto ingresado por el usuario si se acepta, o {@code null} si se cancela.
     */
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

    /**
     * Muestra una alerta para cambiar la contraseña. La alerta incluye campos para 
     * ingresar la contraseña anterior, la nueva contraseña y la repetición de la nueva contraseña.
     * Realiza validaciones sobre la nueva contraseña y asegura que ambas contraseñas coincidan.
     * 
     * @return Un array de dos elementos, donde el primero es la contraseña anterior 
     *         y el segundo es la nueva contraseña, si la validación es exitosa. 
     *         Si se cancela la acción, devuelve {@code null}.
     */
    public static String[] lanzarAlertResetPass() {
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
            String[] array = {pfVieja.getText(), pfNueva1.getText()};
            return array;
        }
        return null;
    }
}

