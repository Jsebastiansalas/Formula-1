package com.formula1.almacenamiento;

import java.util.List;

import com.formula1.simulacion.SimuladorClasificacion.ResultadoVuelta;

/**
 * Modelo de datos para una sesion de carrera guardada en el historial.
 * Almacena circuito, clima, fecha y la clasificacion completa.
 * Se serializa/deserializa a JSON mediante Gson para persistencia en disco.
 */
public class SesionResultado {

    /** Nombre del circuito donde se disputo la sesion */
    private String circuito;
    /** Condicion climatica durante la sesion (SECO, LLUVIOSO, EXTREMO) */
    private String clima;
    /** Fecha y hora de la sesion en formato "yyyy-MM-dd HH:mm:ss" */
    private String fecha;
    /** Clasificacion completa ordenada por posicion */
    private List<ResultadoVuelta> clasificacion;

    /**
     * Crea un resultado de sesion con todos sus datos.
     * @param circuito nombre del circuito
     * @param clima condicion climatica como string
     * @param fecha fecha de la sesion
     * @param clasificacion resultados ordenados
     */
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
