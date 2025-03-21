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
    private java.sql.Date fecha;
    private java.sql.Date fecha_inicio;
    private java.sql.Date fecha_fin;
    private IntegerProperty id_categoria;

    public Evento(int id, String nombre, String descripcion, String lugar, java.sql.Date fecha,
            java.sql.Date fecha_inicio, java.sql.Date fecha_fin, int id_categoria) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.lugar = new SimpleStringProperty(lugar);
        this.fecha = fecha;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.id_categoria = new SimpleIntegerProperty(id_categoria);
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

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public java.sql.Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(java.sql.Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public java.sql.Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(java.sql.Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getId_categoria() {
        return id_categoria.get();
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria.set(id_categoria);
    }

    /**
     * Devolverá un array con todos los eventos de la BD o null si no hay ningún
     * evento
     * 
     * @param
     * @return el array de eventos o null si no hay eventos
     */
    public void getAll(ObservableList<Evento> listaEventos) {

        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO");
            while (rs.next()) {
                listaEventos.add(new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getDate("fecha"), rs.getDate("fecha_inicio"), rs.getDate("fecha_fin"),
                        rs.getInt("id_categoria")));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Devolverá un entero con el id del evento que buscamos o null si no existe
     * 
     * @param id
     * @return el id del evento o null si no existe
     */
    public Evento get(int id) {
        Connection con = ConexionBD.getConection();
        Evento e = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO WHERE id = " + id);
            if (rs.next()) {
                e = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"),
                        rs.getDate("fecha"), rs.getDate("fecha_inicio"), rs.getDate("fecha_fin"),
                        rs.getInt("id_categoria"));
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
                    "SELECT * FROM EVENTO WHERE nombre LIKE '%" + txt + "%' OR descripcion LIKE '%" + txt + "%'");
            while (rs.next()) {
                Evento e = new Evento(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getString("lugar"), rs.getDate("fecha"), rs.getDate("fecha_inicio"), rs.getDate("fecha_fin"),
                        rs.getInt("id_categoria"));
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
    public int getLastId() {
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
                        + this.getDescripcion() + "', lugar = '" + this.getLugar() + "', fecha = '" + this.getFecha()
                        + "', fecha_inicio = '" + this.getFecha_inicio() + "', fecha_fin = '" + this.getFecha_fin()
                        + "', id_categoria = " + this.getId_categoria() + " WHERE id = " + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate(
                        "INSERT INTO EVENTO (id, nombre, descripcion, lugar, fecha, fecha_inicio, fecha_fin, id_categoria) VALUES ("
                                + this.getId() + ", '" + this.getNombre() + "', '" + this.getDescripcion() + "', '"
                                + this.getLugar()
                                + "', '" + this.getFecha() + "', '" + this.getFecha_inicio() + "', '"
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
    public Categoria getCategoria(){
       // Connection con = ConexionBD.getConection();
        Categoria c = null;
        
        return c;
    }

}
