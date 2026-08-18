package com.formula1.simulacion;

import java.util.HashMap;
import java.util.Map;

/**
 * Sistema de puntos oficial de Formula 1 para la temporada 2026.
 * Distribuye puntos a los primeros 10 clasificados: 25-18-15-12-10-8-6-4-2-1.
 * Las posiciones 11 a 22 no reciben puntos.
 * Clase utilitaria con metodos estaticos (no requiere instanciacion).
 */
public class SistemaPuntos {
    /** Mapa de posicion -> puntos (solo posiciones 1-10) */
    private static final Map<Integer, Integer> PUNTOS_POR_POSICION = new HashMap<>();

    static {
        PUNTOS_POR_POSICION.put(1, 25);   // 1er lugar
        PUNTOS_POR_POSICION.put(2, 18);   // 2do lugar
        PUNTOS_POR_POSICION.put(3, 15);   // 3er lugar
        PUNTOS_POR_POSICION.put(4, 12);
        PUNTOS_POR_POSICION.put(5, 10);
        PUNTOS_POR_POSICION.put(6, 8);
        PUNTOS_POR_POSICION.put(7, 6);
        PUNTOS_POR_POSICION.put(8, 4);
        PUNTOS_POR_POSICION.put(9, 2);
        PUNTOS_POR_POSICION.put(10, 1);
        // Posiciones 11-22 no puntúan (0 puntos)
    }

    /**
     * Obtiene los puntos correspondientes a una posicion.
     * @param posicion posicion final en la carrera (1-22)
     * @return puntos otorgados (0 si la posicion no puntua)
     */
    public static int obtenerPuntos(int posicion) {
        return PUNTOS_POR_POSICION.getOrDefault(posicion, 0);
    }

    /** @return true si la posicion esta entre 1 y 10 (zona de puntos) */
    public static boolean esPosicionConPuntos(int posicion) {
        return posicion >= 1 && posicion <= 10;
    }

    /** @return 25, el maximo de puntos posibles en una carrera (victoria) */
    public static int getPuntosMaximos() {
        return 25;
    }

    /** Imprime la tabla de puntos en consola (uso para depuracion) */
    public static void mostrarTablaPuntos() {
        System.out.println("=== Sistema de Puntos F1 ===");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("P%d: %d puntos%n", i, obtenerPuntos(i));
        }
        System.out.println("P11-P22: 0 puntos");
    }
}
