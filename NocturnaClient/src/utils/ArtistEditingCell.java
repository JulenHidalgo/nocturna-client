/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import logic.ArtistManagerFactory;
import model.Artist;

/**
 *
 * @author 2dam
 */
public class ArtistEditingCell extends TableCell<Artist, String> {

    private TextField textField;

    private TextArea textArea;

    public ArtistEditingCell() {

    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            if (isDescripcion()) {
                createTextArea();
            } else {
                createTextField();
            }

            if (isDescripcion()) {
                setText(null);
                setGraphic(textArea);
                textArea.selectAll();
            } else {
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }

        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText((String) getItem());
        setGraphic(null);

    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (isDescripcion()) {
                    if (textArea != null) {
                        textArea.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                }

            } else {
                setText(getString());
                setGraphic(null);
            }
        }

    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        // Obtener el mantenimiento de la fila
        Artist artist = (Artist) getTableRow().getItem();

        if (artist != null) {
            int columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            // Actualizar la propiedad correspondiente del objeto Event según el tipo de campo y el índice de la columna

            switch (columnIndex) {
                case 0:
                    artist.setNombre(newValue);
                    break;
                case 1:
                    artist.setTipoMusica(newValue);
                    break;
                case 2:
                    artist.setDescripcion(newValue);
                    break;
            }

            // Persistir el cambio en el backend
            try {

                // Llamar al método para persistir los datos
                ArtistManagerFactory.get().edit_XML(artist, artist.getId().toString());
            } catch (Exception e) {
                // Si algo falla en la persistencia, muestra un mensaje de error
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar el mantenimiento en el servidor.", ButtonType.OK).showAndWait();
                });
                e.printStackTrace();
            }
        }

        // Llamar al commitEdit original para finalizar la edición
        super.commitEdit((String) newValue);
    }

    private boolean isDescripcion() {
        if (getTableView().getColumns().indexOf(getTableColumn()) == 2) {
            return true;
        } else {
            return false;
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            }
        });
    }

    private void createTextArea() {
        textArea = new TextArea(getString());
        textArea.setWrapText(true);
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textArea.getText());
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();

    }

}
