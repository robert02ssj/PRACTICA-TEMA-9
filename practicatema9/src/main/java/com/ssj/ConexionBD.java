package com.ssj;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionBD {
    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASSWORD;

    /**
     * Establece la conexión a la base de datos utilizando los parámetros de configuración
     * especificados en el archivo config.properties.
     *
     * @return la conexión a la base de datos.
     */
    public static Connection getConection() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            DB_URL = properties.getProperty("DB_URL");
            DB_USER = properties.getProperty("DB_USER");
            DB_PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de configuración: " + e.getMessage());
        }
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Conexión exitosa");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
