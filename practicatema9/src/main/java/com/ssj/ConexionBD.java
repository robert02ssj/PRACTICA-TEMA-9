package com.ssj;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3307/PracticaProg";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";

    public static Connection getConection() {
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
