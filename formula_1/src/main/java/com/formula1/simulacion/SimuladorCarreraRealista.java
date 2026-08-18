package com.formula1.simulacion;

import com.formula1.gestor.GestorPilotos;
import com.formula1.gestor.GestorEquipos;
import com.formula1.modelo.Circuito;
import com.formula1.modelo.Piloto;
import com.formula1.modelo.Equipo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Simulador avanzado de carreras con datos reales y eventos dinamicos.
 * A diferencia de SimuladorClasificacion (que calcula un solo tiempo por piloto),
 * este simulador ejecuta la carrera vuelta a vuelta, generando:
 * - Cambios de clima durante la carrera
 * - Adelantamientos entre pilotos adyacentes
 * - Eventos por vuelta (accidentes, fallos, penalizaciones)
 * - Vuelta rapida y estadisticas detalladas
 * Se usa desde PantallaSimulacionMejorada para ofrecer una experiencia mas inmersiva.
 */
public class SimuladorCarreraRealista {
    private Random random;
    private GestorPilotos gestorPilotos;
    private GestorEquipos gestorEquipos;

    /** Contador de cambios de clima durante la carrera */
    private int cambiasClimaEnCarrera;
    /** Lista de eventos importantes ocurridos (para resumen post-carrera) */
    private List<String> eventosImportantes;
    /** Total de adelantamientos ocurridos en la carrera */
    private int totalAdelantamientos;
    /** Vuelta en la que se registro la vuelta mas rapida */
    private int vueltaMasRapida;
    /** Piloto que registro la vuelta mas rapida */
    private String pilotoVueltaRapida;
    /** Tiempo de la vuelta mas rapida en segundos */
    private double tiempoVueltaRapida;

    public SimuladorCarreraRealista() {
        this.random = new Random();
        this.gestorPilotos = new GestorPilotos();
        this.gestorEquipos = new GestorEquipos();
        this.eventosImportantes = new ArrayList<>();
    }

    /**
     * Simula una carrera completa vuelta a vuelta con eventos dinamicos.
     * Genera clima variable, adelantamientos y eventos aleatorios por vuelta.
     * @param gestorTemporada gestor de temporada para obtener el circuito actual
     * @param climaInicial clima al inicio de la carrera
     * @return resultado detallado con clasificacion, estadisticas y eventos
     */
    public ResultadoCarreraDetallado simularCarreraCompleta(GestorTemporada gestorTemporada, Clima climaInicial) {
        Circuito circuito = gestorTemporada.getSiguienteCircuito();
        List<Piloto> pilotos = gestorPilotos.listarPilotos();

        // Inicializar estadísticas
        eventosImportantes = new ArrayList<>();
        totalAdelantamientos = 0;
        cambiasClimaEnCarrera = 0;
        tiempoVueltaRapida = Double.MAX_VALUE;

        // Crear resultados iniciales
        List<SimuladorClasificacion.ResultadoVuelta> resultados = new ArrayList<>();
        TipoNeumatico neumaticoInicial = TipoNeumatico.seleccionarOptimo(climaInicial);

        for (Piloto piloto : pilotos) {
            double velocidadBase = calcularVelocidadBase(piloto);
            double tiempo = calcularTiempoBase(velocidadBase, circuito, climaInicial, neumaticoInicial, piloto.getHabilidad());

            SimuladorClasificacion.ResultadoVuelta resultado =
                new SimuladorClasificacion.ResultadoVuelta(piloto.getNombre(), piloto.getEquipo(), tiempo);
            resultado.setNeumatico(neumaticoInicial);

            resultados.add(resultado);
        }

        // Ordenar por tiempo inicial
        resultados.sort(Comparator.comparingDouble(SimuladorClasificacion.ResultadoVuelta::getTiempoVueltaSegundos));

        // Simular las vueltas de la carrera
        Clima climaActual = climaInicial;
        int totalVueltas = circuito.getVueltas();

        for (int vuelta = 1; vuelta <= totalVueltas; vuelta++) {
            // Cambio de clima aleatorio (5% de probabilidad por vuelta)
            if (vuelta > 5 && random.nextDouble() < 0.05) {
                Clima nuevoClima = cambiarClimaAleatorio(climaActual);
                if (nuevoClima != climaActual) {
                    climaActual = nuevoClima;
                    cambiasClimaEnCarrera++;
                    eventosImportantes.add(String.format("Vuelta %d: ¡Cambio de clima a %s!", vuelta, climaActual));
                }
            }

            // Simular eventos en esta vuelta
            simularEventosVuelta(resultados, vuelta, climaActual, circuito);

            // Posibilidad de adelantamientos (más probable en primeras y últimas vueltas)
            if (vuelta < 10 || vuelta > totalVueltas - 10) {
                simularAdelantamientos(resultados, vuelta);
            }
        }

        // Asignar posiciones finales y puntos
        asignarPosicionesYPuntos(resultados);

        // Crear resultado detallado
        return new ResultadoCarreraDetallado(
            resultados,
            climaInicial,
            climaActual,
            cambiasClimaEnCarrera,
            eventosImportantes,
            totalAdelantamientos,
            pilotoVueltaRapida,
            tiempoVueltaRapida
        );
    }

