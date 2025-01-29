package utils;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.Artist;
import ui_controllers.ShowAllArtistViewController;
import logic.ArtistManagerFactory;

/**
 * Custom TableCell for editing Artist information.
 */
public class ArtistEditingCell extends TableCell<Artist, String> {

    private TextField textField;
    private TextArea textArea;
    private CheckBox checkBox;
    private int columnIndex;
    private ShowAllArtistViewController controlador;

    public ArtistEditingCell(ShowAllArtistViewController controlador) {
        this.controlador = controlador;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            this.columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            switch (columnIndex) {
                case 0:
                case 1:
                    createTextField();
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();
                    break;
                case 2:
                    createTextArea();
                    setText(null);
                    setGraphic(textArea);
                    textArea.selectAll();
                    break;
                case 3:
                    createCheckBox();
                    setText(null);
                    setGraphic(checkBox);
                    break;
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (columnIndex == 3) {
            setText(checkBox.isSelected() ? "Sí" : "No");
        } else {
            setText((String) getItem());
        }
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
                switch (columnIndex) {
                    case 0:
                    case 1:
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setText(null);
                        setGraphic(textField);
                        break;
                    case 2:
                        if (textArea != null) {
                            textArea.setText(getString());
                        }
                        setText(null);
                        setGraphic(textField);
                        break;
                    case 3:
                        if (checkBox != null) {
                            checkBox.setSelected(getString().equals("Sí"));
                        }
                        setText(null);
                        setGraphic(checkBox);
                        break;
                }
            } else {
                if (columnIndex == 3) {
                    setText(getString().equals("Sí") ? "Sí" : "No");
                    setGraphic(null);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Artist artist = (Artist) getTableRow().getItem();
        if (artist != null) {
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
                case 3:
                    boolean isSelected = checkBox.isSelected();
                    if (isSelected) {
                        controlador.getSeleccionados().add(getTableView().getItems().get(getTableRow().getIndex()));
                    } else {
                        controlador.getSeleccionados().remove(getTableView().getItems().get(getTableRow().getIndex()));
                    }
                    setText(isSelected ? "Sí" : "No");
                    break;
            }
            try {
                ArtistManagerFactory.get().edit_XML(artist, artist.getId().toString());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar el mantenimiento en el servidor.", ButtonType.OK).showAndWait();
                });
                e.printStackTrace();
            }
        }
        super.commitEdit(newValue);
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            }
        });
        if (columnIndex == 0) {
            textField.setPromptText("Introduce el nombre");
        } else if (columnIndex == 1) {
            textField.setPromptText("Introduce el tipo de música");
        }
    }

    private void createTextArea() {
        textArea = new TextArea(getString());
        textArea.setWrapText(true);
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textArea.getText());
            }
        });
        textArea.setPromptText("Introduce la descripción");
    }

    private void createCheckBox() {
        checkBox = new CheckBox();
        checkBox.setSelected(getString().equals("Sí"));
        checkBox.setOnAction(event -> {
            commitEdit("");
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
