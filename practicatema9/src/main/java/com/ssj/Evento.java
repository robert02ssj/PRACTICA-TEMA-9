package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Evento {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty descripcion;
    private StringProperty lugar;
    private StringProperty fecha_inicio;
    private StringProperty fecha_fin;
    private IntegerProperty id_categoria;
    private StringProperty nombre_categoria;

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
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getLugar() {
        return lugar.get();
    }

    public void setLugar(String lugar) {
        this.lugar.set(lugar);
    }

    public String getFecha_inicio() {
        return fecha_inicio.get();
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio.set(fecha_inicio);
    }

    public String getFecha_fin() {
        return fecha_fin.get();
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin.set(fecha_fin);
    }

    public int getId_categoria() {
        return id_categoria.get();
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria.set(id_categoria);
    }

    public String getNombre_categoria() {
        return nombre_categoria.get();
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria.set(nombre_categoria);
    }
    /**
     * Devolverá un array con todos los eventos de la BD o null si no hay ningún
     * evento
     * 
     * @param
     * @return el array de eventos o null si no hay eventos
     */
    public static void getAll(ObservableList<Evento> listaEventos) {

        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id");
            while (rs.next()) {
                listaEventos.add(new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("CATEGORIA.nombre")));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Devolverá un evento con el id del evento que buscamos o null si no existe
     * 
     * @param id el id del evento buscado
     * @return el id del evento o null si no existe
     */
    public Evento get(int id) {
        Connection con = ConexionBD.getConection();
        Evento e = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO WHERE id = " + id + " INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id");
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
     * Buscará en la BD los eventos cuyo nombre o descripción contengan el texto
     * "txt". Devolverá un array de eventos que cumplan la condición, o null si
     * ninguno la cumple.
     * 
     * @param txt
     * @return el array de eventos o null si no hay eventos
     *
     */
    public void get(String txt, ObservableList<Evento> listaEventos) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM EVENTO INNER JOIN CATEGORIA ON EVENTO.id_categoria = CATEGORIA.id WHERE nombre LIKE '%" + txt + "%' OR descripcion LIKE '%" + txt + "%'");
            while (rs.next()) {
                Evento e = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getInt("id_categoria"), rs.getString("CATEGORIA.nombre"));
                listaEventos.add(e);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Devolverá el último ID asignado en la tabla de Evento o 0 si la
     * tabla está vacía.
     * 
     * @return el último ID asignado en la tabla de Evento o 0 si la tabla está
     *         vacía.
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
     * Almacenará en la BD el evento actual como un evento nuevo (si aun no
     * existía) o modificando un evento existente (si ya existía). Devolverá 0 si no
     * se ha podido almacenar nada o 1 si se ha almacenado correctamente un
     * registro.
     * 
     * @return 0 si no se ha podido almacenar nada o 1 si se ha almacenado
     *         correctamente un registro.
     * 
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
     * Eliminará de la BD el evento actual. Devolverá 0 si no se ha borrado
     * nada o 1 si se ha borrado el evento.
     * 
     * @return 0 si no se ha borrado nada o 1 si se ha borrado el evento.
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
     * Devolverá la categoría a la que pertenece el evento.
     * 
     * @return la categoría a la que pertenece el evento.
     * 
     */
    public Categoria getCategoria() {
        Categoria c = Categoria.get(getId_categoria());
        return c;
    }

}
