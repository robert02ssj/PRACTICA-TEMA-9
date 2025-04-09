package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Clase que representa un evento. Contiene información sobre el evento, como
 * su nombre, descripción, lugar, fechas, categoría y número de participantes.
 * También incluye métodos para interactuar con la base de datos y exportar
 * información.
 */
public class Evento implements Exportable {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty descripcion;
    private StringProperty lugar;
    private StringProperty fecha_inicio;
    private StringProperty fecha_fin;
    private IntegerProperty id_categoria;
    private StringProperty nombre_categoria;
    private IntegerProperty numeroParticipantes;

    /**
     * Constructor de la clase Evento.
     * 
     * @param id               Identificador único del evento.
     * @param nombre           Nombre del evento.
     * @param descripcion      Descripción del evento.
     * @param lugar            Lugar donde se realizará el evento.
     * @param fecha_inicio     Fecha de inicio del evento.
     * @param fecha_fin        Fecha de finalización del evento.
     * @param id_categoria     Identificador de la categoría del evento.
     * @param nombre_categoria Nombre de la categoría del evento.
     */
    public Evento(int id, String nombre, String descripcion, String lugar, String fecha_inicio,
            String fecha_fin, int id_categoria, String nombre_categoria) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.lugar = new SimpleStringProperty(lugar);
        this.fecha_inicio = new SimpleStringProperty(fecha_inicio);
        this.fecha_fin = new SimpleStringProperty(fecha_inicio);
        this.id_categoria = new SimpleIntegerProperty(id_categoria);
        this.nombre_categoria = new SimpleStringProperty(nombre_categoria);
        this.numeroParticipantes = new SimpleIntegerProperty(0);
    }
    

    /**
     * Obtiene el identificador del evento.
     * 
     * @return El identificador del evento.
     */
    public int getId() {
        return id.get();
    }

    /**
     * Establece el identificador del evento.
     * 
     * @param id El nuevo identificador del evento.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Obtiene el nombre del evento.
     * 
     * @return El nombre del evento.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Establece el nombre del evento.
     * 
     * @param nombre El nuevo nombre del evento.
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * Obtiene la descripción del evento.
     * 
     * @return La descripción del evento.
     */
    public String getDescripcion() {
        return descripcion.get();
    }

    /**
     * Establece la descripción del evento.
     * 
     * @param descripcion La nueva descripción del evento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    /**
     * Obtiene el lugar del evento.
     * 
     * @return El lugar del evento.
     */
    public String getLugar() {
        return lugar.get();
    }

    /**
     * Establece el lugar del evento.
     * 
     * @param lugar El nuevo lugar del evento.
     */
    public void setLugar(String lugar) {
        this.lugar.set(lugar);
    }

    /**
     * Obtiene la fecha de inicio del evento.
     * 
     * @return La fecha de inicio del evento.
     */
    public String getFecha_inicio() {
        return fecha_inicio.get();
    }

    /**
     * Establece la fecha de inicio del evento.
     * 
     * @param fecha_inicio La nueva fecha de inicio del evento.
     */
    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio.set(fecha_inicio);
    }

    /**
     * Obtiene la fecha de finalización del evento.
     * 
     * @return La fecha de finalización del evento.
     */
    public String getFecha_fin() {
        return fecha_fin.get();
    }

    /**
     * Establece la fecha de finalización del evento.
     * 
     * @param fecha_fin La nueva fecha de finalización del evento.
     */
    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin.set(fecha_fin);
    }

    /**
     * Obtiene el identificador de la categoría del evento.
     * 
     * @return El identificador de la categoría del evento.
     */
    public int getId_categoria() {
        return id_categoria.get();
    }

    /**
     * Establece el identificador de la categoría del evento.
     * 
     * @param id_categoria El nuevo identificador de la categoría del evento.
     */
    public void setId_categoria(int id_categoria) {
        this.id_categoria.set(id_categoria);
    }

    /**
     * Obtiene el nombre de la categoría del evento.
     * 
     * @return El nombre de la categoría del evento.
     */
    public String getNombre_categoria() {
        return nombre_categoria.get();
    }

    /**
     * Establece el nombre de la categoría del evento.
     * 
     * @param nombre_categoria El nuevo nombre de la categoría del evento.
     */
    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria.set(nombre_categoria);
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM CATEGORIA WHERE nombre = '" + nombre_categoria + "'");
            if (rs.next()) {
                this.id_categoria.set(rs.getInt("id"));
            } else {
                this.id_categoria.set(1);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Obtiene el número de participantes del evento.
     * 
     * @return El número de participantes del evento.
     */
    public int getNumeroParticipantes() {
        return numeroParticipantes.get();
    }

    /**
     * Establece el número de participantes del evento.
     * 
     * @param numeroParticipantes El nuevo número de participantes del evento.
     */
    public void setNumeroParticipantes(int numeroParticipantes) {
        this.numeroParticipantes.set(numeroParticipantes);
    }

    /**
     * Obtiene todos los eventos de la base de datos y los añade a la lista proporcionada.
     * 
     * @param listaEventos Lista donde se añadirán los eventos obtenidos.
     */
    public static void getAll(ObservableList<Evento> listaEventos) {

        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT EVENTO.*, CATEGORIA.nombre AS categoria_nombre,(SELECT COUNT(*) FROM PARTICIPA WHERE PARTICIPA.id_evento = EVENTO.id) AS numero_participantes FROM EVENTO INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id");
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("categoria_nombre"));
                evento.setNumeroParticipantes(rs.getInt("numero_participantes"));
                listaEventos.add(evento);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Obtiene un evento por su identificador.
     * 
     * @param id Identificador del evento buscado.
     * @return El evento encontrado o null si no existe.
     */
    public Evento get(int id) {
        Connection con = ConexionBD.getConection();
        Evento e = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM EVENTO INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id WHERE EVENTO.id = "
                            + id);
            if (rs.next()) {
                e = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("CATEGORIA.nombre"));
            } else {
                e = null;
            }
            con.close();
        } catch (Exception exc) {
            System.out.println("Error en SQL " + exc);
        }
        return e;
    }

    /**
     * Busca eventos en la base de datos cuyo nombre o descripción contengan el texto especificado.
     * 
     * @param txt          Texto a buscar en el nombre o descripción de los eventos.
     * @param listaEventos Lista donde se añadirán los eventos encontrados.
     */
    public static void get(String txt, ObservableList<Evento> listaEventos) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT EVENTO.*, CATEGORIA.nombre AS categoria_nombre,(SELECT COUNT(*) FROM PARTICIPA WHERE PARTICIPA.id_evento = EVENTO.id) AS numero_participantes FROM EVENTO INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id WHERE EVENTO.nombre LIKE '%"
                            + txt + "%' OR EVENTO.descripcion LIKE '%" + txt + "%'");
            while (rs.next()) {
                Evento e = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("categoria_nombre"));
                e.setNumeroParticipantes(rs.getInt("numero_participantes"));
                listaEventos.add(e);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Obtiene el último ID asignado en la tabla de eventos.
     * 
     * @return El último ID asignado o 0 si la tabla está vacía.
     */
    public static int getLastId() {
        Connection con = ConexionBD.getConection();
        int id = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(id) FROM EVENTO");
            if (rs.next()) {
                id = rs.getInt(1);
            } else {
                id = 0;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return id;
    }

    /**
     * Guarda el evento actual en la base de datos. Si el evento ya existe, lo actualiza;
     * de lo contrario, lo inserta como un nuevo registro.
     * 
     * @return 1 si se guardó correctamente, 0 en caso contrario.
     */
    public int save() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO WHERE id = " + this.getId());
            if (rs.next()) {
                // Modificar
                st.executeUpdate("UPDATE EVENTO SET nombre = '" + this.getNombre() + "', descripcion = '"
                        + this.getDescripcion() + "', lugar = '" + this.getLugar() + "', fecha_inicio = '"
                        + this.getFecha_inicio() + "', fecha_fin = '" + this.getFecha_fin()
                        + "', id_categoria = " + this.getId_categoria() + " WHERE id = " + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate(
                        "INSERT INTO EVENTO (id, nombre, descripcion, lugar, fecha_inicio, fecha_fin, id_categoria) VALUES ("
                                + this.getId() + ", '" + this.getNombre() + "', '" + this.getDescripcion() + "', '"
                                + this.getLugar()
                                + "', '" + this.getFecha_inicio() + "', '"
                                + this.getFecha_fin() + "', "
                                + this.getId_categoria() + ")");
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
            resultado = 0;
        }
        return resultado;
    }

    /**
     * Elimina el evento actual de la base de datos.
     * 
     * @return 1 si se eliminó correctamente, 0 en caso contrario.
     */
    public int delete() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO WHERE id = " + this.getId());
            if (rs.next()) {
                st.executeUpdate("DELETE FROM EVENTO WHERE id = " + this.getId());
                resultado = 1;
            } else {
                resultado = 0;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
            resultado = 0;
        }
        return resultado;

    }

    /**
     * Obtiene la categoría a la que pertenece el evento.
     * 
     * @return La categoría del evento.
     */
    public Categoria getCategoria() {
        Categoria c = Categoria.get(getId_categoria());
        return c;
    }

    /**
     * Obtiene un evento por su nombre.
     * 
     * @param nombre Nombre del evento buscado.
     * @return El evento encontrado o null si no existe.
     */
    public static Evento getByName(String nombre) {
        Connection con = ConexionBD.getConection();
        Evento evento = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM EVENTO JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id WHERE EVENTO.nombre = '"
                            + nombre + "'");
            if (rs.next()) {
                evento = new Evento(rs.getInt("EVENTO.id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("CATEGORIA.nombre"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return evento;
    }

    /**
     * Exporta la lista de eventos a un archivo de texto, incluyendo detalles como
     * nombre, descripción, lugar, fechas, categoría y número de participantes.
     * 
     * @param listaEventos Lista de eventos a exportar.
     */
    @Override
    public void exportToText(ObservableList<?> listaEventos) {
        try {
        FileWriter fw = new FileWriter("Eventos.txt", true);
        for (int i = 0; i < listaEventos.size(); i++) {
            listaEventos.get(i);
            Evento evento = (Evento) listaEventos.get(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Evento: ").append(evento.getNombre()).append("\n");
            sb.append("Descripción: ").append(evento.getDescripcion()).append("\n");
            sb.append("Lugar: ").append(evento.getLugar()).append("\n");
            sb.append("Fecha de inicio: ").append(evento.getFecha_inicio()).append("\n");
            sb.append("Fecha de fin: ").append(evento.getFecha_fin()).append("\n");
            sb.append("Categoría: ").append(evento.getNombre_categoria()).append("\n");
            sb.append("Número de participantes: ").append(evento.getNumeroParticipantes()).append("\n");
                fw.write(sb.toString());
                fw.write("\n");

        }
        fw.close();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
            
        }
    

    /**
     * Exporta la lista de eventos a un archivo PDF.
     * (Método aún no implementado).
     */
    @Override
    public void exportToPDF() {

    }
}
