package com.formula1.modelo;

public class ConfiguracionVehiculo {
    private String modeloVehiculo;
    private String modoConduccion;
    private double cargaAerodinamica;
    private double presionNeumaticos;
    private String estrategiaCombustible;

    public ConfiguracionVehiculo() {}

    public String getModeloVehiculo() { return modeloVehiculo; }
    public void setModeloVehiculo(String modeloVehiculo) { this.modeloVehiculo = modeloVehiculo; }
    public String getModoConduccion() { return modoConduccion; }
    public void setModoConduccion(String modoConduccion) { this.modoConduccion = modoConduccion; }
    public double getCargaAerodinamica() { return cargaAerodinamica; }
    public void setCargaAerodinamica(double cargaAerodinamica) { this.cargaAerodinamica = cargaAerodinamica; }
    public double getPresionNeumaticos() { return presionNeumaticos; }
    public void setPresionNeumaticos(double presionNeumaticos) { this.presionNeumaticos = presionNeumaticos; }
    public String getEstrategiaCombustible() { return estrategiaCombustible; }
    public void setEstrategiaCombustible(String estrategiaCombustible) { this.estrategiaCombustible = estrategiaCombustible; }

    @Override
    public String toString() {
        return String.format("Config %s: Modo=%s, Aero=%.2f, Presión=%.2f",
                modeloVehiculo, modoConduccion, cargaAerodinamica, presionNeumaticos);
    }
}
