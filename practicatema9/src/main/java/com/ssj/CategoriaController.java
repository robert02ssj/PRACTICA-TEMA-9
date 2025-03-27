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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.fxml.FXML;

public class CategoriaController {

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Categoria, String> nombreColumna;

    @FXML
    TableColumn<Categoria, Integer> IdColumna;

    @FXML
    TableColumn<Categoria, String> DescColumna;

    public ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColumna.setCellValueFactory(new PropertyValueFactory<>("id"));
        DescColumna.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tableView.setItems(listaCategorias);
        nombreColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        DescColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        
        nombreColumna.setOnEditCommit(event -> {
            Categoria Categoria = event.getRowValue();
            Categoria.setNombre(event.getNewValue());
            saveRow(Categoria);
        });  

        DescColumna.setOnEditCommit(event -> {
            Categoria Categoria = event.getRowValue();
            Categoria.setDescripcion(event.getNewValue());
            saveRow(Categoria);
        });

        tableView.setItems(listaCategorias);
        loadData();


    }

    public void loadData(){
        Categoria.getAll(listaCategorias);
    }

    @FXML
    private void CambiarEventos() throws IOException {
        App.setRoot("Eventos");
    }

    @FXML
    private void CambiarParticipantes() throws IOException {
        App.setRoot("Participante");
    }
    @FXML
    private void CambiarArtistas() throws IOException {
        App.setRoot("Artista");
    
}

    public void saveRow(Categoria Categoria) {
      Categoria.save();
}

@FXML
    public void addRow() throws IOException {
        // Creamos un usuario vacío
        Categoria filaVacia = new Categoria(Categoria.getLastId()+1, "", "");

        // Añadimos la fila vacía al ObservableList (esto lo añadirá también al TableView)
        listaCategorias.add(filaVacia);

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
            Categoria Cate = (Categoria) tableView.getSelectionModel().getSelectedItem();
            Cate.delete();  // Lo borramos de la base de datos
            listaCategorias.remove(Cate);  // Lo borramos del ObservableList y del TableView
        }
    }
}