    private double calcularVelocidadBase(Piloto piloto) {
        // Velocidad base según equipo
        double velocidadEquipo = switch (piloto.getEquipo()) {
            case "Red Bull Racing" -> 322.0;
            case "Ferrari" -> 320.0;
            case "McLaren" -> 324.0;
            case "Mercedes-AMG Petronas" -> 318.0;
            case "Aston Martin" -> 312.0;
            case "Alpine" -> 305.0;
            case "Audi" -> 302.0;
            case "Haas" -> 298.0;
            case "Racing Bulls" -> 295.0;
            case "Williams" -> 308.0;
            case "Cadillac" -> 290.0;
            default -> 300.0;
        };

        // Ajustar por habilidad del piloto
        double factorHabilidad = 1.0 + ((piloto.getHabilidad() - 80) / 200.0);

        return velocidadEquipo * factorHabilidad;
    }

    private double calcularTiempoBase(double velocidad, Circuito circuito, Clima clima,
                                     TipoNeumatico neumatico, int habilidad) {
        double distanciaKm = circuito.getLongitudKm();

        // Ajustar velocidad por clima
        double factorClima = switch (clima) {
            case SECO -> 1.0;
            case LLUVIOSO -> 0.85;
            case EXTREMO -> 0.70;
        };

        // Ajustar por neumático
        double factorNeumatico = neumatico.getFactorVelocidad();

        // Variación aleatoria
        double variacion = 0.97 + (random.nextDouble() * 0.06); // 97% - 103%

        // Ajustar por habilidad
        double factorHabilidad = 0.95 + ((100 - habilidad) / 500.0);

        double velocidadFinal = velocidad * factorClima * factorNeumatico * variacion * factorHabilidad;
        double tiempoHoras = distanciaKm / velocidadFinal;

        return tiempoHoras * 3600; // Convertir a segundos
    }

    private void simularEventosVuelta(List<SimuladorClasificacion.ResultadoVuelta> resultados,
                                     int vuelta, Clima clima, Circuito circuito) {
        for (SimuladorClasificacion.ResultadoVuelta resultado : resultados) {
            // Skip si ya está DNF
            if (resultado.isDnf()) continue;

            // Obtener habilidad del piloto
            Piloto piloto = gestorPilotos.listarPilotos().stream()
                .filter(p -> p.getNombre().equals(resultado.getNombrePiloto()))
                .findFirst()
                .orElse(null);

            if (piloto == null) continue;

            // Probabilidad de evento aumenta con:
            // - Clima peor
            // - Circuitos más largos/complejos
            // - Menor habilidad
            double probabilidadEvento = calcularProbabilidadEvento(clima, circuito, piloto.getHabilidad(), vuelta);

            if (random.nextDouble() < probabilidadEvento) {
                EventoCarrera evento = generarEventoAleatorio(clima, piloto.getHabilidad());
                resultado.setEvento(evento);

                if (evento.esAbandonoDNF()) {
                    resultado.marcarDNF(evento.getDescripcion());
                    eventosImportantes.add(String.format("Vuelta %d: %s - %s ❌",
                        vuelta, resultado.getNombrePiloto(), evento.getDescripcion()));
                } else if (evento != EventoCarrera.NORMAL) {
                    double penalizacion = evento.getPenalizacionSegundos();
                    resultado.setTiempoVueltaSegundos(resultado.getTiempoVueltaSegundos() + penalizacion);
                    eventosImportantes.add(String.format("Vuelta %d: %s - %s (+%.1fs)",
                        vuelta, resultado.getNombrePiloto(), evento.getDescripcion(), penalizacion));
                }
            }
        }
    }

