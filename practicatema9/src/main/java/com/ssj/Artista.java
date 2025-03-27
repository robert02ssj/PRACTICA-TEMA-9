package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Artista extends Persona {
    private IntegerProperty id;
    private StringProperty fotografia;
    private StringProperty obra_destacada;

    public Artista(int id, String nombre, String apellido1, String apellido2, String fotografia,
            String obra_destacada) {
        super(id, nombre, apellido1, apellido2);
        this.id = new SimpleIntegerProperty(id);
        this.fotografia = new SimpleStringProperty(fotografia);
        this.obra_destacada = new SimpleStringProperty(obra_destacada);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFotografia() {
        return fotografia.get();
    }

    public void setFotografia(String fotografia) {
        this.fotografia.set(fotografia);
    }

    public String getObraDestacada() {
        return obra_destacada.get();
    }

    public void setObraDestacada(String obra_destacada) {
        this.obra_destacada.set(obra_destacada);
    }

    public void participa(int idEvento, int idPersona, String fecha) {

    }

    /**
     * Devolverá un array con todos los artistas de la BD o null si no hay ningún
     * artista.
     * 
     * @param listaArtistas Lista donde se almacenarán los artistas.
     */
    public static void getAll(ObservableList<Persona> listaArtistas) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ARTISTA INNER JOIN PERSONA ON ARTISTA.id = PERSONA.id");
            while (rs.next()) {
                listaArtistas.add(new Artista(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getString("fotografia"), rs.getString("obra_destacada")));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Devolverá un artista con el id especificado o null si no existe.
     * 
     * @param id El id del artista a buscar.
     * @return El artista encontrado o null si no existe.
     */
    public Artista get(int id) {
        Connection con = ConexionBD.getConection();
        Artista a = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM ARTISTA WHERE id = " + id + " INNER JOIN PERSONA ON ARTISTA.id = PERSONA.id");
            if (rs.next()) {
                a = new Artista(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getString("fotografia"), rs.getString("obra_destacada"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return a;
    }

    /**
     * Devolverá el último ID asignado en la tabla de Artista o 0 si la tabla
     * está vacía.
     * 
     * @return El último ID asignado en la tabla de Artista o 0 si la tabla
     *         está vacía.
     */
    public static int getLastId() {
        Connection con = ConexionBD.getConection();
        int id = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(id) FROM PERSONA");
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
     * Almacenará en la BD el artista actual como un artista nuevo (si aún
     * no existía) o modificando un artista existente (si ya existía).
     * Devolverá 0 si no se ha podido almacenar nada o 1 si se ha almacenado
     * correctamente un registro.
     * 
     * @return 0 si no se ha podido almacenar nada o 1 si se ha almacenado
     *         correctamente un registro.
     */
    public int save() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ARTISTA WHERE id = " + this.getId());
            if (rs.next()) {
                // Modificar
                st.executeUpdate("UPDATE PERSONA SET nombre = '" + this.getNombre() + "', apellido1 = '"
                        + this.getApellido1() + "', apellido2 = '" + this.getApellido2() + "' WHERE id = "
                        + this.getId());
                st.executeUpdate("UPDATE ARTISTA SET fotografia = '" + this.getFotografia() + "', obra_destacada = '"
                        + this.getObraDestacada() + "' WHERE id = " + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate(
                        "INSERT INTO ARTISTA (id, fotografia, obra_destacada) VALUES ("
                                + this.getId() + "', '" + this.getFotografia() + "', '"
                                + this.getObraDestacada() + "')");
                
                st.executeUpdate("INSERT INTO PERSONA (id, nombre, apellido1, apellido2) VALUES (" + this.getId()
                        + ", '" + this.getNombre() + "', '" + this.getApellido1() + "', '" + this.getApellido2()
                        + "')");
                
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
     * Eliminará de la BD el artista actual. Devolverá 0 si no se ha borrado
     * nada o 1 si se ha borrado el artista.
     * 
     * @return 0 si no se ha borrado nada o 1 si se ha borrado el artista.
     */
    public int delete() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ARTISTA WHERE id = " + this.getId());
            if (rs.next()) {
                st.executeUpdate("DELETE FROM ARTISTA WHERE id = " + this.getId());
                st.executeUpdate("DELETE FROM PERSONA WHERE id = " + this.getId());
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
            resultado = 0;
        }
        return resultado;
    }
}
