package com.formula1.modelo;

/**
 * Representa la configuracion tecnica de un vehiculo para una carrera.
 * Incluye modo de conduccion, carga aerodinamica, presion de neumaticos
 * y estrategia de combustible. Se persiste en data/configuracion.json.
 */
public class ConfiguracionVehiculo {

    /** Nombre del modelo de vehiculo al que aplica esta configuracion */
    private String modeloVehiculo;
    /** Modo de conduccion seleccionado (normal, agresiva, ahorro) */
    private String modoConduccion;
    /** Nivel de carga aerodinamica (mayor = mas agarre, menor velocidad punta) */
    private double cargaAerodinamica;
    /** Presion de neumaticos en PSI */
    private double presionNeumaticos;
    /** Estrategia de combustible para la carrera */
    private String estrategiaCombustible;

    /** Constructor vacio requerido por Gson para la deserializacion */
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
