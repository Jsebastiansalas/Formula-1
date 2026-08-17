package com.formula1.simulacion;

public enum EventoCarrera {
    // Eventos que pueden ocurrir durante una carrera
    NORMAL("Carrera sin incidentes", 0, false),
    ACCIDENTE_LEVE("Accidente leve - Pérdida de tiempo", 5.0, false),
    ACCIDENTE_GRAVE("Accidente grave - Abandono", 0, true),
    FALLO_MECANICO("Fallo mecánico - Abandono", 0, true),
    FALLO_MOTOR("Fallo de motor - Abandono", 0, true),
    PINCHAZO("Pinchazo - Pérdida de tiempo", 25.0, false),
    PROBLEMA_FRENOS("Problema de frenos - Abandono", 0, true),
    TROMPO("Trompo - Pérdida de tiempo", 8.0, false),
    PENALIZACION_5S("Penalización 5 segundos", 5.0, false),
    PENALIZACION_10S("Penalización 10 segundos", 10.0, false),
    SALIDA_PISTA("Salida de pista - Pérdida de tiempo", 3.0, false);

    private final String descripcion;
    private final double penalizacionSegundos;
    private final boolean esAbandonoDNF;

    EventoCarrera(String descripcion, double penalizacionSegundos, boolean esAbandonoDNF) {
        this.descripcion = descripcion;
        this.penalizacionSegundos = penalizacionSegundos;
        this.esAbandonoDNF = esAbandonoDNF;
    }

    public String getDescripcion() { return descripcion; }
    public double getPenalizacionSegundos() { return penalizacionSegundos; }
    public boolean esAbandonoDNF() { return esAbandonoDNF; }

    @Override
    public String toString() {
        return descripcion;
    }
}
