package com.formula1.simulacion;

public enum TipoNeumatico {
    BLANDO("Blando", "Soft", 1.03, 2.5),      // +3% velocidad, desgaste alto
    MEDIO("Medio", "Medium", 1.0, 1.5),       // velocidad normal, desgaste medio
    DURO("Duro", "Hard", 0.97, 1.0),          // -3% velocidad, desgaste bajo
    INTERMEDIO("Intermedio", "Intermediate", 0.95, 1.2),  // para lluvia ligera
    LLUVIA("Lluvia", "Wet", 0.88, 0.8);       // para condiciones extremas

    private final String nombreEspanol;
    private final String nombreIngles;
    private final double factorVelocidad;     // multiplica la velocidad base
    private final double factorDesgaste;       // qué tan rápido se desgasta

    TipoNeumatico(String nombreEspanol, String nombreIngles, double factorVelocidad, double factorDesgaste) {
        this.nombreEspanol = nombreEspanol;
        this.nombreIngles = nombreIngles;
        this.factorVelocidad = factorVelocidad;
        this.factorDesgaste = factorDesgaste;
    }

    public String getNombreEspanol() { return nombreEspanol; }
    public String getNombreIngles() { return nombreIngles; }
    public double getFactorVelocidad() { return factorVelocidad; }
    public double getFactorDesgaste() { return factorDesgaste; }

    // Selecciona el neumático óptimo según el clima
    public static TipoNeumatico seleccionarOptimo(Clima clima) {
        return switch (clima) {
            case SECO -> MEDIO;           // En seco, medio es buen balance
            case LLUVIOSO -> INTERMEDIO;  // Lluvia ligera -> Intermedio
            case EXTREMO -> LLUVIA;       // Lluvia fuerte -> Lluvia
        };
    }

    // Verifica si el neumático es apropiado para el clima
    public boolean esApropiadoPara(Clima clima) {
        return switch (clima) {
            case SECO -> this == BLANDO || this == MEDIO || this == DURO;
            case LLUVIOSO -> this == INTERMEDIO || this == LLUVIA;
            case EXTREMO -> this == LLUVIA;
        };
    }

    @Override
    public String toString() {
        return nombreEspanol;
    }
}
