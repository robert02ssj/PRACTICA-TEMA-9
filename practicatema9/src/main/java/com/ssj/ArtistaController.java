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
    TableColumn<Evento, String> nombreColumn;

    @FXML
    TableColumn<Evento, Integer> IdColumn;

    @FXML
    TableColumn<Evento, String> Apellido1Col;

    @FXML
    TableColumn<Evento, String> Apellido2Col;
    
    @FXML
    TableColumn<Evento, String> FotoCol;
    
    @FXML
    TableColumn<Evento, String> ObraCol;

    



    private ObservableList<Evento> listaArtistas = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        Apellido1Col.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        Apellido2Col.setCellValueFactory(new PropertyValueFactory<>("apellido2"));
        FotoCol.setCellValueFactory(new PropertyValueFactory<>("fotografia"));
        ObraCol.setCellValueFactory(new PropertyValueFactory<>("obra_destacada"));

        tableView.setItems(listaArtistas);
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
       

        tableView.setItems(listaArtistas);
        loadData();


    }

    public void loadData(){
        //Evento.getAll(listaArtistas);
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
    

    
}
