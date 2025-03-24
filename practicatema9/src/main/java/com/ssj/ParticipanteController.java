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
            Participante.setNombre(event.getNewValue());
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
    
}
