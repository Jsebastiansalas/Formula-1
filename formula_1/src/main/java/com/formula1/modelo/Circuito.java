package com.formula1.modelo;

/**
 * Representa un circuito de Formula 1 con sus caracteristicas fisicas.
 * Almacena nombre, pais, longitud en km, numero de vueltas, descripcion
 * y el record de vuelta historico del circuito.
 * Se deserializa desde data/circuitos.json mediante Gson.
 */
public class Circuito {

    /** Nombre oficial del circuito (ej: "Circuit de Monaco") */
    private String nombre;
    /** Pais donde se ubica el circuito */
    private String pais;
    /** Longitud total del circuito en kilometros */
    private double longitudKm;
    /** Numero de vueltas que se corren en una carrera */
    private int vueltas;
    /** Descripcion textual del circuito */
    private String descripcion;
    /** Record historico de vuelta rapida en este circuito */
    private RecordVuelta recordVuelta;

    /**
     * Constructor completo para crear un circuito con todos sus datos.
     * @param nombre nombre oficial del circuito
     * @param pais pais de ubicacion
     * @param longitudKm longitud en kilometros
     * @param vueltas numero de vueltas por carrera
     * @param descripcion descripcion del circuito
     * @param recordVuelta record de vuelta rapida
     */
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
