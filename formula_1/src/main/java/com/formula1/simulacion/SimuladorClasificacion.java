package com.formula1.simulacion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.formula1.gestor.GestorVehiculos;
import com.formula1.modelo.Circuito;
import com.formula1.modelo.Piloto;
import com.formula1.modelo.Vehiculo;

/**
 * Motor principal de simulacion de carreras de Formula 1.
 * Calcula tiempos de vuelta basados en velocidad del vehiculo, habilidad del piloto,
 * condiciones climaticas y tipo de neumatico. Genera eventos aleatorios (accidentes,
 * fallos mecanicos, penalizaciones) y determina la clasificacion final con puntos.
 * Soporta dos modos: datos de prueba (22 pilotos hardcodeados) y datos reales (desde JSON).
 */
public class SimuladorClasificacion {
    private Random random;

    public SimuladorClasificacion() {
        this.random = new Random();
    }

    /**
     * Clase interna que representa el resultado de un piloto en una carrera.
     * Contiene tiempo de vuelta, posicion final, puntos, neumatico usado,
     * evento ocurrido y estado (finalizado o DNF con razon).
     */
    public static class ResultadoVuelta {
        private String nombrePiloto;
        private String equipo;
        private double tiempoVueltaSegundos;
        private int posicion;
        private int puntos;
        private TipoNeumatico neumatico;
        private EventoCarrera evento;
        private boolean dnf;  // Did Not Finish
        private String estadoFinal;

        public ResultadoVuelta(String nombrePiloto, String equipo, double tiempoVueltaSegundos) {
            this.nombrePiloto = nombrePiloto;
            this.equipo = equipo;
            this.tiempoVueltaSegundos = tiempoVueltaSegundos;
            this.posicion = 0;
            this.puntos = 0;
            this.neumatico = null;
            this.evento = EventoCarrera.NORMAL;
            this.dnf = false;
            this.estadoFinal = "Finalizado";
        }

        public String getNombrePiloto() { return nombrePiloto; }
        public String getEquipo() { return equipo; }
        public double getTiempoVueltaSegundos() { return tiempoVueltaSegundos; }
        public int getPosicion() { return posicion; }
        public int getPuntos() { return puntos; }
        public TipoNeumatico getNeumatico() { return neumatico; }
        public EventoCarrera getEvento() { return evento; }
        public boolean isDnf() { return dnf; }
        public String getEstadoFinal() { return estadoFinal; }

        public void setPosicion(int posicion) {
            this.posicion = posicion;
            this.puntos = dnf ? 0 : SistemaPuntos.obtenerPuntos(posicion);
        }
        public void setNeumatico(TipoNeumatico neumatico) { this.neumatico = neumatico; }
        public void setEvento(EventoCarrera evento) { this.evento = evento; }
        public void setTiempoVueltaSegundos(double tiempo) { this.tiempoVueltaSegundos = tiempo; }

    /**
     * Marca al piloto como DNF (abandono) con una razon especifica.
     * Pone el tiempo en MAX_VALUE para que quede al final de la clasificacion.
     * @param razon descripcion de la causa del abandono
     */
    public void marcarDNF(String razon) {
            this.dnf = true;
            this.estadoFinal = razon;
            this.puntos = 0;
            this.tiempoVueltaSegundos = Double.MAX_VALUE; // Para que queden al final
        }

        @Override
        public String toString() {
            if (dnf) {
                return String.format("P%d: %s (%s) - DNF [%s]",
                        posicion, nombrePiloto, equipo, estadoFinal);
            }

            int minutos = (int) (tiempoVueltaSegundos / 60);
            double segundos = tiempoVueltaSegundos % 60;
            String neumaticoStr = neumatico != null ? " [" + neumatico + "]" : "";
            String eventoStr = evento != EventoCarrera.NORMAL ? " ⚠ " + evento.getDescripcion() : "";
            return String.format("P%d: %s (%s) - %d:%06.3f%s | %d pts%s",
                    posicion, nombrePiloto, equipo, minutos, segundos, neumaticoStr, puntos, eventoStr);
        }
    }

    // --- DATOS DE PRUEBA: 22 pilotos reales de la temporada 2026 como respaldo ---

