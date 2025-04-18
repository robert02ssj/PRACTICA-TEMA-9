package com.ssj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Artista extends Persona implements Exportable {
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

    
    /** Este método devuelve el id del artista.
     * @return int
     */
    public int getId() {
        return id.get();
    }
    /** Este método establece el id del artista.
     * @param id
     */
    public void setId(int id) {
        this.id.set(id);
    }
    /** Este método devuelve la fotografia del artista.
     * @return String
     */
    public String getFotografia() {
        return fotografia.get();
    }
    /** Este método establece la fotografia del artista.
     * @param fotografia
     */
    public void setFotografia(String fotografia) {
        this.fotografia.set(fotografia);
    }
    /** Este método devuelve la obra destacada del artista.
     * @return String
     */
    public String getObraDestacada() {
        return obra_destacada.get();
    }
    /** Este método establece la obra destacada del artista.
     * @param obra_destacada
     */
    public void setObraDestacada(String obra_destacada) {
        this.obra_destacada.set(obra_destacada);
    }
    /** Este método gestiona la participacion del artista.
     * 
     */
    @Override
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
    /** Este método elimina la participacion del artista.
     * 
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
     * Devolverá un array con todos los artistas que contengan el texto
     * especificado en su nombre o apellidos o 0 si no hay ningún artista.
     * 
     * @param txt          Texto a buscar en el nombre o apellidos del artista.
     * @param listaArtistas Lista donde se almacenarán los artistas encontrados.
     * @return El número de artistas encontrados.
     */
    public static int get(String txt, ObservableList<Persona> listaArtistas) {
        Connection con = ConexionBD.getConection();
        int id = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM ARTISTA INNER JOIN PERSONA ON ARTISTA.id = PERSONA.id WHERE nombre LIKE '%" + txt
                            + "%' OR apellido1 LIKE '%" + txt + "%' OR apellido2 LIKE '%" + txt + "%'");
            while (rs.next()) {
                listaArtistas.add(new Artista(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getString("fotografia"), rs.getString("obra_destacada")));
                id++;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL " + e);
        }
        return id;
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
                System.out.println("INSERT INTO PERSONA (id, nombre, apellido1, apellido2) VALUES (" + this.getId()
                        + ", '" + this.getNombre() + "', '" + this.getApellido1() + "', '" + this.getApellido2()
                        + "')");
                st.executeUpdate("INSERT INTO PERSONA (id, nombre, apellido1, apellido2) VALUES (" + this.getId()
                        + ", '" + this.getNombre() + "', '" + this.getApellido1() + "', '" + this.getApellido2()
                        + "')");
                        System.out.println("INSERT INTO ARTISTA (id, fotografia, obra_destacada) VALUES ("
                                + this.getId() + ", '" + this.getFotografia() + "', '"
                                + this.getObraDestacada() + "')");
                st.executeUpdate(
                        "INSERT INTO ARTISTA (id, fotografia, obra_destacada) VALUES ("
                                + this.getId() + ", '" + this.getFotografia() + "', '"
                                + this.getObraDestacada() + "')");
                
                
                
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
/**
 * * Exporta los datos del artista a un archivo de texto.
 */
    @Override
    public void exportToText(ObservableList<?> listaArtistas) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId()).append("\n");
        sb.append("Nombre: ").append(getNombre()).append("\n");
        sb.append("Apellido 1: ").append(getApellido1()).append("\n");
        sb.append("Apellido 2: ").append(getApellido2()).append("\n");
        sb.append("Fotografía: ").append(getFotografia()).append("\n");
        sb.append("Obra destacada: ").append(getObraDestacada()).append("\n");
        sb.append("Eventos:\n");
        Connection con = ConexionBD.getConection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EVENTO INNER JOIN PARTICIPA ON EVENTO.id = PARTICIPA.id_evento WHERE PARTICIPA.id_persona = " + getId());
            while (rs.next()) {
            sb.append("  - Evento ID: ").append(rs.getInt("id"))
              .append(", Nombre: ").append(rs.getString("nombre"))
              .append(", Fecha: ").append(rs.getString("fecha"))
              .append("\n");
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error en SQL al obtener eventos: " + e);
        }
        try (FileWriter writer = new FileWriter("artista_" + getId() + ".txt")) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error al exportar a texto: " + e.getMessage());
        }
    }

    @Override
    public void exportToPDF() {

    }
}