    private double calcularProbabilidadEvento(Clima clima, Circuito circuito, int habilidad, int vuelta) {
        double baseProb = switch (clima) {
            case SECO -> 0.03;      // 3% por vuelta
            case LLUVIOSO -> 0.08;  // 8% por vuelta
            case EXTREMO -> 0.15;   // 15% por vuelta
        };

        // Aumentar en circuitos largos
        if (circuito.getLongitudKm() > 6.0) {
            baseProb *= 1.3;
        }

        // Ajustar por habilidad (pilotos hábiles tienen menos accidentes)
        baseProb *= (1.0 - (habilidad / 250.0));

        // Mayor probabilidad en primeras vueltas (batalla inicial)
        if (vuelta <= 3) {
            baseProb *= 2.0;
        }

        return baseProb;
    }

    private EventoCarrera generarEventoAleatorio(Clima clima, int habilidad) {
        double rand = random.nextDouble();

        if (clima == Clima.EXTREMO) {
            if (rand < 0.20) return EventoCarrera.ACCIDENTE_GRAVE;
            if (rand < 0.35) return EventoCarrera.FALLO_MECANICO;
            if (rand < 0.45) return EventoCarrera.FALLO_MOTOR;
            if (rand < 0.55) return EventoCarrera.PROBLEMA_FRENOS;
            if (rand < 0.70) return EventoCarrera.TROMPO;
            if (rand < 0.85) return EventoCarrera.SALIDA_PISTA;
            return EventoCarrera.ACCIDENTE_LEVE;
        } else if (clima == Clima.LLUVIOSO) {
            if (rand < 0.10) return EventoCarrera.ACCIDENTE_GRAVE;
            if (rand < 0.20) return EventoCarrera.FALLO_MECANICO;
            if (rand < 0.40) return EventoCarrera.TROMPO;
            if (rand < 0.60) return EventoCarrera.SALIDA_PISTA;
            if (rand < 0.75) return EventoCarrera.PINCHAZO;
            return EventoCarrera.ACCIDENTE_LEVE;
        } else {
            if (rand < 0.15) return EventoCarrera.FALLO_MECANICO;
            if (rand < 0.25) return EventoCarrera.FALLO_MOTOR;
            if (rand < 0.40) return EventoCarrera.ACCIDENTE_LEVE;
            if (rand < 0.55) return EventoCarrera.SALIDA_PISTA;
            if (rand < 0.70) return EventoCarrera.PINCHAZO;
            if (rand < 0.85) return EventoCarrera.PENALIZACION_5S;
            return EventoCarrera.PENALIZACION_10S;
        }
    }

    private Clima cambiarClimaAleatorio(Clima climaActual) {
        double rand = random.nextDouble();

        return switch (climaActual) {
            case SECO -> {
                if (rand < 0.70) yield Clima.SECO;       // 70% mantiene seco
                if (rand < 0.95) yield Clima.LLUVIOSO;   // 25% pasa a lluvia
                yield Clima.EXTREMO;                      // 5% extremo
            }
            case LLUVIOSO -> {
                if (rand < 0.40) yield Clima.SECO;       // 40% mejora a seco
                if (rand < 0.80) yield Clima.LLUVIOSO;   // 40% mantiene
                yield Clima.EXTREMO;                      // 20% empeora
            }
            case EXTREMO -> {
                if (rand < 0.20) yield Clima.SECO;       // 20% mejora mucho
                if (rand < 0.70) yield Clima.LLUVIOSO;   // 50% mejora a lluvia
                yield Clima.EXTREMO;                      // 30% mantiene extremo
            }
        };
    }