    /**
     * Simula una carrera usando datos de prueba hardcodeados (22 pilotos F1 2026).
     * Se usa como respaldo cuando no se han configurado datos reales desde JSON.
     * @param circuito circuito donde se corre la carrera
     * @param clima condiciones climaticas
     * @return lista de resultados ordenados por posicion (1 = ganador)
     */
    public List<ResultadoVuelta> simularConDatosPrueba(Circuito circuito, Clima clima) {
        List<Object[]> pilotosPrueba = List.of(
                new Object[]{"Max Verstappen", "Red Bull Racing", 322.0, 98.0},
                new Object[]{"Charles Leclerc", "Ferrari", 320.0, 94.0},
                new Object[]{"Lando Norris", "McLaren", 325.0, 95.0},
                new Object[]{"George Russell", "Mercedes", 318.0, 91.0},
                new Object[]{"Lewis Hamilton", "Ferrari", 319.0, 93.0},
                new Object[]{"Oscar Piastri", "McLaren", 323.0, 92.0},
                new Object[]{"Fernando Alonso", "Aston Martin", 312.0, 90.0},
                new Object[]{"Carlos Sainz", "Williams", 308.0, 88.0},
                new Object[]{"Sergio Pérez", "Cadillac", 290.0, 84.0},
                new Object[]{"Pierre Gasly", "Alpine", 305.0, 84.0},
                new Object[]{"Lance Stroll", "Aston Martin", 310.0, 78.0},
                new Object[]{"Alexander Albon", "Williams", 307.0, 85.0},
                new Object[]{"Kimi Antonelli", "Mercedes", 316.0, 85.0},
                new Object[]{"Esteban Ocon", "Haas", 298.0, 82.0},
                new Object[]{"Nico Hülkenberg", "Audi", 302.0, 83.0},
                new Object[]{"Liam Lawson", "Racing Bulls", 295.0, 81.0},
                new Object[]{"Valtteri Bottas", "Cadillac", 288.0, 82.0},
                new Object[]{"Franco Colapinto", "Alpine", 303.0, 80.0},
                new Object[]{"Gabriel Bortoleto", "Audi", 299.0, 79.0},
                new Object[]{"Oliver Bearman", "Haas", 296.0, 80.0},
                new Object[]{"Isack Hadjar", "Red Bull Racing", 315.0, 82.0},
                new Object[]{"Arvid Lindblad", "Racing Bulls", 292.0, 76.0}
        );

        // Seleccionar neumático óptimo para el clima
        TipoNeumatico neumaticoOptimo = TipoNeumatico.seleccionarOptimo(clima);

        List<ResultadoVuelta> resultados = new ArrayList<>();
        for (Object[] p : pilotosPrueba) {
            String nombre = (String) p[0];
            String equipo = (String) p[1];
            double velocidadPromedio = (double) p[2];
            double habilidad = (double) p[3];

            // Calcular tiempo base con variación por habilidad y aleatoriedad
            double variacionHabilidad = 0.95 + ((100 - habilidad) / 1000.0); // Mejor habilidad = menos variación
            double variacionAleatoria = 0.98 + (random.nextDouble() * 0.04); // Entre 98% y 102%
            double velocidadAjustada = velocidadPromedio * variacionAleatoria / variacionHabilidad;

            // Calcular tiempo inicial
            double tiempo = calcularTiempoVuelta(velocidadAjustada, circuito, clima, neumaticoOptimo);

            ResultadoVuelta resultado = new ResultadoVuelta(nombre, equipo, tiempo);
            resultado.setNeumatico(neumaticoOptimo);

            // Simular eventos aleatorios (más probabilidad en clima extremo)
            EventoCarrera evento = simularEventoAleatorio(clima, habilidad);
            resultado.setEvento(evento);

            if (evento.esAbandonoDNF()) {
                // Piloto abandona la carrera
                resultado.marcarDNF(evento.getDescripcion());
            } else if (evento != EventoCarrera.NORMAL) {
                // Agregar penalización de tiempo
                resultado.setTiempoVueltaSegundos(tiempo + evento.getPenalizacionSegundos());
            }

            resultados.add(resultado);
        }

        return asignarPosicionesYPuntos(ordenarPorTiempo(resultados));
    }

    // --- DATOS REALES: usa Piloto y Vehiculo cargados desde JSON ---

