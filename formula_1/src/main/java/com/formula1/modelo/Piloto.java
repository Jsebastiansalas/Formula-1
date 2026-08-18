package com.formula1.modelo;

public class Piloto {
    private int id;
    private String nombre;
    private String equipo;
    private String rol;
    private int experienciaAnios;
    private int habilidad;

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
