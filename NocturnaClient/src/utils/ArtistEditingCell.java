package utils;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javax.ws.rs.WebApplicationException;
import model.Artist;
import ui_controllers.ShowAllArtistsViewController;
import logic.ArtistManagerFactory;

/**
 * Representa una celda personalizada en una tabla que permite la edición 
 * de información de un artista
 * @author Julen Hidalgo
 */
public class ArtistEditingCell extends TableCell<Artist, String> {

    private TextField textField;
    private TextArea textArea;
    private CheckBox checkBox;
    private int columnIndex;
    private ShowAllArtistsViewController controlador;
    private boolean seleccionado;

    /**
     * Constructor de la celda de edición.
     * 
     * @param controlador Controlador de la vista que gestiona la lista de artistas seleccionados.
     */
    public ArtistEditingCell(ShowAllArtistsViewController controlador) {
        this.controlador = controlador;
    }

    /**
     * Inicia el modo de edición de la celda, mostrando un campo de texto, 
     * un área de texto o un checkbox según la columna correspondiente.
     */
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

    /**
     * Cancela la edición de la celda y restaura su estado original.
     */
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

    /**
     * Actualiza el contenido de la celda cuando los datos cambian.
     * 
     * @param item  Nuevo valor del elemento.
     * @param empty Indica si la celda está vacía.
     */
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
                        setGraphic(textArea);
                        break;
                    case 3:
                        if (checkBox != null) {
                            checkBox.setSelected("Sí".equals(item));
                        }
                        setText(null);
                        setGraphic(checkBox);
                        break;
                }
            } else {
                if (columnIndex == 3) {
                    setText("Sí".equals(item) ? "Sí" : "No");
                    setGraphic(null);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
    }

    /**
     * Confirma la edición y actualiza el {@link Artist} correspondiente en la base de datos.
     * 
     * @param newValue Nuevo valor ingresado en la celda.
     */
    @Override
    public void commitEdit(String newValue) {
        try {
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
                        if (controlador.getSeleccionados().contains(getTableView().getItems().get(getTableRow().getIndex()))) {
                            controlador.getSeleccionados().remove(getTableView().getItems().get(getTableRow().getIndex()));
                            seleccionado = false;
                            setText("No");
                        } else {
                            controlador.getSeleccionados().add(getTableView().getItems().get(getTableRow().getIndex()));
                            seleccionado = true;
                            setText("Sí");
                        }
                        throw new Exception();
                }

                ArtistManagerFactory.get().edit_XML(artist, artist.getId().toString());
            }
        } catch (WebApplicationException e) {
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.ERROR, "Error al actualizar el artista en el servidor.", ButtonType.OK).showAndWait();
            });
            e.printStackTrace();
        } catch (Exception e) {
            // Captura de excepciones genéricas.
        }
    }

    /**
     * Crea un {@link TextField} para la edición de nombre o tipo de música.
     */
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

    /**
     * Crea un {@link TextArea} para la edición de la descripción del artista.
     */
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

    /**
     * Crea un {@link CheckBox} para la selección del artista en la tabla.
     */
    private void createCheckBox() {
        if (checkBox == null) {
            checkBox = new CheckBox();
            checkBox.setOnAction(event -> commitEdit("Sí"));
            checkBox.setSelected("Sí".equals(getText()));
        }
    }

    /**
     * Obtiene el texto representado en la celda.
     * 
     * @return El texto contenido en la celda o una cadena vacía si es nulo.
     */
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