    /**
     * Simula una carrera usando datos reales de pilotos y vehiculos cargados desde JSON.
     * Busca el vehiculo de cada piloto por nombre de equipo.
     * @param circuito circuito donde se corre
     * @param clima condiciones climaticas
     * @param pilotos lista de pilotos participantes
     * @param gestorVehiculos gestor para buscar el vehiculo de cada equipo
     * @return lista de resultados ordenados por posicion
     */
    public List<ResultadoVuelta> simularConDatosReales(Circuito circuito, Clima clima,
                                                         List<Piloto> pilotos, GestorVehiculos gestorVehiculos) {
        TipoNeumatico neumaticoOptimo = TipoNeumatico.seleccionarOptimo(clima);
        List<ResultadoVuelta> resultados = new ArrayList<>();

        for (Piloto piloto : pilotos) {
            Vehiculo vehiculo = buscarVehiculoDelEquipo(piloto.getEquipo(), gestorVehiculos);
            if (vehiculo == null) {
                System.out.println("Sin vehículo para el equipo de " + piloto.getNombre() + ", se omite.");
                continue;
            }

            double velocidadPromedio = obtenerVelocidadNormal(vehiculo);
            double habilidad = piloto.getHabilidad();

            double variacionHabilidad = 0.95 + ((100 - habilidad) / 1000.0);
            double variacionAleatoria = 0.98 + (random.nextDouble() * 0.04);
            double velocidadAjustada = velocidadPromedio * variacionAleatoria / variacionHabilidad;

            double tiempo = calcularTiempoVuelta(velocidadAjustada, circuito, clima, neumaticoOptimo);

            ResultadoVuelta resultado = new ResultadoVuelta(piloto.getNombre(), piloto.getEquipo(), tiempo);
            resultado.setNeumatico(neumaticoOptimo);

            EventoCarrera evento = simularEventoAleatorio(clima, habilidad);
            resultado.setEvento(evento);

            if (evento.esAbandonoDNF()) {
                resultado.marcarDNF(evento.getDescripcion());
            } else if (evento != EventoCarrera.NORMAL) {
                resultado.setTiempoVueltaSegundos(tiempo + evento.getPenalizacionSegundos());
            }

            resultados.add(resultado);
        }

        return asignarPosicionesYPuntos(ordenarPorTiempo(resultados));
    }

    private Vehiculo buscarVehiculoDelEquipo(String nombreEquipo, GestorVehiculos gestorVehiculos) {
        for (Vehiculo v : gestorVehiculos.listarVehiculos()) {
            if (v.getEquipo() != null && v.getEquipo().equals(nombreEquipo)) {
                return v;
            }
        }
        return null;
    }

    private double obtenerVelocidadNormal(Vehiculo vehiculo) {
        Vehiculo.RendimientoModo modoNormal = vehiculo.getRendimiento().get("conduccion_normal");
        if (modoNormal == null) {
            return vehiculo.getVelocidadMaximaKmh() * 0.9; // fallback si falta ese modo
        }
        return modoNormal.getVelocidadPromedioKmh();
    }

