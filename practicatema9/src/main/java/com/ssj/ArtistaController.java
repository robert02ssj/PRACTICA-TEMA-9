package com.ssj;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;

public class ArtistaController {

    @FXML
    TextField Busqueda;

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

    @FXML
    private VBox detailsBox;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label apellido1Label;

    @FXML
    private Label apellido2Label;

    @FXML
    private Label fotoLabel;

    @FXML
    private Label obraLabel;

    @FXML
    private VBox eventosBox;

    @FXML
    private Button VerMas;

    @FXML
    private Button Exportar;

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

        // Listener para detectar selección en la tabla
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                boolean isRowSelected = newValue != null;
                VerMas.setVisible(isRowSelected);   // Muestra el botón "Ver Detalles" si hay una fila seleccionada
                Exportar.setVisible(isRowSelected); // Muestra el botón "Exportar" si hay una fila seleccionada
            }
        });
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
        a.setHeaderText("¿Estás seguro de que quieres borrar este Artista?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Obtenemos el usuario seleccionado
            Artista Arti = (Artista) tableView.getSelectionModel().getSelectedItem();
            Arti.delete();  // Lo borramos de la base de datos
            listaArtistas.remove(Arti);  // Lo borramos del ObservableList y del TableView
        }
    }

    @FXML
    private void cargarEventos(Artista artista) {
        eventosBox.getChildren().clear(); // Limpiar el VBox antes de cargar los eventos
        ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
        Evento.getAll(listaEventos); // Obtener todos los eventos

        for (Evento evento : listaEventos) {
            CheckBox checkBox = new CheckBox(evento.getNombre());
            checkBox.setSelected(artista.getEventos().stream().anyMatch(e -> e.getId() == evento.getId()));
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    String currentDate = java.time.LocalDate.now().toString(); // Obtener la fecha actual en formato AAAA-MM-DD
                    artista.Participa(evento.getId(), artista.getId(), currentDate); // Guardar participación
                } else {
                    artista.eliminarParticipacion(evento.getId(), artista.getId()); // Eliminar participación
                }
            });
            eventosBox.getChildren().add(checkBox);
        }
    }

    @FXML
    private void verArtista() {
        Artista seleccionado = (Artista) tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            nombreLabel.setText("Nombre: " + seleccionado.getNombre());
            apellido1Label.setText("Apellido 1: " + seleccionado.getApellido1());
            apellido2Label.setText("Apellido 2: " + seleccionado.getApellido2());
            fotoLabel.setText("Fotografía: " + seleccionado.getFotografia());
            obraLabel.setText("Obra Destacada: " + seleccionado.getObraDestacada());
            cargarEventos(seleccionado); // Llamar al método para cargar los eventos
            detailsBox.setVisible(true);
            detailsBox.setManaged(true);
        }
    }

    @FXML
    private void cerrarDetalles() {
        Artista artista = (Artista) tableView.getSelectionModel().getSelectedItem();
        if (artista != null) {
            for (var node : eventosBox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    Evento evento = Evento.getByName(checkBox.getText()); // Obtener el evento por su nombre
                    if (evento != null) {
                        if (checkBox.isSelected()) {
                            artista.Participa(evento.getId(), artista.getId(), "2023-01-01"); // Guardar participación
                        } else {
                            artista.eliminarParticipacion(evento.getId(), artista.getId()); // Eliminar participación
                        }
                    }
                }
            }
        }
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);
    }


    @FXML
    public void Busqueda() throws IOException {
        String busqueda = Busqueda.getText();
        if (busqueda.isEmpty()) {
            listaArtistas.clear(); // Limpiamos la lista actual
            loadData(); // Si el campo de búsqueda está vacío, recargamos todos los eventos
        } else {
            listaArtistas.clear(); // Limpiamos la lista actual
            Artista.get(busqueda, listaArtistas); // Buscamos eventos que coincidan con la búsqueda
        }
        
    }

    @FXML
    public void exportar() {
        Artista seleccionado = (Artista) tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.exportToText(listaArtistas); // Llamar al método exportar del artista seleccionado
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Exportación");
            alert.setHeaderText(null);
            alert.setContentText("El artista ha sido exportado correctamente.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un artista para exportar.");
            alert.showAndWait();
        }
    }
}
