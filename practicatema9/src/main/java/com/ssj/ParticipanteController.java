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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ParticipanteController {

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
            detailsBox.setVisible(true);
            detailsBox.setManaged(true);
        }
    }

    @FXML
    private void cerrarDetalles() {
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
        a.setHeaderText("¿Estás seguro de que quieres borrar este Evento?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Obtenemos el usuario seleccionado
            Participante Parti = (Participante) tableView.getSelectionModel().getSelectedItem();
            Parti.delete();  // Lo borramos de la base de datos
            listaParticipantes.remove(Parti);  // Lo borramos del ObservableList y del TableView
        }
    }
    
}
