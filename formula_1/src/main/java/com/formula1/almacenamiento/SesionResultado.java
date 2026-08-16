package com.formula1.almacenamiento;

import java.util.List;

import com.formula1.simulacion.SimuladorClasificacion.ResultadoVuelta;

public class SesionResultado {
    private String circuito;
    private String clima;
    private String fecha;
    private List<ResultadoVuelta> clasificacion;

    public SesionResultado(String circuito, String clima, String fecha, List<ResultadoVuelta> clasificacion) {
        this.circuito = circuito;
        this.clima = clima;
        this.fecha = fecha;
        this.clasificacion = clasificacion;
    }

    public String getCircuito() { return circuito; }
    public String getClima() { return clima; }
    public String getFecha() { return fecha; }
    public List<ResultadoVuelta> getClasificacion() { return clasificacion; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Sesión en %s | Clima: %s | %s%n", circuito, clima, fecha));
        for (int i = 0; i < clasificacion.size(); i++) {
            sb.append(String.format("  %d. %s%n", i + 1, clasificacion.get(i)));
        }
        return sb.toString();
    }
}   
