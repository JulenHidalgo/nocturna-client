package utils;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import logic.ClubManagerFactory;

import model.Club;

public class ClubEditingCell<T> extends TableCell<Club, T> {

    private TextField textField;
    private Hyperlink hyperlink;

    public ClubEditingCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            if (3==getTableView().getColumns().indexOf(getTableColumn())) {
                createTextField();
                setGraphic(textField);
            } else{
                createTextField();
                setGraphic(textField);
            }
            setText(null);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (3==getTableView().getColumns().indexOf(getTableColumn())) {
            createHyperLink();
            setGraphic(hyperlink);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setText(getString());
            createHyperLink();
            setGraphic(hyperlink);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        //setText(getString());
        //setGraphic(null);
        //setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

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
                if (3==getTableView().getColumns().indexOf(getTableColumn())) {
                    createHyperLink();
                    setGraphic(hyperlink);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    createHyperLink();
                    setGraphic(hyperlink);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }
    }
    
    private void createHyperLink() {
        hyperlink = new Hyperlink(getString());
        hyperlink.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        hyperlink.setStyle("-fx-text-fill: #000000;");
        hyperlink.setOnAction(event -> {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
        } catch (Exception e) {
            
            new Alert(Alert.AlertType.ERROR, "No sse ha podido abrir la URL.").showAndWait();
        }
    });
        hyperlink.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(hyperlink.getText());
            }
        });
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction(event -> commitEdit((T) textField.getText()));
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    @Override
    public void commitEdit(Object newValue) {
        // Obtener el mantenimiento de la fila
        Club club = (Club) getTableRow().getItem();

        if (club != null) {
            int columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            // Actualizar la propiedad correspondiente del objeto Event según el tipo de campo y el índice de la columna
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
            // Persistir el cambio en el backend
            try {

                // Llamar al método para persistir los datos
                ClubManagerFactory.get().edit_XML(club, club.getId().toString());
            } catch (Exception e) {
                // Si algo falla en la persistencia, muestra un mensaje de error
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar el mantenimiento en el servidor.", ButtonType.OK).showAndWait();
                });
                e.printStackTrace();
            }
        }

        // Llamar al commitEdit original para finalizar la edición
        super.commitEdit((T) newValue);
    }
}
