package com.formula1.modelo;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public class Vehiculo {
    private String modelo;
    private String motor;
    private int velocidadMaximaKmh;

    @SerializedName("aceleracion_0_100")
    private double aceleracion0100;

    private List<Integer> pilotos;
    private Map<String, Map<String, RendimientoClima>> rendimiento;

    public Vehiculo() { this.pilotos = new ArrayList<>(); }

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
    public Map<String, Map<String, RendimientoClima>> getRendimiento() { return rendimiento; }
    public void setRendimiento(Map<String, Map<String, RendimientoClima>> rendimiento) { this.rendimiento = rendimiento; }

    public static class RendimientoClima {
        private double consumoCombustible;
        private double desgasteNeumaticos;

        public double getConsumoCombustible() { return consumoCombustible; }
        public void setConsumoCombustible(double consumoCombustible) { this.consumoCombustible = consumoCombustible; }
        public double getDesgasteNeumaticos() { return desgasteNeumaticos; }
        public void setDesgasteNeumaticos(double desgasteNeumaticos) { this.desgasteNeumaticos = desgasteNeumaticos; }
    }

    @Override
    public String toString() {
        return String.format("%s - Motor: %s, Vel. Máx: %d km/h", modelo, motor, velocidadMaximaKmh);
    }
}
