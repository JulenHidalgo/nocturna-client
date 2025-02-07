package utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import logic.EventManagerFactory;
import logic.ClubManagerFactory;
import model.Event;
import model.Club;

/**
 * Representa una celda editable personalizada para la edición de objetos eventos 
 * en una tabla. Dependiendo del tipo de dato de la columna, la celda 
 * mostrará diferentes tipos de controles de entrada
 * 
 * @param <T> Tipo de dato que se mostrará y editará en la celda.
 *  @author Erlantz Rey
 */
public class EventEditingCell<T> extends TableCell<Event, T> {

    private TextField textField;
    private ChoiceBox<String> choiceBox;
    private DatePicker datePicker;

    /**
     * Constructor vacío de la celda de edición.
     */
    public EventEditingCell() {
    }

    /**
     * Inicia el modo de edición de la celda, dependiendo del tipo de dato de la columna 
     * (String, Double, Integer, Date, etc.), crea el control adecuado para la edición.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            T item = getItem();
            if (item instanceof String && getTableView().getColumns().indexOf(getTableColumn()) == 0) {
                createTextField();
                setGraphic(textField);
            } else if (item instanceof Double) {
                createTextFieldForDouble();
                setGraphic(textField);
            } else if (item instanceof Integer) {
                createTextFieldForInteger();
                setGraphic(textField);
            } else if (item instanceof Date) {
                createDatePicker();
                setGraphic(datePicker);
            } else {
                createChoiceBox();
                setGraphic(choiceBox);
            }
            setText(null);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    /**
     * Cancela la edición y restaura la celda al valor original.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        setGraphic(null);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    /**
     * Actualiza el contenido de la celda con el valor adecuado cuando los datos cambian.
     * 
     * @param item El valor del ítem que se muestra en la celda.
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
                } else if (item instanceof Double) {
                    textField.setText(getString());
                    setGraphic(textField);
                } else if (item instanceof Integer) {
                    textField.setText(getString());
                    setGraphic(textField);
                } else if (item instanceof String) {
                    choiceBox.setValue((String) item);
                    setGraphic(choiceBox);
                } else if (item instanceof Date) {
                    datePicker.setValue(((Date) item).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    setGraphic(datePicker);
                }
            } else {
                setText(getString());
                setGraphic(null);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * Crea un {@link TextField} para la edición de cadenas de texto.
     */
    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            }
        });
    }

    /**
     * Crea un {@link TextField} para la edición de valores numéricos de tipo {@code Double}.
     */
    private void createTextFieldForDouble() {
        textField = new TextField();
        textField.setText(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if(!textField.getText().matches("-?\\d+(\\.\\d+)?")){
                    new Alert(Alert.AlertType.ERROR, "Deben de ser números", ButtonType.OK).showAndWait();
                    textField.setText(null);
                } else {
                    commitEdit((T) Double.valueOf(textField.getText()));
                } 
            }
        });
    }

    /**
     * Crea un {@link TextField} para la edición de valores numéricos de tipo {@code Integer}.
     */
    private void createTextFieldForInteger() {
        textField = new TextField();
        textField.setText(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if(!textField.getText().matches("-?\\d+")){
                    new Alert(Alert.AlertType.ERROR, "Deben de ser números enteros", ButtonType.OK).showAndWait();
                    textField.setText(null);
                } else {
                    commitEdit((T) Integer.valueOf(textField.getText()));
                }        
            }
        });
    }

    /**
     * Crea un {@link ChoiceBox} para la selección de clubes desde una lista obtenida 
     * del backend.
     */
    private void createChoiceBox() {
        choiceBox = new ChoiceBox<>();

        Club[] clubArray = ClubManagerFactory.get().findAll_XML(Club[].class);
        List<Club> listClubs = Arrays.asList(clubArray); 
        List<String> nombresClubs = listClubs.stream().map(Club::getNombre).collect(Collectors.toList());
        ObservableList<String> clubs = FXCollections.observableArrayList(nombresClubs);

        choiceBox.setItems(clubs);
        String currentValue = getItem() != null ? (String) getItem().toString() : "";
        choiceBox.setValue(currentValue);

        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    choiceBox.setValue(newValue);
                    Club clubSeleccionado = listClubs.stream()
                            .filter(club -> club.getNombre().equals(newValue))
                            .findFirst()
                            .orElse(null);
                    commitEdit((T) clubSeleccionado);
                }
            }
        });

        choiceBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        setGraphic(choiceBox);
    }

    /**
     * Crea un {@link DatePicker} para la edición de fechas.
     */
    private void createDatePicker() {
        datePicker = new DatePicker();
        datePicker.setValue(((Date) getItem()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        datePicker.setOnAction(event -> commitEdit((T) Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        datePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    /**
     * Obtiene el valor actual de la celda como una cadena.
     * 
     * @return El valor de la celda o una cadena vacía si es nulo.
     */
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    /**
     * Confirma la edición y actualiza el objeto {@link Event} correspondiente en el backend.
     * 
     * @param newValue El nuevo valor ingresado en la celda.
     */
    @Override
    public void commitEdit(Object newValue) {
        Event event = (Event) getTableRow().getItem();

        if (event != null) {
            int columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            if (newValue instanceof String) {
                event.setNombre((String) newValue);
            } else if (newValue instanceof Club) {
                event.setClub((Club) newValue);
            } else if (newValue instanceof Double) {
                event.setPrecioEntrada((Double) newValue);
            } else if (newValue instanceof Date) {
                event.setFecha((Date) newValue);
            } else if (newValue instanceof Integer) {
                switch (columnIndex) {
                    case 4:
                        event.setNumEntradas((int) newValue);
                        break;
                    case 6:
                        event.setConsumicion((int) newValue);
                        break;
                }
            }

            try {
                EventManagerFactory.get().edit_XML(event, event.getId().toString());
                ObservableList<Event> events = FXCollections.observableArrayList(
                        EventManagerFactory.get().findByDate_XML(Event[].class, LocalDate.now().toString()));
                getTableView().setItems(events);
            } catch (Exception e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar el mantenimiento en el servidor.", ButtonType.OK).showAndWait();
                });
                e.printStackTrace();
            }
        }

        super.commitEdit((T) newValue);
    }
}
