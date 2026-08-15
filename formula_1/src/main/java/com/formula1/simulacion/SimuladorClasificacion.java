package com.formula1.simulacion;

import com.formula1.modelo.Circuito;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimuladorClasificacion {

    // Resultado de un piloto en la sesión
    public static class ResultadoVuelta {
        private String nombrePiloto;
        private double tiempoVueltaSegundos;

        public ResultadoVuelta(String nombrePiloto, double tiempoVueltaSegundos) {
            this.nombrePiloto = nombrePiloto;
            this.tiempoVueltaSegundos = tiempoVueltaSegundos;
        }

        public String getNombrePiloto() { return nombrePiloto; }
        public double getTiempoVueltaSegundos() { return tiempoVueltaSegundos; }

        @Override
        public String toString() {
            int minutos = (int) (tiempoVueltaSegundos / 60);
            double segundos = tiempoVueltaSegundos % 60;
            return String.format("%s - %d:%06.3f", nombrePiloto, minutos, segundos);
        }
    }

    // --- DATOS DE PRUEBA (temporal, hasta que exista Vehiculo/Piloto real) ---
    // nombrePiloto, velocidadPromedioKmh del vehículo en modo NORMAL
    public List<ResultadoVuelta> simularConDatosPrueba(Circuito circuito, Clima clima) {
        List<Object[]> pilotosPrueba = List.of(
                new Object[]{"Max Verstappen", 322.0},
                new Object[]{"Charles Leclerc", 320.0},
                new Object[]{"Lando Norris", 325.0},
                new Object[]{"George Russell", 318.0}
        );

        List<ResultadoVuelta> resultados = new ArrayList<>();
        for (Object[] p : pilotosPrueba) {
            String nombre = (String) p[0];
            double velocidadPromedio = (double) p[1];
            double tiempo = calcularTiempoVuelta(velocidadPromedio, circuito, clima);
            resultados.add(new ResultadoVuelta(nombre, tiempo));
        }

        return ordenarPorTiempo(resultados);
    }

    // Cálculo de tiempo de vuelta (RF21): distancia del circuito / velocidad,
    // ajustado por un factor según el clima.
    public double calcularTiempoVuelta(double velocidadPromedioKmh, Circuito circuito, Clima clima) {
        double factorClima = switch (clima) {
            case SECO -> 1.0;
            case LLUVIOSO -> 1.12;   // 12% más lento
            case EXTREMO -> 1.25;    // 25% más lento
        };

        double velocidadAjustada = velocidadPromedioKmh / factorClima;
        double horas = circuito.getLongitudKm() / velocidadAjustada;
        return horas * 3600; // convertir horas a segundos
    }

    // Ordena resultados de menor a mayor tiempo -> el primero es la pole (RF22, RF23)
    public List<ResultadoVuelta> ordenarPorTiempo(List<ResultadoVuelta> resultados) {
        List<ResultadoVuelta> copia = new ArrayList<>(resultados);
        copia.sort(Comparator.comparingDouble(ResultadoVuelta::getTiempoVueltaSegundos));
        return copia;
    }
}
