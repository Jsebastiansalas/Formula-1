package com.formula1.modelo;

public class Circuito {
    private String nombre;
    private String pais;
    private double longitudKm;
    private int vueltas;
    private String descripcion;
    private RecordVuelta recordVuelta;

    public Circuito(String nombre, String pais, double longitudKm, int vueltas,
                     String descripcion, RecordVuelta recordVuelta) {
        this.nombre = nombre;
        this.pais = pais;
        this.longitudKm = longitudKm;
        this.vueltas = vueltas;
        this.descripcion = descripcion;
        this.recordVuelta = recordVuelta;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public double getLongitudKm() { return longitudKm; }
    public int getVueltas() { return vueltas; }
    public String getDescripcion() { return descripcion; }
    public RecordVuelta getRecordVuelta() { return recordVuelta; }

    // Setters (para editar en el CRUD)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPais(String pais) { this.pais = pais; }
    public void setLongitudKm(double longitudKm) { this.longitudKm = longitudKm; }
    public void setVueltas(int vueltas) { this.vueltas = vueltas; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setRecordVuelta(RecordVuelta recordVuelta) { this.recordVuelta = recordVuelta; }

    @Override
    public String toString() {
        return String.format("%s (%s) | %.2f km | %d vueltas | Récord: %s",
                nombre, pais, longitudKm, vueltas, recordVuelta);
    }
}