    private void simularAdelantamientos(List<SimuladorClasificacion.ResultadoVuelta> resultados, int vuelta) {
        for (int i = 1; i < resultados.size() - 1; i++) {
            SimuladorClasificacion.ResultadoVuelta actual = resultados.get(i);
            SimuladorClasificacion.ResultadoVuelta anterior = resultados.get(i - 1);

            // Skip si alguno está DNF
            if (actual.isDnf() || anterior.isDnf()) continue;

            // Probabilidad de adelantamiento (5%)
            if (random.nextDouble() < 0.05) {
                // Intercambiar tiempos
                double tempTiempo = actual.getTiempoVueltaSegundos();
                actual.setTiempoVueltaSegundos(anterior.getTiempoVueltaSegundos() + 0.1);
                anterior.setTiempoVueltaSegundos(tempTiempo - 0.1);

                totalAdelantamientos++;
                eventosImportantes.add(String.format("Vuelta %d: ¡%s adelanta a %s!",
                    vuelta, anterior.getNombrePiloto(), actual.getNombrePiloto()));

                // Reordenar
                resultados.sort(Comparator.comparingDouble(SimuladorClasificacion.ResultadoVuelta::getTiempoVueltaSegundos));
                break; // Solo un adelantamiento por vuelta
            }
        }
    }

    private void asignarPosicionesYPuntos(List<SimuladorClasificacion.ResultadoVuelta> resultados) {
        // Ordenar: primero los que finalizaron (por tiempo), luego los DNF
        resultados.sort((r1, r2) -> {
            if (r1.isDnf() && !r2.isDnf()) return 1;
            if (!r1.isDnf() && r2.isDnf()) return -1;
            return Double.compare(r1.getTiempoVueltaSegundos(), r2.getTiempoVueltaSegundos());
        });

        for (int i = 0; i < resultados.size(); i++) {
            resultados.get(i).setPosicion(i + 1);

            // Registrar vuelta más rápida (solo los que finalizaron)
            if (!resultados.get(i).isDnf() &&
                resultados.get(i).getTiempoVueltaSegundos() < tiempoVueltaRapida) {
                tiempoVueltaRapida = resultados.get(i).getTiempoVueltaSegundos();
                pilotoVueltaRapida = resultados.get(i).getNombrePiloto();
            }
        }
    }

    /**
     * Clase para almacenar resultado detallado con estadísticas
     */
    public static class ResultadoCarreraDetallado {
        private List<SimuladorClasificacion.ResultadoVuelta> clasificacion;
        private Clima climaInicial;
        private Clima climaFinal;
        private int cambiosClima;
        private List<String> eventosImportantes;
        private int totalAdelantamientos;
        private String pilotoVueltaRapida;
        private double tiempoVueltaRapida;

        public ResultadoCarreraDetallado(List<SimuladorClasificacion.ResultadoVuelta> clasificacion,
                                        Clima climaInicial, Clima climaFinal, int cambiosClima,
                                        List<String> eventosImportantes, int totalAdelantamientos,
                                        String pilotoVueltaRapida, double tiempoVueltaRapida) {
            this.clasificacion = clasificacion;
            this.climaInicial = climaInicial;
            this.climaFinal = climaFinal;
            this.cambiosClima = cambiosClima;
            this.eventosImportantes = eventosImportantes;
            this.totalAdelantamientos = totalAdelantamientos;
            this.pilotoVueltaRapida = pilotoVueltaRapida;
            this.tiempoVueltaRapida = tiempoVueltaRapida;
        }

        public List<SimuladorClasificacion.ResultadoVuelta> getClasificacion() { return clasificacion; }
        public Clima getClimaInicial() { return climaInicial; }
        public Clima getClimaFinal() { return climaFinal; }
        public int getCambiosClima() { return cambiosClima; }
        public List<String> getEventosImportantes() { return eventosImportantes; }
        public int getTotalAdelantamientos() { return totalAdelantamientos; }
        public String getPilotoVueltaRapida() { return pilotoVueltaRapida; }
        public double getTiempoVueltaRapida() { return tiempoVueltaRapida; }
    }
}
