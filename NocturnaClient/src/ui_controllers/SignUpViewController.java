/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ClientManagerFactory;
import logic.UserManagerFactory;
import model.Client;
import model.User;
import utils.CustomAlert;

/**
 *
 * @author 2dam
 */
public class SignUpViewController {

    /**
     * Panel principal que contiene los elementos de la interfaz.
     */
    @FXML
    AnchorPane anchorPane;

    @FXML
    TextField tfNombre;

    @FXML
    TextField tfApellido;

    @FXML
    TextField tfTelefono;

    @FXML
    DatePicker dpFechaNac;

    @FXML
    TextField tfCiudad;

    @FXML
    VBox vbDni;

    @FXML
    TextField tfDni;

    @FXML
    TextField tfMail;

    @FXML
    VBox vbPass;

    @FXML
    PasswordField pfPass;

    @FXML
    VBox vbPass2;

    @FXML
    PasswordField pfPass2;

    @FXML
    HBox hbTienesCuenta;

    @FXML
    Hyperlink hlSignIn;

    @FXML
    Button btnCambioPass;

    @FXML
    Button btnElimCuenta;

    @FXML
    Button btnSignUp;

    @FXML
    Button btnModificarDatos;

    private Stage stage;

    private User user;

    private boolean tema;

    private User newUser;

    private final Logger LOGGER = Logger.getLogger("SignUpViewController.view");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTema(boolean tema) {
        this.tema = tema;
    }

    private void closeAppFromX(WindowEvent event) {
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION,"¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing 'SignUp' window.");
        Scene scene = new Scene(root);
        //Si el usuario es null, significa que no ha entrado a la app todavía y 
        //está intentando registrarse, si no, significa que va a modificar su información.
        if (this.user == null) {
            stage.setTitle("Registro de usuarios");
            btnCambioPass.setVisible(false);
            btnElimCuenta.setVisible(false);
            btnModificarDatos.setVisible(false);
        } else {
            stage.setTitle("Modificar información");
            btnSignUp.setVisible(false);
            hbTienesCuenta.setVisible(false);
            vbPass.setVisible(false);
            vbPass2.setVisible(false);
            vbDni.setVisible(false);
            btnModificarDatos.setVisible(true);
            tfNombre.setText(((Client) user).getNombre());
            tfApellido.setText(((Client) user).getApellido());
            tfCiudad.setText(((Client) user).getCiudad());
            tfMail.setText(((Client) user).getMail());
            tfTelefono.setText(((Client) user).getTelefono().toString());
            dpFechaNac.setValue(((Client) user).getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        stage.setOnCloseRequest(this::closeAppFromX);
        btnSignUp.setOnAction(this::signUp);
        btnCambioPass.setOnAction(this::resetPass);
        btnModificarDatos.setOnAction(this::updateInfo);
        btnElimCuenta.setOnAction(this::deleteUser);

        dpFechaNac.setDayCellFactory(picker -> {
            return new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    // Establecer como no seleccionable cualquier fecha posterior a hoy
                    setDisable(item.isAfter(LocalDate.now()));
                }
            };
        });

