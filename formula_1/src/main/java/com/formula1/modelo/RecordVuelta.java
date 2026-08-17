package com.formula1.modelo;

public class RecordVuelta {
    private String tiempo;
    private String piloto;
    private int anio;

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