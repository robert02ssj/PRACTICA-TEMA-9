package com.ssj;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import java.sql.*;

public class Categoria {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty descripcion;

    public Categoria(int id, String nombre, String descripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    /**
     * Obtiene el ID de la categoría.
     * 
     * @return el ID de la categoría.
     */
    public int getId() {
        return id.get();
    }
    
    /**
     * Establece el ID de la categoría.
     * 
     * @param id el nuevo ID de la categoría.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Obtiene el nombre de la categoría.
     * 
     * @return el nombre de la categoría.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Establece el nombre de la categoría.
     * 
     * @param nombre el nuevo nombre de la categoría.
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * Obtiene la descripción de la categoría.
     * 
     * @return la descripción de la categoría.
     */
    public String getDescripcion() {
        return descripcion.get();
    }

    /**
     * Establece la descripción de la categoría.
     * 
     * @param descripcion la nueva descripción de la categoría.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    /**
     * Obtiene todas las categorías de la base de datos y las añade a la lista proporcionada.
     * 
     * @param listaCategorias la lista donde se añadirán las categorías obtenidas.
     */
    public static void getAll(ObservableList<Categoria> listaCategorias) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM CATEGORIA");
            while (rs.next()) {
                listaCategorias.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Obtiene una categoría de la base de datos por su ID.
     * 
     * @param id el ID de la categoría a buscar.
     * @return la categoría encontrada o null si no existe.
     */
    public Categoria get(int id) {
        Connection con = ConexionBD.getConection();
        Categoria c = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE id = " + id);
            if (rs.next()) {
                c = new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return c;
    }

    /**
     * Obtiene el último ID asignado en la tabla de categorías o 0 si la tabla está vacía.
     * 
     * @return el último ID asignado o 0 si la tabla está vacía.
     */
    public int getLastId() {
        Connection con = ConexionBD.getConection();
        int id = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(id) FROM CATEGORIA");
            if (rs.next()) {
                id = rs.getInt(1);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return id;
    }

    /**
     * Guarda la categoría actual en la base de datos. Si ya existe, la actualiza; si no, la inserta.
     * 
     * @return 1 si se guardó correctamente, 0 en caso contrario.
     */
    public int save() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE id = " + this.getId());
            if (rs.next()) {
                // Modificar
                st.executeUpdate("UPDATE CATEGORIA SET nombre = '" + this.getNombre() + "', descripcion = '"
                        + this.getDescripcion() + "' WHERE id = " + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate(
                        "INSERT INTO CATEGORIA (id, nombre, descripcion) VALUES ("
                                + this.getId() + ", '" + this.getNombre() + "', '" + this.getDescripcion() + "')");
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return resultado;
    }

    /**
     * Elimina la categoría actual de la base de datos.
     * 
     * @return 1 si se eliminó correctamente, 0 en caso contrario.
     */
    public int delete() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE id = " + this.getId());
            if (rs.next()) {
                st.executeUpdate("DELETE FROM CATEGORIA WHERE id = " + this.getId());
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return resultado;
    }
}