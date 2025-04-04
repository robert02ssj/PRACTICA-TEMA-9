package com.ssj;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;

public class ParticipanteController {

    @FXML
    TextField Busqueda;

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Participante, String> NombrePartCol;

    @FXML
    TableColumn<Participante, Integer> IdPartColumn;

    @FXML
    TableColumn<Participante, String> Apellido1Col;

    @FXML
    TableColumn<Participante, String> Apellido2Col;

    @FXML
    TableColumn<Participante, String> EmailCol;

    @FXML
    private VBox detailsBox;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label apellido1Label;

    @FXML
    private Label apellido2Label;

    @FXML
    private Label emailLabel;

    @FXML
    private VBox eventosBox;

    @FXML
    private Button Exportar;

    @FXML
    private Button VerMas;

    private ObservableList<Persona> listaParticipantes = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        NombrePartCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdPartColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        Apellido1Col.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        Apellido2Col.setCellValueFactory(new PropertyValueFactory<>("apellido2"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        tableView.setItems(listaParticipantes);
        NombrePartCol.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido1Col.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido2Col.setCellFactory(TextFieldTableCell.forTableColumn());
        EmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        NombrePartCol.setOnEditCommit(event -> {
            Participante Participante = event.getRowValue();
            Participante.setNombre(event.getNewValue());
            saveRow(Participante);
        });  

        Apellido1Col.setOnEditCommit(event -> {
            Participante Participante = event.getRowValue();
            Participante.setApellido1(event.getNewValue());
            saveRow(Participante);
        });

        Apellido2Col.setOnEditCommit(event -> {
            Participante Participante = event.getRowValue();
            Participante.setApellido2(event.getNewValue());
            saveRow(Participante);
        });

        EmailCol.setOnEditCommit(event -> {
            Participante Participante = event.getRowValue();
            Participante.setEmail(event.getNewValue());
            saveRow(Participante);
        });

        tableView.setItems(listaParticipantes);
        loadData();

        // Listener para detectar selección en la tabla
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                boolean isRowSelected = newValue != null;
                Exportar.setVisible(isRowSelected); // Muestra el botón "Exportar" si hay una fila seleccionada
                VerMas.setVisible(isRowSelected);   // Muestra el botón "Ver Detalles" si hay una fila seleccionada
            }
        });
    }

    public void loadData(){
        Participante.getAll(listaParticipantes);
    }

    @FXML
    private void verParticipante() {
        Participante seleccionado = (Participante) tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            nombreLabel.setText("Nombre: " + seleccionado.getNombre());
            apellido1Label.setText("Apellido 1: " + seleccionado.getApellido1());
            apellido2Label.setText("Apellido 2: " + seleccionado.getApellido2());
            emailLabel.setText("Email: " + seleccionado.getEmail());
            cargarEventos(seleccionado); // Llamar al método para cargar los eventos
            detailsBox.setVisible(true);
            detailsBox.setManaged(true);
        }
    }

    @FXML
    private void cerrarDetalles() {
        Participante participante = (Participante) tableView.getSelectionModel().getSelectedItem();
        if (participante != null) {
            for (var node : eventosBox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    Evento evento = Evento.getByName(checkBox.getText()); // Obtener el evento por su nombre
                    if (evento != null) {
                        if (checkBox.isSelected()) {
                            participante.Participa(evento.getId(), participante.getId(), "2023-01-01"); // Guardar participación
                        } else {
                            participante.eliminarParticipacion(evento.getId(), participante.getId()); // Eliminar participación
                        }
                    }
                }
            }
        }
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);
    }

    @FXML
    private void CambiarCategoria() throws IOException {
        App.setRoot("Categoria");
    }

    @FXML
    private void CambiarEventos() throws IOException {
        App.setRoot("Eventos");
    }
    
    @FXML
    private void CambiarArtistas() throws IOException {
        App.setRoot("Artista");
    }
    
    public void saveRow(Participante Participante) {
        Participante.save();
    }
    @FXML
    public void addRow() throws IOException {
        // Creamos un usuario vacío
        Participante filaVacia = new Participante(Persona.getLastId()+1, "", "", "", "");

        // Añadimos la fila vacía al ObservableList (esto lo añadirá también al TableView)
        listaParticipantes.add(filaVacia);

        // Seleccionamos la fila recién añadida y hacemos que sea editable
        tableView.getSelectionModel().select(filaVacia);

        

    }

        @FXML
    public void deleteRow() {
        // Pedimos confirmación con un Alert antes de continuar
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Confirmación");
        a.setHeaderText("¿Estás seguro de que quieres borrar este Participante?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Obtenemos el usuario seleccionado
            Participante Parti = (Participante) tableView.getSelectionModel().getSelectedItem();
            Parti.delete();  // Lo borramos de la base de datos
            listaParticipantes.remove(Parti);  // Lo borramos del ObservableList y del TableView
        }
    }

    @FXML
    private void cargarEventos(Participante participante) {
        eventosBox.getChildren().clear(); // Limpiar el VBox antes de cargar los eventos
        ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
        Evento.getAll(listaEventos); // Obtener todos los eventos

        for (Evento evento : listaEventos) {
            CheckBox checkBox = new CheckBox(evento.getNombre());
            checkBox.setSelected(participante.getEventos().stream().anyMatch(e -> e.getId() == evento.getId()));
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    participante.Participa(evento.getId(), participante.getId(), "2023-01-01"); // Fecha de ejemplo
                } else {
                    // Aquí puedes implementar la lógica para eliminar la participación si es necesario
                }
            });
            eventosBox.getChildren().add(checkBox);
        }
    }

    @FXML
    public void Busqueda() throws IOException {
        String busqueda = Busqueda.getText();
        if (busqueda.isEmpty()) {
            listaParticipantes.clear(); // Limpiamos la lista actual
            loadData(); // Si el campo de búsqueda está vacío, recargamos todos los eventos
        } else {
            listaParticipantes.clear(); // Limpiamos la lista actual
            Participante.get(busqueda, listaParticipantes); // Buscamos eventos que coincidan con la búsqueda
        }
    }
    

    @FXML
    public void exportar(){
        Participante seleccionado = (Participante) tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.exportToText(listaParticipantes); // Llamar al método exportar del participante seleccionado
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Exportación");
            alert.setHeaderText(null);
            alert.setContentText("El participante ha sido exportado correctamente.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un participante para exportar.");
            alert.showAndWait();
        }

    }
}
