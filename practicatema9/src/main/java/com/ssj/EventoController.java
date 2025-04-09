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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class EventoController {

    @FXML
    TextField Busqueda;

    @FXML
    TableView tableView;

    @FXML
    TableColumn<Evento, String> nombreColumn;

    @FXML
    TableColumn<Evento, Integer> IdColumn;

    @FXML
    TableColumn<Evento, String> DescColumn;

    @FXML
    TableColumn<Evento, String> LugarCol;

    @FXML
    TableColumn<Evento, String> FechaIniCol;

    @FXML
    TableColumn<Evento, String> FechaFinCol;

    @FXML
    TableColumn<Evento, String> NombreCateCol;

    @FXML
    TableColumn<Evento, Integer> NumeroParticipantesCol;

    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    private ObservableList<String> listaCategorias = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DescColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        LugarCol.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        FechaIniCol.setCellValueFactory(new PropertyValueFactory<>("fecha_inicio"));
        FechaFinCol.setCellValueFactory(new PropertyValueFactory<>("fecha_fin"));
        NombreCateCol.setCellValueFactory(new PropertyValueFactory<>("nombre_categoria"));
        NumeroParticipantesCol.setCellValueFactory(new PropertyValueFactory<>("numeroParticipantes"));

        tableView.setItems(listaEventos);

        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        DescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        LugarCol.setCellFactory(TextFieldTableCell.forTableColumn());
        FechaIniCol.setCellFactory(TextFieldTableCell.forTableColumn());
        FechaFinCol.setCellFactory(TextFieldTableCell.forTableColumn());
        NombreCateCol.setCellFactory(ComboBoxTableCell.forTableColumn(listaCategorias));
        NombreCateCol.setOnEditCommit(event -> {
            Evento evento = event.getRowValue();
            evento.setNombre_categoria(event.getNewValue());
            saveRow(evento);
        });

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

        NombreCateCol.setOnEditCommit(event -> {
            Evento Evento = event.getRowValue();
            Evento.setNombre_categoria(event.getNewValue());
            saveRow(Evento);
        });

        tableView.setItems(listaEventos);
        loadData();

    }

    /**
     * Método para cargar los datos de la base de datos en la tabla.
     * Se llama al inicializar el controlador.
     */
    public void loadData() {
        Evento.getAll(listaEventos);
        listaCategorias.clear();
        Categoria.getNombres(listaCategorias);
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

    /**
     * Método para guardar una fila en la base de datos.
     * 
     * @param Evento
     */
    public void saveRow(Evento Evento) {
        Evento.save();
    }

    /**
     * Método para añadir una nueva fila a la tabla. Se llama al hacer clic en el
     * botón "Añadir".
     * 
     * @throws IOException si ocurre un error al cargar los datos.
     */
    @FXML
    public void addRow() throws IOException {
        String today = java.time.LocalDate.now().toString();
        System.out.println(today);
        // Creamos un usuario vacío
        Evento filaVacia = new Evento(Evento.getLastId() + 1, "", "", "", today, today, 1, "");

        // Añadimos la fila vacía al ObservableList (esto lo añadirá también al
        // TableView)
        listaEventos.add(filaVacia);

        // Seleccionamos la fila recién añadida y hacemos que sea editable
        tableView.getSelectionModel().select(filaVacia);

    }

    /**
     * Método para borrar una fila de la tabla. Se llama al hacer clic en el botón
     * "Borrar".
     * Pedimos confirmación antes de borrar la fila.
     */
    @FXML
    public void deleteRow() {
        // Pedimos confirmación con un Alert antes de continuar
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Confirmación");
        a.setHeaderText("¿Estás seguro de que quieres borrar este Evento?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Obtenemos el usuario seleccionado
            Evento event = (Evento) tableView.getSelectionModel().getSelectedItem();
            event.delete(); // Lo borramos de la base de datos
            listaEventos.remove(event); // Lo borramos del ObservableList y del TableView
        }
    }

    /**
     * * Método para hacer consultas en la base de datos buscando sobre el nombre y
     * la descripcion de el evento mediante el campo de texto.
     * 
     * @throws IOException
     */
    @FXML
    public void Busqueda() throws IOException {
        String busqueda = Busqueda.getText();
        if (busqueda.isEmpty()) {
            listaEventos.clear(); // Limpiamos la lista actual
            loadData(); // Si el campo de búsqueda está vacío, recargamos todos los eventos
        } else {
            listaEventos.clear(); // Limpiamos la lista actual
            Evento.get(busqueda, listaEventos); // Buscamos eventos que coincidan con la búsqueda
        }

    }

    /**
     * Método para exportar los eventos a un archivo de texto. Se llama al hacer
     * clic en el botón "Exportar".
     * Crea un nuevo objeto Evento y llama al método exportToText para exportar los
     * datos.
     */
    @FXML
    public void exportar() {
        String today = java.time.LocalDate.now().toString();

        Evento e = new Evento(0, "", "", "", today, today, 1, "");
        loadData();
        e.exportToText(listaEventos);
        System.out.println("Exportado correctamente");
    }
}
