package com.formula1.simulacion;

import java.util.*;

/**
 * Tabla de clasificacion del campeonato mundial de pilotos.
 * Acumula puntos carrera a carrera y genera la clasificacion ordenada.
 * El desempate se resuelve por mejor posicion obtenida en la temporada.
 */
public class TablaCampeonato {
    /** Mapa de pilotos en el campeonato, indexado por nombre */
    private Map<String, PilotoCampeonato> pilotos;

    public TablaCampeonato() {
        this.pilotos = new HashMap<>();
    }

    /**
     * Clase interna que almacena las estadisticas acumuladas de un piloto en el campeonato.
     * Registra puntos totales, carreras completadas, puntos por carrera y mejor posicion.
     */
    public static class PilotoCampeonato {
        private String nombre;
        private String equipo;
        private int puntosTotal;
        private int carrerasCompletadas;
        private List<Integer> puntosPorCarrera;
        private int mejorPosicion;

        public PilotoCampeonato(String nombre, String equipo) {
            this.nombre = nombre;
            this.equipo = equipo;
            this.puntosTotal = 0;
            this.carrerasCompletadas = 0;
            this.puntosPorCarrera = new ArrayList<>();
            this.mejorPosicion = Integer.MAX_VALUE;
        }

        public void agregarResultado(int posicion, int puntos) {
            this.puntosTotal += puntos;
            this.carrerasCompletadas++;
            this.puntosPorCarrera.add(puntos);
            if (posicion < mejorPosicion) {
                this.mejorPosicion = posicion;
            }
        }

        public String getNombre() { return nombre; }
        public String getEquipo() { return equipo; }
        public int getPuntosTotal() { return puntosTotal; }
        public int getCarrerasCompletadas() { return carrerasCompletadas; }
        public List<Integer> getPuntosPorCarrera() { return puntosPorCarrera; }
        public int getMejorPosicion() { return mejorPosicion; }

        @Override
        public String toString() {
            String mejorPos = mejorPosicion == Integer.MAX_VALUE ? "N/A" : "P" + mejorPosicion;
            return String.format("%s (%s) | %d pts | Carreras: %d | Mejor: %s",
                    nombre, equipo, puntosTotal, carrerasCompletadas, mejorPos);
        }
    }

    /**
     * Registra los resultados de una carrera en la tabla de campeonato.
     * Crea pilotos nuevos si es su primera carrera, y acumula puntos.
     * @param resultados clasificacion completa de la carrera
     */
    public void registrarCarrera(List<SimuladorClasificacion.ResultadoVuelta> resultados) {
        for (SimuladorClasificacion.ResultadoVuelta resultado : resultados) {
            String nombrePiloto = resultado.getNombrePiloto();

            // Si el piloto no existe, créalo
            if (!pilotos.containsKey(nombrePiloto)) {
                pilotos.put(nombrePiloto, new PilotoCampeonato(nombrePiloto, resultado.getEquipo()));
            }

            // Agregar resultado
            PilotoCampeonato piloto = pilotos.get(nombrePiloto);
            piloto.agregarResultado(resultado.getPosicion(), resultado.getPuntos());
        }
    }

    /**
     * Obtiene la clasificacion del campeonato ordenada por puntos (mayor a menor).
     * En caso de empate, se desempata por mejor posicion obtenida.
     * @return lista ordenada de pilotos del campeonato
     */
    public List<PilotoCampeonato> obtenerClasificacion() {
        List<PilotoCampeonato> clasificacion = new ArrayList<>(pilotos.values());
        clasificacion.sort((p1, p2) -> {
            // Primero por puntos totales (descendente)
            int comparacionPuntos = Integer.compare(p2.getPuntosTotal(), p1.getPuntosTotal());
            if (comparacionPuntos != 0) {
                return comparacionPuntos;
            }
            // Si empatan en puntos, por mejor posición (ascendente)
            return Integer.compare(p1.getMejorPosicion(), p2.getMejorPosicion());
        });
        return clasificacion;
    }

    /** Imprime la tabla de campeonato formateada en consola */
    public void mostrarClasificacion() {
        List<PilotoCampeonato> clasificacion = obtenerClasificacion();
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            TABLA DE CAMPEONATO - TEMPORADA 2026                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.printf("%-4s %-25s %-20s %6s %8s %10s%n",
                "Pos", "Piloto", "Equipo", "Puntos", "Carreras", "Mejor");
        System.out.println("─────────────────────────────────────────────────────────────────");

        for (int i = 0; i < clasificacion.size(); i++) {
            PilotoCampeonato piloto = clasificacion.get(i);
            String mejorPos = piloto.getMejorPosicion() == Integer.MAX_VALUE ? "-" : "P" + piloto.getMejorPosicion();
            System.out.printf("%-4d %-25s %-20s %6d %8d %10s%n",
                    (i + 1),
                    piloto.getNombre(),
                    piloto.getEquipo(),
                    piloto.getPuntosTotal(),
                    piloto.getCarrerasCompletadas(),
                    mejorPos);
        }
        System.out.println();
    }

    /**
     * Obtiene al campeon actual (piloto con mas puntos).
     * @return el piloto lider o null si no hay pilotos registrados
     */
    public PilotoCampeonato obtenerCampeon() {
        List<PilotoCampeonato> clasificacion = obtenerClasificacion();
        return clasificacion.isEmpty() ? null : clasificacion.get(0);
    }

    /** Reinicia la tabla para comenzar una nueva temporada desde cero */
    public void reiniciar() {
        pilotos.clear();
    }
}
