package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Participante extends Persona {
    private IntegerProperty id;
    private StringProperty email;

    public Participante(int id, String nombre, String apellido1, String apellido2, String email) {
        super(id, nombre, apellido1, apellido2);
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void participa(int idEvento, int idPersona, String fecha) {

    }

    /**
     * Devolverá un array con todos los participantes de la BD o null si no hay
     * ningún
     * participante.
     * 
     * @param listaParticipantes Lista donde se almacenarán los participantes.
     */
    public static void getAll(ObservableList<Persona> listaParticipantes) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st
                    .executeQuery("SELECT * FROM PARTICIPANTE INNER JOIN PERSONA ON PARTICIPANTE.id = PERSONA.id ORDER BY PERSONA.id");

            while (rs.next()) {
                listaParticipantes.add(new Participante(rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellido1"), rs.getString("apellido2"), rs.getString("email")));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * Devolverá un participante con el id especificado o null si no existe.
     * 
     * @param id El id del participante a buscar.
     * @return El participante encontrado o null si no existe.
     */
    public Participante get(int id) {
        Connection con = ConexionBD.getConection();
        Participante p = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPANTE WHERE id = " + id + " INNER JOIN PERSONA ON PARTICIPANTE.id = PERSONA.id");
            if (rs.next()) {
                p = new Participante(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getString("email"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return p;
    }

    /**
     * Devolverá el último ID asignado en la tabla de Participante o 0 si la tabla
     * está vacía.
     * 
     * @return El último ID asignado en la tabla de Participante o 0 si la tabla
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
     * Almacenará en la BD el participante actual como un participante nuevo (si aún
     * no existía) o modificando un participante existente (si ya existía).
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
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPANTE WHERE id = " + this.getId());
            if (rs.next()) {
                // Modificar
                st.executeUpdate(
                        "UPDATE PARTICIPANTE SET email = '" + this.getEmail() + "' WHERE id = " + this.getId());
                st.executeUpdate("UPDATE PERSONA SET nombre = '" + this.getNombre() + "', apellido1 = '"
                        + this.getApellido1() + "', apellido2 = '" + this.getApellido2() + "' WHERE id = "
                        + this.getId());
                resultado = 1;
            } else {
                // Insertar
                st.executeUpdate("INSERT INTO PERSONA (id, nombre, apellido1, apellido2) VALUES (" + this.getId()
                + ", '" + this.getNombre() + "', '" + this.getApellido1() + "', '" + this.getApellido2()
                + "')");
                st.executeUpdate(
                        "INSERT INTO PARTICIPANTE (id,email) VALUES (" + this.getId() + ", '" + this.getEmail() + "')");
               
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
     * Eliminará de la BD el participante actual. Devolverá 0 si no se ha borrado
     * nada o 1 si se ha borrado el participante.
     * 
     * @return 0 si no se ha borrado nada o 1 si se ha borrado el participante.
     */
    public int delete() {
        int resultado = 0;
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPANTE WHERE id = " + this.getId());
            if (rs.next()) {
                st.executeUpdate("DELETE FROM PARTICIPANTE WHERE id = " + this.getId());
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
/**
 * * Almacenará en la BD la participación de un participante en un evento en la tabla participa. Si ya
 * existía la participación, la modificará. Devolverá 0 si no se ha podido
 * @param idEvento
 * @param idPersona
 * @param fecha
 */
    public void Participa(int idEvento, int idPersona, String fecha) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPA WHERE id_evento = " + idEvento + " AND id_persona = " + idPersona);
            if (rs.next()) {
                // Modificar
                st.executeUpdate("UPDATE PARTICIPA SET fecha = '" + fecha + "' WHERE id_evento = " + idEvento + " AND id_persona = " + idPersona);
            } else {
                // Insertar
                st.executeUpdate("INSERT INTO PARTICIPA (id_evento, id_persona, fecha) VALUES (" + idEvento + ", " + idPersona + ", '" + fecha + "')");
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }
}
