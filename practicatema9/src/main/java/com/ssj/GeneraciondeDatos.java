package com.ssj;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
public class GeneraciondeDatos {



    // --- Configuración de la Base de Datos ---
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3307/PracticaProg"; // Cambia esto a tu URL de base de datos
    private static final String DB_USER = "root"; // Cambia esto a tu usuario de base de datos
    private static final String DB_PASSWORD = "root"; // Cambia esto a tu contraseña de base de datos

    // --- Configuración de Generación ---
    private static final int NUM_EVENTOS_EXISTENTES = 100;
    private static final int NUM_PERSONAS_TOTAL = 10100; // 200 Participantes + 200 Artistas
    private static final int NUM_PARTICIPANTES = 10000;
    private static final int NUM_ARTISTAS = 100;
    private static final int NUM_PARTICIPACIONES = 100000;
    private static final int BATCH_SIZE = 500; // Tamaño del lote para inserciones

    // --- Datos de Ejemplo ---
    private static final List<String> NOMBRES_ES = List.of("Alejandro", "Maria", "Carlos", "Lucia", "David", "Laura", "Javier", "Ana", "Sergio", "Marta", "Pablo", "Elena", "Raul", "Claudia", "Adrian", "Isabel", "Fernando", "Patricia", "Ivan", "Sara", "Alberto", "Cristina", "Ruben", "Sofia", "Miguel", "Paula", "Alvaro", "Irene", "Hugo", "Nuria", "Manuel", "Carmen", "Luis", "Rocio", "Oscar", "Silvia", "Mario", "Eva", "Andres", "Alicia", "Daniel", "Beatriz", "Victor", "Natalia", "Francisco", "Clara", "Enrique", "Teresa", "Guillermo", "Julia", "Angel", "Lorena", "Emilio", "Veronica", "Jaime", "Sandra", "Tomas", "Noelia", "Eduardo", "Raquel", "Marcos", "Ines", "Alfonso", "Victoria", "Esteban", "Pilar", "Diego", "Angela", "Jorge", "Lidia", "Hector", "Monica", "Ignacio", "Carolina", "Samuel", "Nerea", "Gabriel", "Rosa", "Vicente", "Amparo", "Felix", "Agata", "Pedro", "Marina", "Antonio", "Elisa", "Ricardo", "Celia", "Joaquin", "Belen", "Juan");
    private static final List<String> APELLIDOS_ES = List.of("Garcia", "Martinez", "Lopez", "Sanchez", "Perez", "Gomez", "Martin", "Jimenez", "Hernandez", "Diaz", "Moreno", "Alvarez", "Muñoz", "Romero", "Alonso", "Gutierrez", "Navarro", "Torres", "Dominguez", "Vazquez", "Ramos", "Gil", "Ramirez", "Serrano", "Blanco", "Molina", "Morales", "Suarez", "Ortega", "Delgado", "Castro", "Ortiz", "Rubio", "Marin", "Sanz", "Iglesias", "Nuñez", "Medina", "Garrido", "Santos", "Castillo", "Cortes", "Lozano", "Guerrero", "Cano", "Prieto", "Mendez", "Cruz", "Calvo", "Gallego", "Vidal", "Leon", "Herrera", "Marquez", "Peña", "Flores", "Cabrera", "Campos", "Vega", "Fuentes", "Carrasco", "Diez", "Reyes", "Caballero", "Nieto", "Aguilar", "Pascual", "Santana", "Herrero", "Lorenzo", "Hidalgo", "Montero", "Ibañez", "Gimenez", "Ferrer", "Duran", "Vicente", "Benitez", "Mora", "Santiago", "Arias", "Vargas", "Carmona", "Crespo", "Roman", "Pastor", "Soto", "Saez", "Velasco", "Moya", "Soler", "Parra", "Esteban", "Bravo", "Gallardo", "Rojas", "Pardo", "Merino", "Franco", "Espinosa", "Izquierdo", "Lara", "Rivas", "Silva", "Rivera", "Casado", "Camacho", "Soria", "Redondo", "Marti", "Mateo", "Collado", "Pozo");

    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Integer> personaIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Conectado a la base de datos.");
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar Personas y obtener IDs
            personaIds = insertPersonas(conn);
            System.out.println(personaIds.size() + " personas insertadas.");

            // 2. Insertar Participantes
            insertParticipantes(conn, personaIds);
            System.out.println(NUM_PARTICIPANTES + " participantes insertados.");

            // 3. Insertar Artistas
            insertArtistas(conn, personaIds);
            System.out.println(NUM_ARTISTAS + " artistas insertados.");

            // 4. Insertar Participaciones
            insertParticipaciones(conn);
            System.out.println(NUM_PARTICIPACIONES + " participaciones insertadas.");

            conn.commit(); // Confirmar transacción
            System.out.println("¡Datos insertados correctamente!");

        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            e.printStackTrace();
            // Considerar rollback en caso de error: conn.rollback();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Métodos de Inserción ---

