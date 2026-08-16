package com.formula1;

import java.util.List;
import java.util.Scanner;

import com.formula1.almacenamiento.HistorialResultados;
import com.formula1.gestor.GestorCircuitos;
import com.formula1.simulacion.Clima;
import com.formula1.simulacion.GestorTemporada;
import com.formula1.simulacion.SimuladorClasificacion;
import com.formula1.simulacion.SistemaPuntos;
import com.formula1.simulacion.TablaCampeonato;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GestorTemporada gestorTemporada;
    private static SimuladorClasificacion simulador;
    private static HistorialResultados historial;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║      SIMULADOR DE FÓRMULA 1 - TEMPORADA 2026                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        // Inicializar componentes
        GestorCircuitos gestorCircuitos = new GestorCircuitos();
        gestorCircuitos.cargarDesdeJSON("data/circuitos.json");
        System.out.println();

        gestorTemporada = new GestorTemporada(2026);
        gestorTemporada.cargarCalendario(gestorCircuitos);
        System.out.println();

        simulador = new SimuladorClasificacion();
        historial = new HistorialResultados();

        // Menú principal
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1 -> simularSiguienteCarrera();
                case 2 -> simularTemporadaCompleta();
                case 3 -> mostrarCalendario();
                case 4 -> mostrarTablaCampeonato();
                case 5 -> mostrarResumenTemporada();
                case 6 -> mostrarHistorialCarreras();
                case 7 -> {
                    System.out.println("\n¡Gracias por usar el simulador F1!");
                    salir = true;
                }
                default -> System.out.println("\n❌ Opción inválida. Intenta de nuevo.\n");
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        MENÚ PRINCIPAL                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Simular siguiente carrera                                 ║");
        System.out.println("║  2. Simular temporada completa                                ║");
        System.out.println("║  3. Ver calendario                                            ║");
        System.out.println("║  4. Ver tabla de campeonato                                   ║");
        System.out.println("║  5. Ver resumen de temporada                                  ║");
        System.out.println("║  6. Ver historial de carreras                                 ║");
        System.out.println("║  7. Salir                                                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.print("Selecciona una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void simularSiguienteCarrera() {
        if (gestorTemporada.temporadaFinalizada()) {
            System.out.println("\n⚠️  La temporada ya finalizó. No hay más carreras.\n");
            return;
        }

        GestorTemporada.ResultadoCarrera resultado = gestorTemporada.simularSiguienteCarrera(simulador);

        if (resultado != null) {
            // Mostrar resultados completos
            System.out.println("╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    RESULTADOS DE LA CARRERA                    ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            List<SimuladorClasificacion.ResultadoVuelta> clasificacion = resultado.getClasificacion();

            // Contar DNFs
            int dnfCount = 0;
            int finalizadosCount = 0;
            for (SimuladorClasificacion.ResultadoVuelta r : clasificacion) {
                System.out.println(r);
                if (r.isDnf()) {
                    dnfCount++;
                } else {
                    finalizadosCount++;
                }
            }

            // Mostrar estadísticas de la carrera
            System.out.println("\n" + "─".repeat(65));
            System.out.println("📊 Estadísticas de la carrera:");
            System.out.println("   Pilotos que finalizaron: " + finalizadosCount);
            System.out.println("   Abandonos (DNF): " + dnfCount);
            System.out.println("─".repeat(65));

            // Guardar en historial
            historial.guardarSesion(resultado.getCircuito(), resultado.getClima(), clasificacion);

            // Mostrar tabla actualizada
            System.out.println();
            gestorTemporada.getCampeonato().mostrarClasificacion();

            // Mostrar ganador
            if (!resultado.getClasificacion().isEmpty() && !resultado.getClasificacion().get(0).isDnf()) {
                System.out.println("🏆 Ganador: " + resultado.getGanador());
            } else {
                System.out.println("⚠️  No hubo ganador válido en esta carrera");
            }
            System.out.println();

            // Verificar si terminó la temporada
            if (gestorTemporada.temporadaFinalizada()) {
                mostrarCampeonFinal();
            }
        }

        esperarEnter();
    }

    private static void mostrarCampeonFinal() {
        System.out.println("\n🏁 ¡TEMPORADA FINALIZADA! 🏁\n");
        TablaCampeonato.PilotoCampeonato campeon = gestorTemporada.getCampeonato().obtenerCampeon();
        if (campeon != null) {
            System.out.println("═".repeat(65));
            System.out.println("🏆🏆🏆 CAMPEÓN DEL MUNDO 2026: " + campeon.getNombre() + " 🏆🏆🏆");
            System.out.println("     Equipo: " + campeon.getEquipo());
            System.out.println("     Puntos totales: " + campeon.getPuntosTotal());
            System.out.println("═".repeat(65));
            System.out.println();
        }
    }

    private static void simularTemporadaCompleta() {
        if (gestorTemporada.getCarreraActual() > 0) {
            System.out.print("\n⚠️  Ya hay carreras simuladas. ¿Continuar con el resto? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (!respuesta.equals("s") && !respuesta.equals("si")) {
                return;
            }
        }

        int totalDNFs = 0;
        int totalAccidentes = 0;
        int totalFallosMecanicos = 0;
        int carrerasSimuladas = 0;

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║          SIMULANDO TEMPORADA COMPLETA 2026                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        while (!gestorTemporada.temporadaFinalizada()) {
            GestorTemporada.ResultadoCarrera resultado = gestorTemporada.simularSiguienteCarrera(simulador);
            if (resultado != null) {
                carrerasSimuladas++;

                // Acumular estadísticas
                totalDNFs += resultado.getTotalDNF();
                totalAccidentes += resultado.getTotalAccidentes();
                totalFallosMecanicos += resultado.getTotalFallosMecanicos();

                // Mostrar resumen DETALLADO de cada carrera
                System.out.println(resultado.getResumenDetallado());
                System.out.println();

                // Guardar en historial
                historial.guardarSesion(resultado.getCircuito(), resultado.getClima(), resultado.getClasificacion());
            }
        }

        System.out.println("\n🏁 ¡TEMPORADA FINALIZADA! 🏁\n");
        System.out.println("═".repeat(65));
        System.out.println("📊 ESTADÍSTICAS COMPLETAS DE LA TEMPORADA 2026:");
        System.out.println("─".repeat(65));
        System.out.println("   Carreras disputadas:       " + carrerasSimuladas);
        System.out.println("   Total de abandonos (DNF):  " + totalDNFs);
        System.out.println("   - Por accidentes:          " + totalAccidentes);
        System.out.println("   - Por fallos mecánicos:    " + totalFallosMecanicos);
        System.out.println("   Promedio DNF por carrera:  " + String.format("%.2f", (double) totalDNFs / carrerasSimuladas));
        System.out.println("   Tasa de finalización:      " + String.format("%.1f%%",
                ((22.0 * carrerasSimuladas - totalDNFs) / (22.0 * carrerasSimuladas)) * 100));
        System.out.println("═".repeat(65));
        System.out.println();

        // Mostrar tabla final
        gestorTemporada.getCampeonato().mostrarClasificacion();

        // Mostrar TOP 3 del campeonato con detalles
        System.out.println("═".repeat(65));
        System.out.println("🏆 PODIO DEL CAMPEONATO MUNDIAL 2026:");
        System.out.println("─".repeat(65));

        List<TablaCampeonato.PilotoCampeonato> top3 = gestorTemporada.getCampeonato().obtenerClasificacion();
        for (int i = 0; i < Math.min(3, top3.size()); i++) {
            TablaCampeonato.PilotoCampeonato piloto = top3.get(i);
            String medalla = i == 0 ? "🥇" : (i == 1 ? "🥈" : "🥉");
            System.out.printf("%s P%d: %s (%s) - %d puntos%n",
                    medalla, (i + 1), piloto.getNombre(), piloto.getEquipo(), piloto.getPuntosTotal());
        }
        System.out.println("═".repeat(65));
        System.out.println();

        // Mostrar campeón
        TablaCampeonato.PilotoCampeonato campeon = gestorTemporada.getCampeonato().obtenerCampeon();
        if (campeon != null) {
            System.out.println("═".repeat(65));
            System.out.println("🏆🏆🏆 CAMPEÓN DEL MUNDO 2026: " + campeon.getNombre() + " 🏆🏆🏆");
            System.out.println("     Equipo:           " + campeon.getEquipo());
            System.out.println("     Puntos totales:   " + campeon.getPuntosTotal());
            System.out.println("     Carreras:         " + campeon.getCarrerasCompletadas());
            System.out.println("     Mejor posición:   P" + campeon.getMejorPosicion());
            System.out.println("═".repeat(65));
            System.out.println();
        }

        esperarEnter();
    }

    private static void mostrarCalendario() {
        gestorTemporada.mostrarCalendario();
        esperarEnter();
    }

    private static void mostrarTablaCampeonato() {
        gestorTemporada.getCampeonato().mostrarClasificacion();
        esperarEnter();
    }

    private static void mostrarResumenTemporada() {
        gestorTemporada.mostrarResumenTemporada();
        esperarEnter();
    }

    private static void mostrarHistorialCarreras() {
        List<GestorTemporada.ResultadoCarrera> historialCarreras = gestorTemporada.getHistorialCarreras();

        if (historialCarreras.isEmpty()) {
            System.out.println("\n⚠️  No hay carreras simuladas todavía.\n");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                  HISTORIAL DE CARRERAS                         ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

            // Mostrar resumen detallado de cada carrera
            for (GestorTemporada.ResultadoCarrera resultado : historialCarreras) {
                System.out.println(resultado.getResumenDetallado());
                System.out.println();
            }

            // Estadísticas acumuladas
            int totalDNFs = 0;
            int totalAccidentes = 0;
            int totalFallosMecanicos = 0;
            int totalPenalizaciones = 0;

            for (GestorTemporada.ResultadoCarrera r : historialCarreras) {
                totalDNFs += r.getTotalDNF();
                totalAccidentes += r.getTotalAccidentes();
                totalFallosMecanicos += r.getTotalFallosMecanicos();
                totalPenalizaciones += r.getTotalPenalizaciones();
            }

            System.out.println("═".repeat(65));
            System.out.println("📊 Resumen acumulado (" + historialCarreras.size() + " carreras):");
            System.out.println("   Total DNF: " + totalDNFs + " | Accidentes: " + totalAccidentes +
                    " | Fallos mec.: " + totalFallosMecanicos + " | Penalizaciones: " + totalPenalizaciones);
            System.out.println("═".repeat(65));
            System.out.println();
        }

        esperarEnter();
    }

    private static void esperarEnter() {
        System.out.print("Presiona ENTER para continuar...");
        scanner.nextLine();
        System.out.println();
    }
}
