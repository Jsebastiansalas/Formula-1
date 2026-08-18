package com.formula1.modelo;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa una escuderia (equipo) de Formula 1.
 * Almacena nombre, pais de origen, proveedor de motor y la lista de IDs
 * de pilotos que pertenecen al equipo.
 * Se deserializa desde data/equipos.json mediante Gson.
 */
public class Equipo {

    /** Nombre oficial de la escuderia */
    private String nombre;
    /** Pais de origen del equipo */
    private String pais;
    /** Proveedor de la unidad de potencia */
    private String motor;
    /** Lista de IDs de pilotos que pertenecen a este equipo */
    private List<Integer> pilotos;

    /** Constructor vacio requerido por Gson, inicializa la lista de pilotos */
    public Equipo() { this.pilotos = new ArrayList<>(); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getMotor() { return motor; }
    public void setMotor(String motor) { this.motor = motor; }
    public List<Integer> getPilotos() { return pilotos; }
    public void setPilotos(List<Integer> pilotos) { this.pilotos = pilotos; }

    @Override
    public String toString() {
        return String.format("%s (%s) - Motor: %s", nombre, pais, motor);
    }
}
