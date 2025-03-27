package com.ssj;

import java.io.IOException;
import java.sql.Date;

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

public class ArtistaController {

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Artista, String> nombreColumnArt;

    @FXML
    TableColumn<Artista, Integer> IdColArt;

    @FXML
    TableColumn<Artista, String> Apellido1ColArt;

    @FXML
    TableColumn<Artista, String> Apellido2ColArt;
    
    @FXML
    TableColumn<Artista, String> FotoColArt;
    
    @FXML
    TableColumn<Artista, String> ObraColArt;

    



    private ObservableList<Persona> listaArtistas = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        nombreColumnArt.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColArt.setCellValueFactory(new PropertyValueFactory<>("id"));
        Apellido1ColArt.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        Apellido2ColArt.setCellValueFactory(new PropertyValueFactory<>("apellido2"));
        FotoColArt.setCellValueFactory(new PropertyValueFactory<>("fotografia"));
        ObraColArt.setCellValueFactory(new PropertyValueFactory<>("obra_destacada"));

        tableView.setItems(listaArtistas);
        nombreColumnArt.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido1ColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        Apellido2ColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        FotoColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        ObraColArt.setCellFactory(TextFieldTableCell.forTableColumn());
        
        nombreColumnArt.setOnEditCommit(event -> {
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

        ObraColArt.setOnEditCommit(event -> {
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

    


    
}
