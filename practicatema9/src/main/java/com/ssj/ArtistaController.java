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

public class ArtistaController {

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Artista, String> ArtName;

    @FXML
    TableColumn<Artista, Integer> IdColArt;

    @FXML
    TableColumn<Artista, String> Apellido1ColArt;

    @FXML
    TableColumn<Artista, String> Apellido2ColArt;
    
    @FXML
    TableColumn<Artista, String> FotoColArt;
    
    @FXML
    TableColumn<Artista, String> ObraCol;

    



    private ObservableList<Persona> listaArtistas = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        ArtName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColArt.setCellValueFactory(new PropertyValueFactory<>("id"));
        Apellido1ColArt.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        Apellido2ColArt.setCellValueFactory(new PropertyValueFactory<>("apellido2"));
        FotoColArt.setCellValueFactory(new PropertyValueFactory<>("fotografia"));
        ObraCol.setCellValueFactory(new PropertyValueFactory<>("obraDestacada"));

        tableView.setItems(listaArtistas);
        
        ArtName.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido1ColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido2ColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        FotoColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        ObraCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        ArtName.setOnEditCommit(event -> {
            Artista Artista = event.getRowValue();
            Artista.setNombre(event.getNewValue());
            saveRow(Artista);
        });
        
        Apellido1ColArt.setOnEditCommit(event -> {
            Artista Artista = event.getRowValue();
            Artista.setApellido1(event.getNewValue());
            saveRow(Artista);
        });

        Apellido2ColArt.setOnEditCommit(event -> {
            Artista Artista = event.getRowValue();
            Artista.setApellido2(event.getNewValue());
            saveRow(Artista);
        });

        FotoColArt.setOnEditCommit(event -> {
            Artista Artista = event.getRowValue();
            Artista.setFotografia(event.getNewValue());
            saveRow(Artista);
        });

        ObraCol.setOnEditCommit(event -> {
            Artista Artista = event.getRowValue();
            Artista.setObraDestacada(event.getNewValue());
            saveRow(Artista);
        });


        
        tableView.setItems(listaArtistas);
        loadData();


    }

    public void loadData(){
        Artista.getAll(listaArtistas);
    }

    @FXML
    private void CambiarCategoria() throws IOException {
        App.setRoot("Categoria");
    }
    @FXML
    private void CambiarParticipantes() throws IOException {
        App.setRoot("Participante");
    }
    @FXML
    private void CambiarEventos() throws IOException {
        App.setRoot("Eventos");
    }
    
    public void saveRow(Artista Artista) {
        Artista.save();
    }

    @FXML
    public void addRow() throws IOException {
        // Creamos un usuario vacío
        Artista filaVacia = new Artista(Persona.getLastId()+1, "", "", "", "", "");

        // Añadimos la fila vacía al ObservableList (esto lo añadirá también al TableView)
        listaArtistas.add(filaVacia);

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
            Artista Arti = (Artista) tableView.getSelectionModel().getSelectedItem();
            Arti.delete();  // Lo borramos de la base de datos
            listaArtistas.remove(Arti);  // Lo borramos del ObservableList y del TableView
        }
    }


    
}
