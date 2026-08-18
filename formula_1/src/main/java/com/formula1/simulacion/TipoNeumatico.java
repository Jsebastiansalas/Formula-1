package com.formula1.simulacion;

/**
 * Enum que modela los cinco compuestos de neumaticos de Formula 1.
 * Cada compuesto tiene un factor de velocidad (mayor = mas rapido)
 * y un factor de desgaste (mayor = se degrada mas rapido).
 * La seleccion del neumatico depende de las condiciones climaticas.
 */
public enum TipoNeumatico {
    /** Compuesto blando: maximo agarre y velocidad, desgaste muy alto */
    BLANDO("Blando", "Soft", 1.03, 2.5),
    /** Compuesto medio: balance entre velocidad y durabilidad */
    MEDIO("Medio", "Medium", 1.0, 1.5),
    /** Compuesto duro: menor velocidad pero maxima durabilidad */
    DURO("Duro", "Hard", 0.97, 1.0),
    /** Intermedio: disenado para pista humeda con lluvia ligera */
    INTERMEDIO("Intermedio", "Intermediate", 0.95, 1.2),
    /** Lluvia extrema: maximo drenaje para condiciones de agua severa */
    LLUVIA("Lluvia", "Wet", 0.88, 0.8);

    private final String nombreEspanol;
    private final String nombreIngles;
    /** Multiplicador de velocidad (>1 = mas rapido que la base) */
    private final double factorVelocidad;
    /** Multiplicador de desgaste (mayor valor = se gasta mas rapido) */
    private final double factorDesgaste;

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

    /**
     * Selecciona el neumatico optimo segun las condiciones climaticas.
     * @param clima condicion climatica actual
     * @return el compuesto mas apropiado para ese clima
     */
    public static TipoNeumatico seleccionarOptimo(Clima clima) {
        return switch (clima) {
            case SECO -> MEDIO;           // En seco, medio es buen balance
            case LLUVIOSO -> INTERMEDIO;  // Lluvia ligera -> Intermedio
            case EXTREMO -> LLUVIA;       // Lluvia fuerte -> Lluvia
        };
    }

    /**
     * Verifica si este neumatico es apropiado para el clima dado.
     * Usar un neumatico inapropiado genera penalizacion de velocidad del 15%.
     * @param clima condicion climatica a evaluar
     * @return true si el neumatico es adecuado para ese clima
     */
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
