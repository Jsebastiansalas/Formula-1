package com.formula1.simulacion;

import java.util.Random;

public class GeneradorClima {
    private static final Random random = new Random();

    // Probabilidades de clima según la región/época del circuito
    public enum PerfilClimatico {
        DESERTICO(0.95, 0.04, 0.01),        // Bahréin, Arabia Saudita, Abu Dhabi
        TROPICAL(0.65, 0.25, 0.10),         // Singapur, Miami, Brasil
        TEMPLADO(0.75, 0.20, 0.05),         // Europa (mayoría)
        LLUVIOSO(0.55, 0.35, 0.10),         // Reino Unido, Bélgica, Países Bajos
        VARIABLE(0.70, 0.25, 0.05);         // Resto

        private final double probSeco;
        private final double probLluvioso;
        private final double probExtremo;

        PerfilClimatico(double probSeco, double probLluvioso, double probExtremo) {
            this.probSeco = probSeco;
            this.probLluvioso = probLluvioso;
            this.probExtremo = probExtremo;
        }

        public Clima generarClima() {
            double rand = random.nextDouble();
            if (rand < probSeco) {
                return Clima.SECO;
            } else if (rand < probSeco + probLluvioso) {
                return Clima.LLUVIOSO;
            } else {
                return Clima.EXTREMO;
            }
        }
    }

    // Asigna perfil climático según el nombre del circuito
    public static Clima generarClimaPorCircuito(String nombreCircuito) {
        PerfilClimatico perfil = obtenerPerfilClimatico(nombreCircuito);
        return perfil.generarClima();
    }

    private static PerfilClimatico obtenerPerfilClimatico(String nombreCircuito) {
        // Circuitos desérticos
        if (nombreCircuito.contains("Bahrain") || nombreCircuito.contains("Jeddah") ||
            nombreCircuito.contains("Yas Marina") || nombreCircuito.contains("Lusail")) {
            return PerfilClimatico.DESERTICO;
        }

        // Circuitos tropicales
        if (nombreCircuito.contains("Marina Bay") || nombreCircuito.contains("Miami") ||
            nombreCircuito.contains("Interlagos") || nombreCircuito.contains("Hermanos Rodríguez")) {
            return PerfilClimatico.TROPICAL;
        }

        // Circuitos lluviosos
        if (nombreCircuito.contains("Silverstone") || nombreCircuito.contains("Spa") ||
            nombreCircuito.contains("Zandvoort")) {
            return PerfilClimatico.LLUVIOSO;
        }

        // Circuitos templados (Europa)
        if (nombreCircuito.contains("Monza") || nombreCircuito.contains("Mónaco") ||
            nombreCircuito.contains("Barcelona") || nombreCircuito.contains("Madring") ||
            nombreCircuito.contains("Red Bull Ring") || nombreCircuito.contains("Hungaroring")) {
            return PerfilClimatico.TEMPLADO;
        }

        // Resto - variable
        return PerfilClimatico.VARIABLE;
    }
}
