package com.formula1.simulacion;

import java.util.HashMap;
import java.util.Map;

public class SistemaPuntos {
    // Sistema de puntos oficial de F1 2026
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

    // Obtiene los puntos correspondientes a una posición
    public static int obtenerPuntos(int posicion) {
        return PUNTOS_POR_POSICION.getOrDefault(posicion, 0);
    }

    // Verifica si una posición puntúa
    public static boolean esPosicionConPuntos(int posicion) {
        return posicion >= 1 && posicion <= 10;
    }

    // Obtiene el máximo de puntos posibles en una carrera
    public static int getPuntosMaximos() {
        return 25;
    }

    // Para debug/visualización: muestra la tabla de puntos
    public static void mostrarTablaPuntos() {
        System.out.println("=== Sistema de Puntos F1 ===");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("P%d: %d puntos%n", i, obtenerPuntos(i));
        }
        System.out.println("P11-P22: 0 puntos");
    }
}
