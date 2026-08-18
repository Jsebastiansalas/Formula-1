package com.formula1.modelo;

/**
 * Representa un piloto de Formula 1 con sus atributos de rendimiento.
 * Se deserializa desde data/pilotos.json mediante Gson.
 * La habilidad (0-100) es el factor principal que afecta el tiempo de vuelta
 * y la probabilidad de incidentes durante la simulacion.
 */
public class Piloto {

    /** Identificador unico del piloto */
    private int id;
    /** Nombre completo del piloto */
    private String nombre;
    /** Nombre de la escuderia a la que pertenece */
    private String equipo;
    /** Rol dentro del equipo (primer piloto, segundo piloto) */
    private String rol;
    /** Anios de experiencia en Formula 1 */
    private int experienciaAnios;
    /** Nivel de habilidad del piloto (0-100), afecta directamente la simulacion */
    private int habilidad;

    /** Constructor vacio requerido por Gson para la deserializacion */
    public Piloto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public int getExperienciaAnios() { return experienciaAnios; }
    public void setExperienciaAnios(int experienciaAnios) { this.experienciaAnios = experienciaAnios; }
    public int getHabilidad() { return habilidad; }
    public void setHabilidad(int habilidad) { this.habilidad = habilidad; }

    @Override
    public String toString() {
        return String.format("%s (%s) - Habilidad: %d", nombre, equipo, habilidad);
    }
}
