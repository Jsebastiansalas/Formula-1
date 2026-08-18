package com.formula1.modelo;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

/**
 * Representa un vehiculo (monoplaza) de Formula 1 con sus especificaciones tecnicas.
 * Cada vehiculo pertenece a un equipo y tiene un mapa de rendimiento por modo de conduccion
 * (normal, agresiva, ahorro) que incluye velocidad promedio, consumo y desgaste por clima.
 * Se deserializa desde data/vehiculos.json mediante Gson.
 */
public class Vehiculo {

    /** Nombre de la escuderia propietaria del vehiculo */
    private String equipo;
    /** Nombre del modelo (clave unica en el sistema) */
    private String modelo;
    /** Tipo de motor/unidad de potencia */
    private String motor;
    /** Velocidad maxima teorica en km/h */
    private int velocidadMaximaKmh;

    /** Tiempo de aceleracion de 0 a 100 km/h en segundos */
    @SerializedName("aceleracion_0_100")
    private double aceleracion0100;

    /** Lista de IDs de pilotos asignados a este vehiculo */
    private List<Integer> pilotos;
    /** Mapa de rendimiento por modo de conduccion (clave: "conduccion_normal", "conduccion_agresiva", etc.) */
    private Map<String, RendimientoModo> rendimiento;

    /** Constructor vacio requerido por Gson, inicializa la lista de pilotos */
    public Vehiculo() { this.pilotos = new ArrayList<>(); }

    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMotor() { return motor; }
    public void setMotor(String motor) { this.motor = motor; }
    public int getVelocidadMaximaKmh() { return velocidadMaximaKmh; }
    public void setVelocidadMaximaKmh(int velocidadMaximaKmh) { this.velocidadMaximaKmh = velocidadMaximaKmh; }
    public double getAceleracion0100() { return aceleracion0100; }
    public void setAceleracion0100(double aceleracion0100) { this.aceleracion0100 = aceleracion0100; }
    public List<Integer> getPilotos() { return pilotos; }
    public void setPilotos(List<Integer> pilotos) { this.pilotos = pilotos; }
    public Map<String, RendimientoModo> getRendimiento() { return rendimiento; }
    public void setRendimiento(Map<String, RendimientoModo> rendimiento) { this.rendimiento = rendimiento; }

    /**
     * Clase interna que representa el rendimiento de un modo de conduccion especifico.
     * Contiene velocidad promedio fija y mapas de consumo/desgaste que varian segun
     * las condiciones climaticas (seco, lluvioso, extremo).
     */
    public static class RendimientoModo {
        /** Velocidad promedio en este modo de conduccion (km/h) */
        private double velocidadPromedioKmh;
        /** Consumo de combustible por tipo de clima (claves: seco, lluvioso, extremo) */
        private Map<String, Double> consumoCombustible;
        /** Desgaste de neumaticos por tipo de clima (claves: seco, lluvioso, extremo) */
        private Map<String, Double> desgasteNeumaticos;

        public double getVelocidadPromedioKmh() { return velocidadPromedioKmh; }
        public void setVelocidadPromedioKmh(double v) { this.velocidadPromedioKmh = v; }
        public Map<String, Double> getConsumoCombustible() { return consumoCombustible; }
        public void setConsumoCombustible(Map<String, Double> c) { this.consumoCombustible = c; }
        public Map<String, Double> getDesgasteNeumaticos() { return desgasteNeumaticos; }
        public void setDesgasteNeumaticos(Map<String, Double> d) { this.desgasteNeumaticos = d; }
    }
}