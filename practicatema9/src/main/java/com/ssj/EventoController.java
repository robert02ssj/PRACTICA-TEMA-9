package com.ssj;

import java.io.IOException;
import java.sql.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class EventoController {

    @FXML
    TableView  tableView;

    @FXML
    TableColumn<Evento, String> nombreColumn;

    @FXML
    TableColumn<Evento, Integer> IdColumn;

    @FXML
    TableColumn<Evento, String> DescColumn;

    @FXML
    TableColumn<Evento, String> LugarCol;

    @FXML
    TableColumn<Evento,String> FechaIniCol;

    @FXML
    TableColumn<Evento, String> FechaFinCol;

    @FXML
    TableColumn<Evento, Integer> idCatCol;



    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DescColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        LugarCol.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        FechaIniCol.setCellValueFactory(new PropertyValueFactory<>("fecha_inicio"));
        FechaFinCol.setCellValueFactory(new PropertyValueFactory<>("fecha_fin"));
        idCatCol.setCellValueFactory(new PropertyValueFactory<>("id_categoria"));

        tableView.setItems(listaEventos);
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        DescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        LugarCol.setCellFactory(TextFieldTableCell.forTableColumn());
        FechaIniCol.setCellFactory(TextFieldTableCell.forTableColumn());
        FechaFinCol.setCellFactory(TextFieldTableCell.forTableColumn());
        

        nombreColumn.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setNombre(event.getNewValue());
            saveRow(Evento);
        });  

        DescColumn.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setDescripcion(event.getNewValue());
            saveRow(Evento);
        });

        LugarCol.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setLugar(event.getNewValue());
            saveRow(Evento);
        });

        FechaIniCol.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setFecha_inicio(event.getNewValue());
            saveRow(Evento);
        });

        FechaFinCol.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setFecha_fin(event.getNewValue());
            saveRow(Evento);
        });

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
    private void CambiarParticipantes() throws IOException {
        App.setRoot("Participante");
    }
    @FXML
    private void CambiarArtistas() throws IOException {
        App.setRoot("Artista");
    }
    
    public void saveRow(Evento Evento) {
        Evento.save();
    }
    
}
