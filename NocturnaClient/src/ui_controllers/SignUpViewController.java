/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui_controllers;

import model.Sesion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ClientManagerFactory;
import logic.UserManagerFactory;
import model.Client;
import model.User;
import security.AsimetricEncrypt;
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

    @FXML
    Button btnVerPass1;

    @FXML
    Button btnVerPass2;

    @FXML
    TextField tfPass1;

    @FXML
    TextField tfPass2;

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
        if (CustomAlert.throwAlertCustom(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea salir?")) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    private void verPass1(ActionEvent event) {
        if (tfPass1.isVisible()) {
            pfPass.setVisible(true);
            tfPass1.setVisible(false);
            pfPass.setText(tfPass1.getText());
        } else {
            pfPass.setVisible(false);
            tfPass1.setVisible(true);
            tfPass1.setText(pfPass.getText());
        }
    }

    private void verPass2(ActionEvent event) {
        if (tfPass2.isVisible()) {
            pfPass2.setVisible(true);
            tfPass2.setVisible(false);
            pfPass2.setText(tfPass2.getText());
        } else {
            pfPass2.setVisible(false);
            tfPass2.setVisible(true);
            tfPass2.setText(pfPass2.getText());
        }
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing 'SignUp' window.");
        stage = Sesion.getStage();
        user = Sesion.getUser();
        Scene scene = new Scene(root);
        //Si el usuario es null, significa que no ha entrado a la app todavía y 
        //está intentando registrarse, si no, significa que va a modificar su información.
        if (user == null) {
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
            tfMail.setEditable(false);
            btnModificarDatos.setOnAction(this::updateInfo);
            btnElimCuenta.setOnAction(this::deleteUser);

            addListenersToTextFields();
        }

        stage.setOnCloseRequest(this::closeAppFromX);
        btnSignUp.setOnAction(this::signUp);
        btnCambioPass.setOnAction(this::resetPass);
        btnModificarDatos.setOnAction(this::updateInfo);
        btnElimCuenta.setOnAction(this::deleteUser);
        btnVerPass1.setOnAction(this::verPass1);
        btnVerPass2.setOnAction(this::verPass2);

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


    private void addListenersToTextFields() {
        tfNombre.textProperty().addListener((observable, oldValue, newValue) -> checkChangesModify());
        tfApellido.textProperty().addListener((observable, oldValue, newValue) -> checkChangesModify());
        tfTelefono.textProperty().addListener((observable, oldValue, newValue) -> checkChangesModify());
        dpFechaNac.valueProperty().addListener((observable, oldValue, newValue) -> checkChangesModify());
        tfCiudad.textProperty().addListener((observable, oldValue, newValue) -> checkChangesModify());
    }

    private void checkChangesModify() {
        if (tfNombre.getText().equals(((Client) this.user).getNombre())
                && tfApellido.getText().equals(((Client) this.user).getApellido())
                && tfTelefono.getText().equals(((Client) this.user).getTelefono().toString())
                && Date.from(dpFechaNac.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).equals(((Client) this.user).getFechaNacimiento())
                && tfCiudad.getText().equals(((Client) this.user).getCiudad())) {
            btnModificarDatos.setDisable(true);
        } else {
            btnModificarDatos.setDisable(false);
        }
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
            newUser.setMail(URLEncoder.encode(AsimetricEncrypt.encrypt(tfMail.getText()), "UTF-8"));
            newUser.setPasswd(URLEncoder.encode(AsimetricEncrypt.encrypt(pfPass.getText()), "UTF-8"));

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

    private void resetPass(ActionEvent event){
        String[] respuesta;
        String oldPass, newPass;
        User usuario = new User();
        try {
            respuesta = CustomAlert.lanzarAlertResetPass();
            if (respuesta != null) {
                oldPass = URLEncoder.encode(AsimetricEncrypt.encrypt(respuesta[0]), "UTF-8");
                newPass = URLEncoder.encode(AsimetricEncrypt.encrypt(respuesta[1]), "UTF-8");
                
                usuario.setMail(this.user.getMail());
                usuario.setPasswd(oldPass);
                
                UserManagerFactory.get().updatePasswd_XML(usuario, newPass);
            }
        } catch (UnsupportedEncodingException ex) {
            LOGGER.severe("An error occurred encrypting the new password");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "Ha sucedido un error guardando la contraseña");
        } 

    }

    private void signUp(ActionEvent event) {
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
            client.setMail(URLEncoder.encode(AsimetricEncrypt.encrypt(tfMail.getText()), "UTF-8"));
            client.setPasswd(URLEncoder.encode(AsimetricEncrypt.encrypt(pfPass.getText()), "UTF-8"));
            ClientManagerFactory.get().create_XML(client);
            client.setMail(tfMail.getText());
            Sesion.setUser(client);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/showAllEventsView.fxml"));

            Parent root = loader.load();

            ShowAllEventsViewController controller = (ShowAllEventsViewController) loader.getController();

            controller.initStage(root);

        } catch (IOException e) {
            CustomAlert.throwAlertCustom(AlertType.ERROR, "Ha sucedido un error al cargar la ventana, intentalo más tarde");
            LOGGER.warning("Error while opening 'ShowAllEvents' window");
        } catch (Exception e) {
            System.out.println("ERROR");
        }

    }

    private void checkModifyInfo() throws Exception {
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

    private void checkMail() throws Exception {
        if (!tfMail.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            LOGGER.warning("Mail validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El correo no tiene un patrón correcto");
            throw new Exception();
        }
    }

    private void checkPhone() throws Exception {
        if (!tfTelefono.getText().matches("^\\d{9}$")) {
            LOGGER.warning("PhoneNumber validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El teléfono tiene que contener 9 números");
            throw new Exception();
        }
    }

    private void checkDNI() throws Exception {
        if (!tfDni.getText().matches("^\\d{8}[A-Za-z]$")) {
            LOGGER.warning("DNI validation error, pattern incorrect");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "El DNI tiene que estar compuesto por 8 números y una letra");
            throw new Exception();
        }
    }


    private void checkPass() throws Exception {
        if (tfPass1.isVisible()) {
            pfPass.setText(tfPass1.getText());
        }
        if (tfPass2.isVisible()) {
            pfPass2.setText(tfPass2.getText());
        }
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

    private void checkDate() throws Exception {
        if (dpFechaNac.getValue().isAfter(LocalDate.now())) {
            LOGGER.warning("Date validation error, later date");
            CustomAlert.throwAlertCustom(AlertType.ERROR, "La fecha de nacimiento no puede ser posterior a la fecha actual");
            throw new Exception();
        }
    }

}
