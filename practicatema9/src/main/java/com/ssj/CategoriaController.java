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

    
}
