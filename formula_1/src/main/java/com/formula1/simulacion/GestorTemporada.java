package com.formula1.simulacion;

import java.util.ArrayList;
import java.util.List;

import com.formula1.gestor.GestorCircuitos;
import com.formula1.gestor.GestorPilotos;
import com.formula1.gestor.GestorVehiculos;
import com.formula1.modelo.Circuito;

/**
 * Gestor principal de la temporada de Formula 1.
 * Administra el calendario de 24 carreras, controla la progresion de la temporada,
 * simula carreras secuencialmente, acumula puntos en la tabla de campeonato
 * y almacena el historial de resultados de cada carrera disputada.
 */
public class GestorTemporada {

    /** Calendario ordenado de circuitos para la temporada */
    private List<Circuito> calendario;
    /** Indice de la siguiente carrera a simular (0-based) */
    private int carreraActual;
    /** Tabla de clasificacion del mundial de pilotos */
    private TablaCampeonato campeonato;
    /** Historial de resultados de todas las carreras disputadas */
    private List<ResultadoCarrera> historialCarreras;
    /** Anio de la temporada */
    private int temporada;
    /** Gestor de pilotos reales (nullable - si es null se usan datos de prueba) */
    private GestorPilotos gestorPilotos;
    /** Gestor de vehiculos reales (nullable - si es null se usan datos de prueba) */
    private GestorVehiculos gestorVehiculos;

    /**
     * Crea un gestor de temporada para un anio especifico.
     * @param temporada anio de la temporada (ej: 2026)
     */
    public GestorTemporada(int temporada) {
        this.temporada = temporada;
        this.calendario = new ArrayList<>();
        this.carreraActual = 0;
        this.campeonato = new TablaCampeonato();
        this.historialCarreras = new ArrayList<>();
    }

    /**
     * Conecta los gestores de datos reales para la simulacion.
     * Si no se llama, el simulador usara datos de prueba hardcodeados.
     * @param gestorPilotos gestor con los 22 pilotos cargados desde JSON
     * @param gestorVehiculos gestor con los vehiculos cargados desde JSON
     */
    public void configurarDatosReales(GestorPilotos gestorPilotos, GestorVehiculos gestorVehiculos) {
        this.gestorPilotos = gestorPilotos;
        this.gestorVehiculos = gestorVehiculos;
    }

    /**
     * Clase interna que almacena el resultado completo de una carrera.
     * Calcula automaticamente estadisticas: DNFs, accidentes, fallos mecanicos,
     * penalizaciones y determina el ganador.
     */
    public static class ResultadoCarrera {
        private int numeroCarrera;
        private Circuito circuito;
        private Clima clima;
        private List<SimuladorClasificacion.ResultadoVuelta> clasificacion;
        private String ganador;
        private int totalDNF;
        private int totalFinalizados;
        private int totalAccidentes;
        private int totalFallosMecanicos;
        private int totalPenalizaciones;

        public ResultadoCarrera(int numeroCarrera, Circuito circuito, Clima clima,
                                List<SimuladorClasificacion.ResultadoVuelta> clasificacion) {
            this.numeroCarrera = numeroCarrera;
            this.circuito = circuito;
            this.clima = clima;
            this.clasificacion = clasificacion;

            // Calcular estadísticas
            calcularEstadisticas();

            // Determinar ganador (primer piloto que no sea DNF)
            this.ganador = "N/A";
            for (SimuladorClasificacion.ResultadoVuelta r : clasificacion) {
                if (!r.isDnf()) {
                    this.ganador = r.getNombrePiloto();
                    break;
                }
            }
        }

        private void calcularEstadisticas() {
            totalDNF = 0;
            totalFinalizados = 0;
            totalAccidentes = 0;
            totalFallosMecanicos = 0;
            totalPenalizaciones = 0;

            for (SimuladorClasificacion.ResultadoVuelta r : clasificacion) {
                if (r.isDnf()) {
                    totalDNF++;
                    // Clasificar tipo de DNF
                    String estado = r.getEstadoFinal().toLowerCase();
                    if (estado.contains("accidente")) {
                        totalAccidentes++;
                    } else if (estado.contains("fallo") || estado.contains("motor") || estado.contains("frenos")) {
                        totalFallosMecanicos++;
                    }
                } else {
                    totalFinalizados++;
                    // Contar penalizaciones
                    if (r.getEvento() != EventoCarrera.NORMAL && !r.getEvento().esAbandonoDNF()) {
                        totalPenalizaciones++;
                    }
                }
            }
        }

