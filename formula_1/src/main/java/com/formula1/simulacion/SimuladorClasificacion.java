package com.formula1.simulacion;

import com.formula1.modelo.Circuito;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimuladorClasificacion {
    private Random random;

    public SimuladorClasificacion() {
        this.random = new Random();
    }

    // Resultado de un piloto en la sesión (mejorado con más datos)
    public static class ResultadoVuelta {
        private String nombrePiloto;
        private String equipo;
        private double tiempoVueltaSegundos;
        private int posicion;
        private int puntos;
        private TipoNeumatico neumatico;

        public ResultadoVuelta(String nombrePiloto, String equipo, double tiempoVueltaSegundos) {
            this.nombrePiloto = nombrePiloto;
            this.equipo = equipo;
            this.tiempoVueltaSegundos = tiempoVueltaSegundos;
            this.posicion = 0;
            this.puntos = 0;
            this.neumatico = null;
        }

        public String getNombrePiloto() { return nombrePiloto; }
        public String getEquipo() { return equipo; }
        public double getTiempoVueltaSegundos() { return tiempoVueltaSegundos; }
        public int getPosicion() { return posicion; }
        public int getPuntos() { return puntos; }
        public TipoNeumatico getNeumatico() { return neumatico; }

        public void setPosicion(int posicion) {
            this.posicion = posicion;
            this.puntos = SistemaPuntos.obtenerPuntos(posicion);
        }
        public void setNeumatico(TipoNeumatico neumatico) { this.neumatico = neumatico; }

        @Override
        public String toString() {
            int minutos = (int) (tiempoVueltaSegundos / 60);
            double segundos = tiempoVueltaSegundos % 60;
            String neumaticoStr = neumatico != null ? " [" + neumatico + "]" : "";
            return String.format("P%d: %s (%s) - %d:%06.3f%s | %d pts",
                    posicion, nombrePiloto, equipo, minutos, segundos, neumaticoStr, puntos);
        }
    }

    // --- DATOS DE PRUEBA (temporal, hasta que exista Vehiculo/Piloto real) ---
    // nombrePiloto, equipo, velocidadPromedioKmh del vehículo en modo NORMAL
    public List<ResultadoVuelta> simularConDatosPrueba(Circuito circuito, Clima clima) {
        List<Object[]> pilotosPrueba = List.of(
                new Object[]{"Max Verstappen", "Red Bull Racing", 322.0},
                new Object[]{"Charles Leclerc", "Ferrari", 320.0},
                new Object[]{"Lando Norris", "McLaren", 325.0},
                new Object[]{"George Russell", "Mercedes", 318.0},
                new Object[]{"Lewis Hamilton", "Ferrari", 319.0},
                new Object[]{"Oscar Piastri", "McLaren", 323.0},
                new Object[]{"Fernando Alonso", "Aston Martin", 312.0},
                new Object[]{"Carlos Sainz", "Williams", 308.0},
                new Object[]{"Sergio Pérez", "Cadillac", 290.0},
                new Object[]{"Pierre Gasly", "Alpine", 305.0},
                new Object[]{"Lance Stroll", "Aston Martin", 310.0},
                new Object[]{"Alexander Albon", "Williams", 307.0},
                new Object[]{"Kimi Antonelli", "Mercedes", 316.0},
                new Object[]{"Esteban Ocon", "Haas", 298.0},
                new Object[]{"Nico Hülkenberg", "Audi", 302.0},
                new Object[]{"Liam Lawson", "Racing Bulls", 295.0},
                new Object[]{"Valtteri Bottas", "Cadillac", 288.0},
                new Object[]{"Franco Colapinto", "Alpine", 303.0},
                new Object[]{"Gabriel Bortoleto", "Audi", 299.0},
                new Object[]{"Oliver Bearman", "Haas", 296.0},
                new Object[]{"Isack Hadjar", "Red Bull Racing", 315.0},
                new Object[]{"Arvid Lindblad", "Racing Bulls", 292.0}
        );

        // Seleccionar neumático óptimo para el clima
        TipoNeumatico neumaticoOptimo = TipoNeumatico.seleccionarOptimo(clima);

        List<ResultadoVuelta> resultados = new ArrayList<>();
        for (Object[] p : pilotosPrueba) {
            String nombre = (String) p[0];
            String equipo = (String) p[1];
            double velocidadPromedio = (double) p[2];

            // Calcular tiempo con variación aleatoria para realismo
            double variacion = 0.97 + (random.nextDouble() * 0.06); // entre 97% y 103%
            double velocidadAjustada = velocidadPromedio * variacion;

            double tiempo = calcularTiempoVuelta(velocidadAjustada, circuito, clima, neumaticoOptimo);
            ResultadoVuelta resultado = new ResultadoVuelta(nombre, equipo, tiempo);
            resultado.setNeumatico(neumaticoOptimo);
            resultados.add(resultado);
        }

        return asignarPosicionesYPuntos(ordenarPorTiempo(resultados));
    }

    // Cálculo de tiempo de vuelta (RF21): distancia del circuito / velocidad,
    // ajustado por clima y tipo de neumático
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

    // Ordena resultados de menor a mayor tiempo -> el primero es la pole (RF22, RF23)
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
