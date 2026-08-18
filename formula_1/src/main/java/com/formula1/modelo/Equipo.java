package com.formula1.modelo;
import java.util.List;
import java.util.ArrayList;

public class Equipo {
    private String nombre;
    private String pais;
    private String motor;
    private List<Integer> pilotos;

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