        public int getNumeroCarrera() { return numeroCarrera; }
        public Circuito getCircuito() { return circuito; }
        public Clima getClima() { return clima; }
        public List<SimuladorClasificacion.ResultadoVuelta> getClasificacion() { return clasificacion; }
        public String getGanador() { return ganador; }
        public int getTotalDNF() { return totalDNF; }
        public int getTotalFinalizados() { return totalFinalizados; }
        public int getTotalAccidentes() { return totalAccidentes; }
        public int getTotalFallosMecanicos() { return totalFallosMecanicos; }
        public int getTotalPenalizaciones() { return totalPenalizaciones; }

        public String getResumenDetallado() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("R%02d: %-35s | %-10s | 🏆 %-20s%n",
                    numeroCarrera, circuito.getNombre(), clima, ganador));
            sb.append(String.format("     Finalizados: %2d | DNF: %2d (Accidentes: %d, Fallos mec.: %d) | Penalizaciones: %d",
                    totalFinalizados, totalDNF, totalAccidentes, totalFallosMecanicos, totalPenalizaciones));
            return sb.toString();
        }

        @Override
        public String toString() {
            return String.format("R%02d: %s | Clima: %s | 🏆 %s | DNF: %d",
                    numeroCarrera, circuito.getNombre(), clima, ganador, totalDNF);
        }
    }

    /**
     * Carga el calendario completo de 24 carreras desde el gestor de circuitos.
     * Busca cada circuito por nombre exacto segun el orden oficial F1 2026.
     * @param gestorCircuitos gestor con los circuitos cargados
     */
    public void cargarCalendario(GestorCircuitos gestorCircuitos) {
        // Orden del calendario F1 2026 (24 carreras) - NOMBRES EXACTOS DEL JSON
        String[] ordenCircuitos = {
            "Bahrain International Circuit",       // 1. Bahréin
            "Jeddah Street Circuit",               // 2. Arabia Saudita
            "Albert Park Circuit",                 // 3. Australia
            "Suzuka International Racing Course",  // 4. Japón
            "Shanghai International Circuit",      // 5. China
            "Miami International Autodrome",       // 6. Miami
            "Circuito de Madring",                 // 7. Emilia Romaña (Imola)
            "Circuito de Mónaco",                  // 8. Mónaco
            "Circuit de Barcelona-Catalunya",      // 9. España
            "Circuit Gilles-Villeneuve",           // 10. Canadá
            "Red Bull Ring",                       // 11. Austria
            "Silverstone",                         // 12. Gran Bretaña
            "Hungaroring",                         // 13. Hungría
            "Circuito de Spa-Francorchamps",       // 14. Bélgica
            "Circuit Zandvoort",                   // 15. Países Bajos
            "Circuito de Monza",                   // 16. Italia
            "Baku City Circuit",                   // 17. Azerbaiyán
            "Marina Bay Street Circuit",           // 18. Singapur
            "Circuit of the Americas",             // 19. Estados Unidos
            "Autódromo Hermanos Rodríguez",        // 20. México
            "Interlagos",                          // 21. Brasil
            "Las Vegas Strip Circuit",             // 22. Las Vegas
            "Lusail International Circuit",        // 23. Qatar
            "Circuito de Yas Marina"               // 24. Abu Dhabi
        };

        for (String nombreCircuito : ordenCircuitos) {
            Circuito circuito = gestorCircuitos.buscarPorNombre(nombreCircuito);
            if (circuito != null) {
                calendario.add(circuito);
            } else {
                System.out.println("Advertencia: No se encontró el circuito: " + nombreCircuito);
            }
        }

        System.out.println("Calendario cargado: " + calendario.size() + " carreras.");
    }

    /**
     * Simula la siguiente carrera del calendario.
     * Genera clima realista, ejecuta la simulacion, registra puntos y guarda resultado.
     * @param simulador motor de simulacion a utilizar
     * @return resultado de la carrera o null si la temporada ya termino
     */
    public ResultadoCarrera simularSiguienteCarrera(SimuladorClasificacion simulador) {
        if (carreraActual >= calendario.size()) {
            System.out.println("¡La temporada ha finalizado!");
            return null;
        }

        Circuito circuito = calendario.get(carreraActual);
        // Usar clima realista basado en el circuito
        Clima clima = GeneradorClima.generarClimaPorCircuito(circuito.getNombre());

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║  CARRERA " + (carreraActual + 1) + " de " + calendario.size() +
                " - " + circuito.getNombre());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("Ubicación: " + circuito.getPais());
        System.out.println("Clima: " + clima);
        System.out.println("Distancia: " + circuito.getLongitudKm() + " km x " + circuito.getVueltas() + " vueltas");
        System.out.println();

        // Simular (datos reales si ya se configuraron; si no, datos de prueba como respaldo)
        List<SimuladorClasificacion.ResultadoVuelta> clasificacion;
        if (gestorPilotos != null && gestorVehiculos != null) {
            clasificacion = simulador.simularConDatosReales(circuito, clima, gestorPilotos.listarPilotos(), gestorVehiculos);
        } else {
            clasificacion = simulador.simularConDatosPrueba(circuito, clima);
        }

        // Registrar en campeonato
        campeonato.registrarCarrera(clasificacion);

        // Guardar resultado
        ResultadoCarrera resultado = new ResultadoCarrera(carreraActual + 1, circuito, clima, clasificacion);
        historialCarreras.add(resultado);

        carreraActual++;
        return resultado;
    }

    /**
     * Simula todas las carreras restantes de la temporada de golpe.
     * @param simulador motor de simulacion a utilizar
     */
    public void simularTemporadaCompleta(SimuladorClasificacion simulador) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║          SIMULANDO TEMPORADA COMPLETA " + temporada + "                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        while (carreraActual < calendario.size()) {
            ResultadoCarrera resultado = simularSiguienteCarrera(simulador);
            if (resultado != null) {
                // Mostrar resumen breve
                System.out.println("✓ " + resultado);
                System.out.println();
            }
        }

        System.out.println("\n🏁 ¡TEMPORADA FINALIZADA! 🏁\n");
    }

    /** @return true si todas las carreras del calendario fueron disputadas */
    public boolean temporadaFinalizada() {
        return carreraActual >= calendario.size();
    }

    // Getters
    public List<Circuito> getCalendario() { return calendario; }
    public int getCarreraActual() { return carreraActual; }
    public TablaCampeonato getCampeonato() { return campeonato; }
    public List<ResultadoCarrera> getHistorialCarreras() { return historialCarreras; }
    public int getTemporada() { return temporada; }

    /** @return el circuito de la siguiente carrera, o null si la temporada termino */
    public Circuito getSiguienteCircuito() {
        if (carreraActual < calendario.size()) {
            return calendario.get(carreraActual);
        }
        return null;
    }

    /** Imprime el calendario completo con indicadores de progreso (completado/siguiente/pendiente) */
    public void mostrarCalendario() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║             CALENDARIO F1 TEMPORADA " + temporada + "                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        for (int i = 0; i < calendario.size(); i++) {
            Circuito c = calendario.get(i);
            String estado = i < carreraActual ? "✓" : (i == carreraActual ? "▶" : " ");
            System.out.printf("%s R%-2d: %-40s (%s)%n", estado, (i + 1), c.getNombre(), c.getPais());
        }
        System.out.println();
    }

    /** Imprime un resumen de la temporada con las ultimas 5 carreras disputadas */
    public void mostrarResumenTemporada() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              RESUMEN TEMPORADA " + temporada + "                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("Carreras completadas: " + carreraActual + " / " + calendario.size());
        System.out.println();

        if (!historialCarreras.isEmpty()) {
            System.out.println("Últimas carreras:");
            int inicio = Math.max(0, historialCarreras.size() - 5);
            for (int i = inicio; i < historialCarreras.size(); i++) {
                System.out.println("  " + historialCarreras.get(i));
            }
        }
    }
}