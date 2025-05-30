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
 * Clase que representa a un participante, que hereda de Persona e implementa la interfaz Exportable.
 */
public class Participante extends Persona implements Exportable {
    /**
     * Identificador único del participante.
     */
    private IntegerProperty id;

    /**
     * Correo electrónico del participante.
     */
    private StringProperty email;

    /**
     * Constructor de la clase Participante.
     * 
     * @param id        Identificador único del participante.
     * @param nombre    Nombre del participante.
     * @param apellido1 Primer apellido del participante.
     * @param apellido2 Segundo apellido del participante.
     * @param email     Correo electrónico del participante.
     */
    public Participante(int id, String nombre, String apellido1, String apellido2, String email) {
        super(id, nombre, apellido1, apellido2);
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
    }

    /**
     * Obtiene el identificador único del participante.
     * 
     * @return El identificador único del participante.
     */
    public int getId() {
        return id.get();
    }

    /**
     * Establece el identificador único del participante.
     * 
     * @param id El nuevo identificador único del participante.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Obtiene el correo electrónico del participante.
     * 
     * @return El correo electrónico del participante.
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Establece el correo electrónico del participante.
     * 
     * @param email El nuevo correo electrónico del participante.
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * Registra la participación de un participante en un evento.
     * 
     * @param idEvento  Identificador del evento.
     * @param idPersona Identificador del participante.
     * @param fecha     Fecha de la participación.
     */
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
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPANTE INNER JOIN PERSONA ON PARTICIPANTE.id = PERSONA.id WHERE PARTICIPANTE.id = " + id);
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

    public static int get(String txt, ObservableList<Persona> listaParticipantes) {
        Connection con = ConexionBD.getConection();
        int id = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPANTE INNER JOIN PERSONA ON PARTICIPANTE.id = PERSONA.id WHERE MATCH (nombre, apellido1, apellido2) AGAINST ('"+ txt +"*' IN BOOLEAN MODE) ORDER BY PARTICIPANTE.id");
            while (rs.next()) {
                listaParticipantes.add(new Participante(rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellido1"), rs.getString("apellido2"), rs.getString("email")));
                id++;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return id;
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

    /**
     * Elimina la participación de un participante en un evento.
     * 
     * @param idEvento  Identificador del evento.
     * @param idPersona Identificador del participante.
     */
    public void eliminarParticipacion(int idEvento, int idPersona) {
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM PARTICIPA WHERE id_evento = " + idEvento + " AND id_persona = " + idPersona);
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
    }

    /**
     * * Método para exportar a texto los datos de un artista .
     */
    @Override
    public void exportToText(ObservableList<?> listaParticipantes) {
        // Implementación para exportar a texto
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId()).append("\n");
        sb.append("Nombre: ").append(getNombre()).append("\n");
        sb.append("Apellido 1: ").append(getApellido1()).append("\n");
        sb.append("Apellido 2: ").append(getApellido2()).append("\n"); 
        sb.append("Email: ").append(getEmail()).append("\n");
        sb.append("Eventos:\n");
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPA INNER JOIN EVENTO ON PARTICIPA.id_evento = EVENTO.id WHERE PARTICIPA.id_persona = " + getId());
            while (rs.next()) {
            sb.append("  - Evento ID: ").append(rs.getInt("id_evento"))
              .append(", Nombre: ").append(rs.getString("nombre"))
              .append(", Fecha: ").append(rs.getString("fecha"))
              .append("\n");
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        try (FileWriter writer = new FileWriter("Participante_" + getId() + ".txt")) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error al exportar a texto: " + e.getMessage());
        }

    }

    /**
     * Exporta los datos del participante a un archivo PDF.
     * (Implementación pendiente).
     */
    @Override
    public void exportToPDF() {

    }
}
