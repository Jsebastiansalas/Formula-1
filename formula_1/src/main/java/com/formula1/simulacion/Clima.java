package com.formula1.simulacion;

import java.util.Random;

/**
 * Enum que representa las condiciones climaticas posibles durante una carrera.
 * El clima afecta directamente:
 * - La velocidad de los vehiculos (factor multiplicador)
 * - La probabilidad de incidentes (mayor en EXTREMO)
 * - La seleccion optima de neumaticos
 */
public enum Clima {
    /** Condiciones secas - pista en optimo estado */
    SECO,
    /** Lluvia moderada - pista mojada, menor agarre */
    LLUVIOSO,
    /** Condiciones extremas - visibilidad reducida, alto riesgo */
    EXTREMO;

    /**
     * Genera un clima completamente aleatorio (probabilidad uniforme).
     * Para generacion realista basada en circuito, usar GeneradorClima.
     * @return un valor aleatorio de Clima
     */
    public static Clima generarAleatorio() {
        Clima[] valores = Clima.values();
        Random random = new Random();
        return valores[random.nextInt(valores.length)];
    }
}
