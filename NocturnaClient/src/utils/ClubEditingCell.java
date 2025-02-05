package utils;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import logic.ClubManagerFactory;
import model.Club;

/**
 * Representa una celda personalizada en una tabla que permite la edición 
 * de información de un club. 
 * @param <T> Tipo de dato que se mostrará y editará en la celda.
 *  @author Adrian Rocha
 */
public class ClubEditingCell<T> extends TableCell<Club, T> {

    private TextField textField;
    private Hyperlink hyperlink;

    /**
     * Constructor de la celda de edición.
     */
    public ClubEditingCell() {
    }

    /**
     * Inicia el modo de edición de la celda, mostrando un campo de texto 
     * para modificar el contenido.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setGraphic(textField);
            setText(null);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    /**
     * Cancela la edición de la celda y restaura su estado original.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        createHyperLink();
        setGraphic(hyperlink);
        setContentDisplay(3 == getTableView().getColumns().indexOf(getTableColumn()) 
                ? ContentDisplay.GRAPHIC_ONLY 
                : ContentDisplay.TEXT_ONLY);
        setText(getString());
    }

    /**
     * Actualiza el contenido de la celda cuando los datos cambian.
     * 
     * @param item  Nuevo valor del elemento.
     * @param empty Indica si la celda está vacía.
     */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (item instanceof String) {
                    textField.setText(getString());
                    setGraphic(textField);
                }
            } else {
                createHyperLink();
                setGraphic(hyperlink);
                setContentDisplay(3 == getTableView().getColumns().indexOf(getTableColumn()) 
                        ? ContentDisplay.GRAPHIC_ONLY 
                        : ContentDisplay.TEXT_ONLY);
                setText(getString());
            }
        }
    }

    /**
     * Crea un {@link Hyperlink} para representar un enlace en la celda.
     * El enlace abrirá la URL especificada al hacer clic sobre él.
     */
    private void createHyperLink() {
        hyperlink = new Hyperlink(getString());
        hyperlink.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        hyperlink.setStyle("-fx-text-fill: #000000;");
        hyperlink.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "No se ha podido abrir la URL.").showAndWait();
            }
        });
        hyperlink.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit((T) hyperlink.getText());
            }
        });
    }

    /**
     * Crea un {@link TextField} para la edición del contenido de la celda.
     */
    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction(event -> commitEdit((T) textField.getText()));
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit((T) textField.getText());
            }
        });
    }

    /**
     * Obtiene el texto representado en la celda.
     * 
     * @return El texto contenido en la celda o una cadena vacía si es nulo.
     */
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    /**
     * Confirma la edición y actualiza el {@link Club} correspondiente en la base de datos.
     * 
     * @param newValue Nuevo valor ingresado en la celda.
     */
    @Override
    public void commitEdit(Object newValue) {
        Club club = (Club) getTableRow().getItem();

        if (club != null) {
            int columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            switch (columnIndex) {
                case 0:
                    club.setNombre((String) newValue);
                    break;
                case 1:
                    club.setUbicacion((String) newValue);
                    break;
                case 2:
                    club.setCiudad((String) newValue);
                    break;
                case 3:
                    club.setInstagram((String) newValue);
                    break;
                default:
                    break;
            }
            try {
                ClubManagerFactory.get().edit_XML(club, club.getId().toString());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar el club en el servidor.", ButtonType.OK).showAndWait();
                });
                e.printStackTrace();
            }
        }

        super.commitEdit((T) newValue);
    }
}

