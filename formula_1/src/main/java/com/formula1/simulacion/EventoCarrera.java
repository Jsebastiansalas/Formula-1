package com.formula1.simulacion;

/**
 * Enum que define todos los eventos posibles durante una carrera de F1.
 * Cada evento tiene una descripcion, una penalizacion de tiempo en segundos
 * y un indicador de si causa abandono (DNF - Did Not Finish).
 * Los eventos con DNF hacen que el piloto abandone la carrera.
 * Los eventos sin DNF agregan tiempo de penalizacion al resultado final.
 */
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

    /** Descripcion legible del evento */
    private final String descripcion;
    /** Segundos de penalizacion que se suman al tiempo (0 para eventos DNF) */
    private final double penalizacionSegundos;
    /** Si es true, el piloto abandona la carrera (Did Not Finish) */
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