        stage.show();
        stage.setScene(scene);
        LOGGER.info("'SignUp' window initialized.");

    }


    private void updateInfo(ActionEvent event) {
        try {
            if (tfNombre.getText().isEmpty() || tfApellido.getText().isEmpty()
                    || tfCiudad.getText().isEmpty() || tfTelefono.getText().isEmpty()
                    || dpFechaNac.getValue() == null
                    || tfMail.getText().isEmpty()) {

                LOGGER.warning("Hay campos vacios");
                CustomAlert.throwAlertCustom(AlertType.ERROR, "Todos los campos tienen que estar llenos");
                throw new Exception();
            }
            checkMail();
            checkPhone();
            checkDate();

            checkModifyInfo();

            newUser.setId(user.getId());

            ClientManagerFactory.get().edit_XML(newUser, newUser.getId().toString());

            CustomAlert.throwAlertCustom(AlertType.INFORMATION, "La información se ha modificado con exito");
        } catch (Exception e) {

        }

    }

    private void deleteUser(ActionEvent event) {
        if (CustomAlert.throwAlertTextField("Borrado de cuenta", "Introduce la contraseña para borrar la cuenta (Se perderán todas las entradas asociadas al usuario), pulsa cancelar para salir", "Contraseña de la cuenta") != null) {
            ClientManagerFactory.get().remove(user.getId().toString());

            CustomAlert.throwAlertCustom(AlertType.INFORMATION, "El usuario se ha borrado correctamente.");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signInView.fxml"));

                Parent root = loader.load();

                SignInViewController controller = (SignInViewController) loader.getController();

                controller.setStage(stage);
                controller.setTema(tema);
                controller.initStage(root);

            } catch (IOException e) {
                CustomAlert.throwAlertCustom(AlertType.ERROR, "Ha sucedido un error al cargar la ventana, intentalo más tarde");
                LOGGER.warning("Error while opening 'SignIn' window");
            }
        }
    }

    private void resetPass(ActionEvent event) {
        try {
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
                    LOGGER.warning("Password validation error, pattern incorrect");
                    CustomAlert.throwAlertCustom(AlertType.ERROR, "La contraseña tiene que tener al menos 6 caracteres, una mayúscula, una minúscula y un número");
                    throw new Exception();
                }
                if (!pfNueva1.getText().equals(pfNueva2.getText())) {
                    LOGGER.warning("Password validation error, pfNueva1 and pfNueva2 don't match");
                    CustomAlert.throwAlertCustom(AlertType.ERROR, "Las contraseñas no coinciden");
                    throw new Exception();
                }
                UserManagerFactory.get().updatePasswd_XML(user, pfNueva1.getText());
            }

        } catch (Exception e) {

        }

    }

    public void signUp(ActionEvent event) {
        try {
            if (tfNombre.getText().isEmpty() || tfApellido.getText().isEmpty()
                    || tfCiudad.getText().isEmpty() || tfTelefono.getText().isEmpty()
                    || tfDni.getText().isEmpty() || dpFechaNac.getValue() == null
                    || tfMail.getText().isEmpty() || pfPass.getText().isEmpty()) {

                LOGGER.warning("Hay campos vacios");
                CustomAlert.throwAlertCustom(AlertType.ERROR, "Todos los campos tienen que estar llenos");
                throw new Exception();
            }

            checkMail();
            checkPhone();
            checkDNI();
            checkPass();
            checkDate();

            Client client = new Client();
            client.setNombre(tfNombre.getText());
            client.setApellido(tfApellido.getText());
            client.setCiudad(tfCiudad.getText());
            client.setTelefono(Integer.parseInt(tfTelefono.getText()));
            client.setFechaNacimiento(Date.from(dpFechaNac.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            client.setDni(tfDni.getText());
            client.setMail(tfMail.getText());
            client.setPasswd(pfPass.getText());

            ClientManagerFactory.get().create_XML(client);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));

            Parent root = loader.load();

            ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();

            controller.setStage(stage);
            controller.setTema(tema);
            controller.initStage(root);

        } catch (IOException e) {
            CustomAlert.throwAlertCustom(AlertType.ERROR, "Ha sucedido un error al cargar la ventana, intentalo más tarde");
            LOGGER.warning("Error while opening 'ShowAllEvents' window");
        } catch (Exception e) {
            System.out.println("ERROR");
        }

    }

    public void checkModifyInfo() throws Exception {
        newUser = new Client();
        newUser.setMail(tfMail.getText());
        ((Client) newUser).setNombre(tfNombre.getText());
        ((Client) newUser).setApellido(tfApellido.getText());
        ((Client) newUser).setCiudad(tfCiudad.getText());
        ((Client) newUser).setTelefono(Integer.valueOf(tfTelefono.getText()));
        ((Client) newUser).setFechaNacimiento(Date.from(dpFechaNac.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newUser.setPasswd(this.user.getPasswd());
        ((Client) newUser).setDni(((Client) this.user).getDni());

        if (newUser.equals(user)) {
            throw new Exception();
        }
    }

    public void checkMail() throws Exception {
        if (!tfMail.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            LOGGER.warning("Mail validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El correo no tiene un patrón correcto");
            throw new Exception();
        }
    }

    public void checkPhone() throws Exception {
        if (!tfTelefono.getText().matches("^\\d{9}$")) {
            LOGGER.warning("PhoneNumber validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El teléfono tiene que contener 9 números");
            throw new Exception();
        }
    }

    public void checkDNI() throws Exception {
        if (!tfDni.getText().matches("^\\d{8}[A-Za-z]$")) {
            LOGGER.warning("DNI validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El DNI tiene que estar compuesto por 8 números y una letra");
            throw new Exception();
        }
    }

    public void checkPass() throws Exception {
        if (!pfPass.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")) {
            LOGGER.warning("Password validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "La contraseña tiene que tener al menos 6 caracteres, una mayúscula, una minúscula y un número");
            throw new Exception();
        }
        if (!pfPass.getText().equals(pfPass2.getText())) {
            LOGGER.warning("Password validation error, pfPass and pfPass2 don't match");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "Las contraseñas no coinciden");
            throw new Exception();
        }
    }

    public void checkDate() throws Exception {
        if (dpFechaNac.getValue().isAfter(LocalDate.now())) {
            LOGGER.warning("Date validation error, later date");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "La fecha de nacimiento no puede ser posterior a la fecha actual");
            throw new Exception();
        }
    }

}
