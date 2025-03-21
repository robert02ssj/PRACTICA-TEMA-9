package com.ssj;

import java.io.IOException;

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

public class EventoController {

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Evento, String> nombreColumn;

    @FXML
    TableColumn<Evento, Integer> IdColumn;

    @FXML
    TableColumn<Evento, String> DescColumn;

    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DescColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tableView.setItems(listaEventos);
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        DescColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.setItems(listaEventos);
        loadData();


    }

    public void loadData(){
        Evento.getAll(listaEventos);
    }

    @FXML
    private void CambiarCategoria() throws IOException {
        App.setRoot("Categoria");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Eventos");
    }
}
