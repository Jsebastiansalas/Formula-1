package com.formula1.simulacion;

/**
 * Enum que define los modos de conduccion disponibles para un piloto.
 * Cada modo afecta el rendimiento del vehiculo de forma diferente:
 * - NORMAL: balance entre velocidad y consumo
 * - AGRESIVA: mayor velocidad pero mas desgaste y consumo
 * - AHORRO: menor velocidad pero conserva neumaticos y combustible
 */
public enum ModoConduccion {
    NORMAL, AGRESIVA, AHORRO
}