    /**
     * Simula eventos aleatorios durante la carrera basados en clima y habilidad.
     * Mayor probabilidad de incidente en peor clima y menor habilidad del piloto.
     * @param clima condiciones climaticas (afecta probabilidad base)
     * @param habilidad habilidad del piloto (reduce probabilidad hasta 50%)
     * @return el evento ocurrido (NORMAL si no hubo incidente)
     */
    private EventoCarrera simularEventoAleatorio(Clima clima, double habilidad) {
        // Probabilidad base de incidente (más alta en peor clima y menor habilidad)
        double probabilidadIncidente = switch (clima) {
            case SECO -> 0.08;      // 8% de incidente en seco
            case LLUVIOSO -> 0.18;  // 18% en lluvia
            case EXTREMO -> 0.30;   // 30% en condiciones extremas
        };

        // Ajustar por habilidad del piloto (pilotos más hábiles tienen menos incidentes)
        probabilidadIncidente *= (1.0 - (habilidad / 200.0)); // Reduce hasta 50% para habilidad 100

        double rand = random.nextDouble();

        if (rand > probabilidadIncidente) {
            return EventoCarrera.NORMAL; // Sin incidentes
        }

        // Ocurrió un incidente, determinar cuál
        double tipoIncidente = random.nextDouble();

        if (clima == Clima.EXTREMO) {
            // En clima extremo, más probabilidad de accidentes graves
            if (tipoIncidente < 0.15) return EventoCarrera.ACCIDENTE_GRAVE;
            if (tipoIncidente < 0.25) return EventoCarrera.FALLO_MECANICO;
            if (tipoIncidente < 0.35) return EventoCarrera.PROBLEMA_FRENOS;
            if (tipoIncidente < 0.55) return EventoCarrera.TROMPO;
            if (tipoIncidente < 0.75) return EventoCarrera.SALIDA_PISTA;
            return EventoCarrera.ACCIDENTE_LEVE;
        } else if (clima == Clima.LLUVIOSO) {
            // En lluvia, más trompos y salidas de pista
            if (tipoIncidente < 0.08) return EventoCarrera.ACCIDENTE_GRAVE;
            if (tipoIncidente < 0.15) return EventoCarrera.FALLO_MECANICO;
            if (tipoIncidente < 0.35) return EventoCarrera.TROMPO;
            if (tipoIncidente < 0.60) return EventoCarrera.SALIDA_PISTA;
            if (tipoIncidente < 0.75) return EventoCarrera.ACCIDENTE_LEVE;
            if (tipoIncidente < 0.85) return EventoCarrera.PINCHAZO;
            return EventoCarrera.PENALIZACION_5S;
        } else {
            // En seco, menos graves pero aún posibles
            if (tipoIncidente < 0.05) return EventoCarrera.ACCIDENTE_GRAVE;
            if (tipoIncidente < 0.12) return EventoCarrera.FALLO_MECANICO;
            if (tipoIncidente < 0.18) return EventoCarrera.FALLO_MOTOR;
            if (tipoIncidente < 0.30) return EventoCarrera.PINCHAZO;
            if (tipoIncidente < 0.45) return EventoCarrera.ACCIDENTE_LEVE;
            if (tipoIncidente < 0.60) return EventoCarrera.SALIDA_PISTA;
            if (tipoIncidente < 0.75) return EventoCarrera.PENALIZACION_5S;
            if (tipoIncidente < 0.85) return EventoCarrera.PENALIZACION_10S;
            return EventoCarrera.TROMPO;
        }
    }

    /**
     * Calcula el tiempo de vuelta en segundos.
     * Formula: tiempo = (distancia / velocidadAjustada) * 3600
     * La velocidad se ajusta por factores de clima y neumatico.
     * Penalizacion extra del 15% si el neumatico no es apropiado para el clima.
     * @param velocidadPromedioKmh velocidad base del vehiculo
     * @param circuito circuito (para obtener longitud)
     * @param clima condiciones climaticas
     * @param neumatico tipo de neumatico equipado
     * @return tiempo de vuelta en segundos
     */
    public double calcularTiempoVuelta(double velocidadPromedioKmh, Circuito circuito,
                                       Clima clima, TipoNeumatico neumatico) {
        // Factor por clima
        double factorClima = switch (clima) {
            case SECO -> 1.0;
            case LLUVIOSO -> 1.12;   // 12% más lento
            case EXTREMO -> 1.25;    // 25% más lento
        };

        // Factor por neumático
        double factorNeumatico = neumatico.getFactorVelocidad();

        // Penalización si el neumático no es apropiado para el clima
        if (!neumatico.esApropiadoPara(clima)) {
            factorClima *= 1.15; // 15% más lento si usa neumático inadecuado
        }

        double velocidadAjustada = (velocidadPromedioKmh * factorNeumatico) / factorClima;
        double horas = circuito.getLongitudKm() / velocidadAjustada;
        return horas * 3600; // convertir horas a segundos
    }

    /**
     * Ordena los resultados de menor a mayor tiempo (el primero es el ganador).
     * Los pilotos con DNF (tiempo MAX_VALUE) quedan automaticamente al final.
     * @param resultados lista de resultados sin ordenar
     * @return nueva lista ordenada por tiempo ascendente
     */
    public List<ResultadoVuelta> ordenarPorTiempo(List<ResultadoVuelta> resultados) {
        List<ResultadoVuelta> copia = new ArrayList<>(resultados);
        copia.sort(Comparator.comparingDouble(ResultadoVuelta::getTiempoVueltaSegundos));
        return copia;
    }

    // Asigna posiciones y puntos a los resultados ordenados
    private List<ResultadoVuelta> asignarPosicionesYPuntos(List<ResultadoVuelta> resultadosOrdenados) {
        for (int i = 0; i < resultadosOrdenados.size(); i++) {
            resultadosOrdenados.get(i).setPosicion(i + 1);
        }
        return resultadosOrdenados;
    }
}