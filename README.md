# Gestor de Eventos - Convención Friki

## Descripción

Este proyecto es una aplicación de gestión de eventos diseñada para una convención de cómics, videojuegos y películas. Permite administrar eventos, artistas y participantes, almacenando toda la información en una base de datos relacional.

## Funcionalidades

- **Gestión de Personas**:
  - Almacenar, modificar y eliminar información de participantes y artistas.
  - Consultar la lista de personas registradas.
- **Gestión de Eventos**:
  - Crear, modificar y eliminar eventos.
  - Consultar eventos por nombre o descripción.
- **Participación en Eventos**:
  - Registrar la participación de personas en eventos.
  - Consultar los eventos a los que una persona está inscrita.

## Estructura del Proyecto

- **`Persona`**: Clase abstracta que define los atributos y métodos comunes para participantes y artistas.
- **`Participante`**: Subclase de `Persona` que incluye información adicional como el correo electrónico.
- **`Artista`**: Subclase de `Persona` que incluye atributos como fotografía y obra destacada.
- **`Evento`**: Clase que gestiona los eventos, incluyendo su nombre, descripción, lugar y fechas.

## Requisitos

- **Java**: Versión 8 o superior.
- **Base de Datos**: Configuración de una base de datos relacional con las tablas `PERSONA`, `PARTICIPANTE`, `ARTISTA` y `EVENTO`.
- **Librerías**:
  - `javafx.beans.property`
  - `java.sql`
  - `javafx.collections`

## Configuración

1. Configurar la conexión a la base de datos en la clase `ConexionBD`.
2. Crear las tablas necesarias en la base de datos:

   ```sql
   CREATE TABLE PERSONA (
       id INT PRIMARY KEY,
       nombre VARCHAR(50),
       apellido1 VARCHAR(50),
       apellido2 VARCHAR(50)
   );

   CREATE TABLE PARTICIPANTE (
       id INT PRIMARY KEY,
       nombre VARCHAR(50),
       apellido1 VARCHAR(50),
       apellido2 VARCHAR(50),
       email VARCHAR(100)
   );

   CREATE TABLE ARTISTA (
       id INT PRIMARY KEY,
       nombre VARCHAR(50),
       apellido1 VARCHAR(50),
       apellido2 VARCHAR(50),
       fotografia VARCHAR(255),
       obra_destacada VARCHAR(255)
   );

   CREATE TABLE EVENTO (
       id INT PRIMARY KEY,
       nombre VARCHAR(100),
       descripcion TEXT,
       lugar VARCHAR(100),
       fecha DATE,
       fecha_inicio DATE,
       fecha_fin DATE,
       id_categoria INT
   );
   ```
