package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.ArrayList;
import java.util.Observable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public abstract class Persona {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty apellido1;
    private StringProperty apellido2;

    public Persona(int id, String nombre, String apellido1, String apellido2) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido1 = new SimpleStringProperty(apellido1);
        this.apellido2 = new SimpleStringProperty(apellido2);
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

    public String getApellido1() {
        return apellido1.get();
    }

    public void setApellido1(String apellido1) {
        this.apellido1.set(apellido1);
    }

    public String getApellido2() {
        return apellido2.get();
    }

    public void setApellido2(String apellido2) {
        this.apellido2.set(apellido2);
    }

    

    /**
     * Devolverá una persona con el id especificado o null si no existe.
     * 
     * @param id El id de la persona a buscar.
     * @return La persona encontrada o null si no existe.
     */
    public Persona get(int id) {
        Connection con = ConexionBD.getConection();
        Persona p = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PERSONA WHERE id = " + id);
            if (rs.next()) {
                //p = new Persona(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
                //     rs.getString("apellido2"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return p;
    }

    /**
     * Devolverá el último ID asignado en la tabla de Persona o 0 si la tabla está
     * vacía.
     * 
     * @return El último ID asignado en la tabla de Persona o 0 si la tabla está
     *         vacía.
     */
    public static  int getLastId() {
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
     * Almacenará en la BD la persona actual como una nueva persona (si aún no
     * existía)
     * o modificará una persona existente (si ya existía).
     * 
     * @return 0 si no se ha podido almacenar nada o 1 si se ha almacenado
     *         correctamente.
     */
    public int save(){
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PERSONA WHERE id = " + this.getId());
            if (rs.next()) {
                // Modificar
                st.executeUpdate("UPDATE PERSONA SET nombre = '" + this.getNombre() + "', apellido1 = '"
                        + this.getApellido1() + "', apellido2 = '" + this.getApellido2() + "' WHERE id = "
                        + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate(
                        "INSERT INTO PERSONA (id, nombre, apellido1, apellido2) VALUES ("
                                + this.getId() + ", '" + this.getNombre() + "', '" + this.getApellido1() + "', '"
                                + this.getApellido2() + "')");
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return resultado;
    }

    /**
     * Eliminará de la BD la persona actual.
     * 
     * @return 0 si no se ha borrado nada o 1 si se ha borrado la persona.
     */
    public int delete() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PERSONA WHERE id = " + this.getId());
            if (rs.next()) {
                st.executeUpdate("DELETE FROM PERSONA WHERE id = " + this.getId());
                resultado = 1;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return resultado;
    }

    /**
     * Devolverá la lista de eventos a los que una persona está apuntada. mirando la tabla Participa
     * 
     */
    public ArrayList<Evento> getEventos() {
        Connection con = ConexionBD.getConection();
        ArrayList<Evento> listaEventos = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_evento FROM PARTICIPA WHERE id_persona = " + this.getId());
            while (rs.next()) {
                Evento e = new Evento(rs.getInt("id_evento"), "", "", "", "0000-00-00", "0000-00-00", 1, "");
                e.get(rs.getInt("id_evento"));
                listaEventos.add(e);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return listaEventos;
    }

    /**
     * Método abstracto para
     * almacenar la participación de una persona en un evento
     */
    public abstract void Participa(int idEvento, int idPersona, String fecha);
}
