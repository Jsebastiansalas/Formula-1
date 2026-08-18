package com.formula1.simulacion;

import java.util.Random;

/**
 * Generador de condiciones climaticas realistas para cada circuito.
 * Asigna un perfil climatico (desertico, tropical, templado, lluvioso, variable)
 * segun la ubicacion geografica del circuito, y genera el clima con probabilidades
 * ponderadas acordes a la region.
 */
public class GeneradorClima {
    private static final Random random = new Random();

    /**
     * Enum interno que define perfiles climaticos con probabilidades para cada tipo de clima.
     * Cada perfil tiene probabilidades que suman 1.0 para seco, lluvioso y extremo.
     */
    public enum PerfilClimatico {
        /** Circuitos en desierto: casi siempre seco (Bahrain, Arabia Saudita, Abu Dhabi) */
        DESERTICO(0.95, 0.04, 0.01),
        /** Circuitos tropicales: alta humedad y lluvias frecuentes (Singapur, Miami, Brasil) */
        TROPICAL(0.65, 0.25, 0.10),
        /** Circuitos europeos: clima estable con lluvias ocasionales */
        TEMPLADO(0.75, 0.20, 0.05),
        /** Circuitos con alta probabilidad de lluvia (Silverstone, Spa, Zandvoort) */
        LLUVIOSO(0.55, 0.35, 0.10),
        /** Perfil por defecto para circuitos no clasificados */
        VARIABLE(0.70, 0.25, 0.05);

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

    /**
     * Genera el clima para un circuito basado en su perfil geografico.
     * @param nombreCircuito nombre del circuito (se usa para determinar el perfil)
     * @return clima generado aleatoriamente con probabilidades ponderadas
     */
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
