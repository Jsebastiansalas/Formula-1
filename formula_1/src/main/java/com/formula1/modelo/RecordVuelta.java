package com.formula1.modelo;

/**
 * Representa el record de vuelta rapida de un circuito.
 * Almacena el tiempo, el piloto que lo logro y el anio.
 * Clase inmutable (solo tiene getters, sin setters).
 */
public class RecordVuelta {

    /** Tiempo del record en formato String (ej: "1:29.525") */
    private String tiempo;
    /** Nombre del piloto que establecio el record */
    private String piloto;
    /** Anio en que se establecio el record */
    private int anio;

    /**
     * Crea un nuevo record de vuelta.
     * @param tiempo tiempo del record como cadena
     * @param piloto piloto que logro el record
     * @param anio anio del record
     */
    public RecordVuelta(String tiempo, String piloto, int anio) {
        this.tiempo = tiempo;
        this.piloto = piloto;
        this.anio = anio;
    }

    public String getTiempo() { return tiempo; }
    public String getPiloto() { return piloto; }
    public int getAnio() { return anio; }

    @Override
    public String toString() {
        return String.format("%s (%s, %d)", tiempo, piloto, anio);
    }
}