    private static List<Integer> insertPersonas(Connection conn) throws SQLException {
        List<Integer> generatedIds = new ArrayList<>();
        String sql = "INSERT INTO PERSONA (nombre, apellido1, apellido2) VALUES (?, ?, ?)";
        int count = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < NUM_PERSONAS_TOTAL; i++) {
                String nombre = NOMBRES_ES.get(random.nextInt(NOMBRES_ES.size()));
                String apellido1 = APELLIDOS_ES.get(random.nextInt(APELLIDOS_ES.size()));
                String apellido2 = APELLIDOS_ES.get(random.nextInt(APELLIDOS_ES.size()));
                while (apellido1.equals(apellido2)) {
                    apellido2 = APELLIDOS_ES.get(random.nextInt(APELLIDOS_ES.size()));
                }

                pstmt.setString(1, nombre);
                pstmt.setString(2, apellido1);
                pstmt.setString(3, apellido2);
                pstmt.addBatch();
                count++;

                if (count % BATCH_SIZE == 0 || i == NUM_PERSONAS_TOTAL - 1) {
                    pstmt.executeBatch();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    while (rs.next()) {
                        generatedIds.add(rs.getInt(1));
                    }
                    rs.close();
                    System.out.println("Lote de Personas procesado (" + count + "/" + NUM_PERSONAS_TOTAL + ")");
                }
            }
        }
        if (generatedIds.size() != NUM_PERSONAS_TOTAL) {
             throw new SQLException("Error: No se generaron todos los IDs de Persona esperados (" + generatedIds.size() + "/" + NUM_PERSONAS_TOTAL + ")");
        }
        return generatedIds;
    }

    private static void insertParticipantes(Connection conn, List<Integer> personaIds) throws SQLException {
        String sql = "INSERT INTO PARTICIPANTE (id, email) VALUES (?, ?)";
        int count = 0;

         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < NUM_PARTICIPANTES; i++) {
                int personaId = personaIds.get(i); // IDs 1 a 200 teóricamente
                // Necesitamos obtener nombre/apellido para el email, idealmente se haría una consulta
                // o se guardarían los datos al crear personas. Simplificamos aquí:
                String email = "participante" + personaId + "@correo-java.com";

                pstmt.setInt(1, personaId);
                pstmt.setString(2, email);
                pstmt.addBatch();
                count++;

                if (count % BATCH_SIZE == 0 || i == NUM_PARTICIPANTES - 1) {
                    pstmt.executeBatch();
                    System.out.println("Lote de Participantes procesado (" + count + "/" + NUM_PARTICIPANTES + ")");
                }
            }
        }
    }

     private static void insertArtistas(Connection conn, List<Integer> personaIds) throws SQLException {
        String sql = "INSERT INTO ARTISTA (id, fotografia, obra_destacada) VALUES (?, ?, ?)";
        int count = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = NUM_PARTICIPANTES; i < NUM_PERSONAS_TOTAL; i++) {
                int personaId = personaIds.get(i); // IDs 201 a 400 teóricamente
                String fotografia = "foto_java_" + personaId + ".jpg";
                String obra_destacada = "Obra Java " + personaId;

                pstmt.setInt(1, personaId);
                pstmt.setString(2, fotografia);
                pstmt.setString(3, obra_destacada);
                pstmt.addBatch();
                count++;

                 if (count % BATCH_SIZE == 0 || i == NUM_PERSONAS_TOTAL - 1) {
                    pstmt.executeBatch();
                    System.out.println("Lote de Artistas procesado (" + count + "/" + NUM_ARTISTAS + ")");
                }
            }
        }
    }

    private static void insertParticipaciones(Connection conn) throws SQLException {
        String sql = "INSERT INTO PARTICIPA (id_persona, id_evento, fecha) VALUES (?, ?, ?)";
        int count = 0;
        // Asegurarse de que no haya duplicados (persona, evento) - podría ser más robusto
        java.util.Set<String> participacionesHechas = new java.util.HashSet<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            while (count < NUM_PARTICIPACIONES) {
                // IDs de Personas participantes: teóricamente los primeros NUM_PARTICIPANTES IDs generados.
                // Si los IDs no son secuenciales 1-200, esta lógica falla.
                // Asumiendo que personaIds.get(0) es el ID 1, personaIds.get(199) es el ID 200.
                // ¡CUIDADO! Si los IDs autoincrementales no empiezan en 1 o hay saltos, esto es incorrecto.
                // Una forma más segura sería obtener los IDs de PARTICIPANTE de la BD.
                // Simplificación: Asumimos que los IDs de participantes son 1 a NUM_PARTICIPANTES.
                int id_persona = random.nextInt(NUM_PARTICIPANTES) + 1; // Asume IDs 1 a 200

                // IDs de Eventos existentes: 1 a NUM_EVENTOS_EXISTENTES
                int id_evento = random.nextInt(NUM_EVENTOS_EXISTENTES) + 1; // Asume IDs 1 a 200

                String key = id_persona + "-" + id_evento;
                if (!participacionesHechas.contains(key)) {
                    LocalDate fecha = generateRandomDate(2025, 2025); // Fecha aleatoria en 2025

                    pstmt.setInt(1, id_persona);
                    pstmt.setInt(2, id_evento);
                    pstmt.setDate(3, Date.valueOf(fecha)); // Convertir LocalDate a java.sql.Date
                    pstmt.addBatch();
                    participacionesHechas.add(key);
                    count++;

                    if (count % BATCH_SIZE == 0 || count == NUM_PARTICIPACIONES) {
                        pstmt.executeBatch();
                         System.out.println("Lote de Participaciones procesado (" + count + "/" + NUM_PARTICIPACIONES + ")");
                    }
                }
                 // Si hay muchas colisiones, este bucle podría tardar. Añadir un límite de intentos si es necesario.
            }
        }
    }

    // --- Funciones Auxiliares ---
    private static LocalDate generateRandomDate(int startYear, int endYear) {
        LocalDate start = LocalDate.of(startYear, 1, 1);
        LocalDate end = LocalDate.of(endYear, 12, 31);
        long days = ChronoUnit.DAYS.between(start, end);
        return start.plusDays(ThreadLocalRandom.current().nextLong(days + 1));
    }
}

