package ui_controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.ArtistManagerFactory;
import logic.ClubManagerFactory;
import logic.EventManagerFactory;
import model.Artist;
import model.Club;
import model.Event;

public class EventEditingCell<T> extends TableCell<Event, T> {

    private TextField textField;
    private ChoiceBox<String> choiceBox;
    private DatePicker datePicker;

    public EventEditingCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            T item = getItem();
            if (item instanceof String) {
                createTextField();
                setGraphic(textField);
            } else if (item instanceof Double) {
                createTextFieldForDouble();
                setGraphic(textField);
            }else if (item instanceof Integer) {
                createTextFieldForInteger();
                setGraphic(textField);
            }else if (item instanceof Date) {
                createDatePicker();
                setGraphic(datePicker);
            }else{
               // createDatePicker();
               // setGraphic(datePicker);
                createChoiceBox();
                setGraphic(choiceBox);
            }
            setText(null);
            //setGraphic(item instanceof String ? textField : item instanceof ChoiceBox ? choiceBox : datePicker);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        setGraphic(null);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
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
                } else if (item instanceof Double) {
                    textField.setText(getString());
                    setGraphic(textField);
                }else if (item instanceof Integer) {
                    textField.setText(getString());
                    setGraphic(textField);
                }else if ((getTableColumn().getCellData(getIndex()) instanceof String)) {
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

    private void createTextFieldForDouble() {
        textField = new TextField();
        textField.setText(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                 commitEdit((T) Double.valueOf(textField.getText()));
            }
        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
 
    private void createTextFieldForInteger() {
        textField = new TextField();
        textField.setText(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                 commitEdit((T) Integer.valueOf(textField.getText()));
            }
        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
    
 private void createChoiceBox() {
    choiceBox = new ChoiceBox<>();

    // Obtener los clubes desde el backend
    Club[] clubArray = ClubManagerFactory.get().findAll_XML(Club[].class);

    // Convertir los datos en una lista de nombres
    List<Club> listClubs = Arrays.asList(clubArray); 
    List<String> nombresClubs = listClubs.stream().map(Club::getNombre).collect(Collectors.toList());
    ObservableList<String> clubs = FXCollections.observableArrayList(nombresClubs);

    // Crear una lista observable y asignarla al ChoiceBox
    choiceBox.setItems(clubs);
   
    // Establecer el valor actual del ChoiceBox
    String currentValue = (String) getItem().toString();
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

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    @Override
    public void commitEdit(Object newValue) {
        // Obtener el mantenimiento de la fila
        Event event = (Event) getTableRow().getItem();

        if (event != null) {
            int columnIndex = getTableView().getColumns().indexOf(getTableColumn());
            // Actualizar la propiedad correspondiente del objeto Event según el tipo de campo y el índice de la columna
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
                    // Agrega más casos según la estructura de tus datos
                }
            }
            // Persistir el cambio en el backend
            try {

                // Llamar al método para persistir los datos
                EventManagerFactory.get().edit_XML(event, event.getId().toString());

            // Recargar los datos desde el backend
            ObservableList<Event> events = FXCollections.observableArrayList(EventManagerFactory.get().findByDate_XML(Event[].class, LocalDate.now().toString()));
            getTableView().setItems(events);
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
