package com.formula1.simulacion;

import com.formula1.modelo.Circuito;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sistema de comentaristas para narrar las carreras de F1 en tiempo real
 */
public class Comentarista {
    private static final Random random = new Random();

    // Frases de inicio de carrera
    private static final String[] INICIO_CARRERA = {
        "¡Buenos días a todos los aficionados! Estamos a punto de presenciar una carrera emocionante en %s.",
        "¡Bienvenidos a %s! El clima está %s y los pilotos están listos en la parrilla.",
        "¡Arranca la adrenalina en %s! Las luces están a punto de apagarse.",
        "¡Estamos en vivo desde %s! Una jornada prometedora nos espera en el circuito de %s.",
        "¡Atención aficionados! La carrera en %s está por comenzar. El clima muestra condiciones %s.",
        "¡Qué espectáculo nos espera hoy en %s! Los motores rugen en la parrilla.",
        "¡Desde %s les damos la bienvenida! Prepárense para una batalla épica.",
        "¡Luces rojas encendidas en %s! Los 22 pilotos están listos para la acción.",
        "¡Comienza la cuenta regresiva en %s! El público está enloquecido.",
        "¡La tensión es palpable en %s! Todos los ojos están en la pole position."
    };

    // Comentarios sobre el clima
    private static final String[] CLIMA_SECO = {
        "Tenemos un día perfecto para las carreras, cielo despejado y pista seca.",
        "Condiciones ideales hoy, esto favorecerá las altas velocidades.",
        "El sol brilla sobre el circuito, será una batalla de pura velocidad.",
        "Pista seca y limpia, los pilotos podrán exprimir al máximo sus monoplazas.",
        "Las condiciones no podrían ser mejores, esperamos tiempos de vuelta increíbles.",
        "Clima perfecto para los amantes de la velocidad pura.",
        "La pista está en condiciones óptimas, veremos adelantamientos emocionantes."
    };

    private static final String[] CLIMA_LLUVIA = {
        "¡Atención! La lluvia está cayendo sobre el circuito. Esto cambiará completamente la estrategia.",
        "Condiciones difíciles hoy. Los pilotos tendrán que demostrar su maestría bajo la lluvia.",
        "La lluvia hace acto de presencia. Veremos quién tiene el mejor control en mojado.",
        "La pista está mojada y traicionera. Cada curva es un desafío.",
        "¡Lluvia en el circuito! Los neumáticos de lluvia serán fundamentales.",
        "Condiciones complicadas con la lluvia. Aquí es donde los grandes brillan.",
        "La pista está resbaladiza, cualquier error puede ser fatal.",
        "¡Lluvia torrencial! La visibilidad es limitada, esto será una prueba de nervios."
    };

    private static final String[] CLIMA_EXTREMO = {
        "¡Condiciones extremas! La lluvia torrencial podría obligar a los comisarios a detener la carrera.",
        "Visibilidad casi nula. Los pilotos están arriesgando mucho en cada curva.",
        "¡Esto es peligroso! El agua en la pista está complicando todo.",
        "¡Condiciones límite! Algunos pilotos están pidiendo bandera roja.",
        "¡Diluvio en el circuito! Esto es una lotería, cualquier cosa puede pasar.",
        "¡Condiciones impracticables! Los directores de carrera están evaluando la situación.",
        "¡Aquaplaning en varios sectores! Los pilotos apenas pueden ver.",
        "¡Safety car virtual activado! Las condiciones son extremadamente peligrosas."
    };

    // Comentarios de accidentes
    private static final String[] ACCIDENTE_GRAVE = {
        "¡Oh no! ¡%s ha tenido un accidente terrible en la curva! Los comisarios agitan las banderas amarillas.",
        "¡Impacto fuerte de %s! El coche está destrozado, esperamos que el piloto esté bien.",
        "¡%s se va contra las barreras! Esto es un DNF seguro, qué lástima.",
        "¡Accidente de %s! El safety car tendrá que salir a pista.",
        "¡Trompazo de %s! Vaya manera de terminar la carrera para el piloto de %s.",
        "¡%s impacta violentamente! Los médicos están en camino.",
        "¡Bandera roja! ¡Accidente grave de %s! La carrera se detiene.",
        "¡%s pierde el control completamente! Impacto contra el muro de protección.",
        "¡Terrible accidente! %s sale del monoplaza, parece estar bien pero la carrera terminó para él.",
        "¡%s choca fuertemente! Los restos del coche están por toda la pista."
    };

    private static final String[] ACCIDENTE_LEVE = {
        "¡%s se va largo en la curva! Pierde varias posiciones pero continúa.",
        "%s comete un error y toca los límites de pista. Perderá tiempo valioso.",
        "¡Pequeño toque de %s! Nada grave, pero le costará unas posiciones.",
        "%s se bloquea en la frenada, ¡pero logra mantener el control!",
        "¡%s roza el muro pero continúa! Eso estuvo cerca.",
        "¡Toque entre ruedas! %s tiene que recalcular su estrategia.",
        "%s sale de la pista momentáneamente, pierde momentum.",
        "¡Error de pilotaje de %s! Eso le costará en la clasificación final."
    };

    // Comentarios de fallos mecánicos
    private static final String[] FALLO_MECANICO = {
        "¡Problemas para %s! Humo saliendo del coche, esto no pinta bien.",
        "¡%s se detiene en la pista! Parece un fallo mecánico grave.",
        "La transmisión de %s ha cedido. Fin de carrera para el piloto de %s.",
        "¡Motor roto para %s! Qué decepción después de una buena carrera.",
        "%s abandona. Los mecánicos tendrán trabajo esta noche.",
        "¡Fallo eléctrico en el monoplaza de %s! No puede continuar.",
        "¡%s reporta pérdida de potencia! Tiene que abandonar.",
        "¡La caja de cambios de %s ha fallado! DNF para %s.",
        "¡Problemas hidráulicos para %s! El coche no responde.",
        "¡%s pierde todas las marchas! Tiene que retirarse a boxes."
    };

    // Comentarios de adelantamientos
    private static final String[] ADELANTAMIENTO = {
        "¡Espectacular maniobra de %s sobre %s! ¡Qué piloto!",
        "¡%s encuentra el hueco y adelanta a %s por el interior!",
        "¡Adelantamiento limpio de %s! Ahora está en posición %d.",
        "¡%s no perdona y pasa a %s con autoridad!",
        "¡Qué valentía de %s! Le quita la posición a %s.",
        "¡Maniobra arriesgada de %s! Adelanta a %s por el exterior!",
        "¡%s aprovecha el rebufo y supera a %s!",
        "¡Adelantamiento de libro! %s deja atrás a %s.",
        "¡%s finta y se lanza! Sobrepasa a %s brillantemente.",
        "¡DRS activado! %s pasa a %s como si estuviera parado."
    };

    // Comentarios de liderato
    private static final String[] LIDER = {
        "%s lidera la carrera con puño de hierro. ¡Ritmo implacable!",
        "El líder %s está volando. Nadie puede seguir su ritmo.",
        "%s en cabeza, cada vuelta más rápido. ¡Impresionante!",
        "Dominio absoluto de %s. Va camino de la victoria.",
        "%s controla desde la punta. ¡Qué exhibición!",
        "¡%s abre brecha! Nadie puede alcanzarlo.",
        "El ritmo de %s es estratosférico. Está en otra liga.",
        "%s marca el ritmo de la carrera. Los demás solo pueden seguirlo.",
        "¡Clase magistral de %s! Lidera con autoridad.",
        "%s gestiona la ventaja perfectamente. Camino al triunfo."
    };

    // Comentarios de neumáticos
    private static final String[] NEUMATICOS_BLANDOS = {
        "Los neumáticos blandos están dando ventaja en velocidad, pero el desgaste será crucial.",
        "Estrategia agresiva con neumáticos blandos. Veremos si aguantan hasta el final.",
        "Los blandos están funcionando perfectamente en estas condiciones.",
        "Neumáticos blandos, máximo agarre pero menos durabilidad.",
        "Los compuestos blandos les dan velocidad punta, pero tendrán que cuidarlos."
    };

    private static final String[] NEUMATICOS_DUROS = {
        "Estrategia conservadora con neumáticos duros. Jugarán a largo plazo.",
        "Los duros no tienen el agarre inicial, pero serán fundamentales al final.",
        "Paciencia con los duros. La carrera se decide en las últimas vueltas.",
        "Neumáticos duros, sacrifican velocidad por durabilidad.",
        "Los compuestos duros les permitirán ir más lejos sin paradas."
    };

    // Comentarios de final de carrera
    private static final String[] VICTORIA = {
        "¡%s CRUZA LA META EN PRIMER LUGAR! ¡Qué carrera hemos presenciado!",
        "¡VICTORIA DE %s! ¡Dominio total desde la pole!",
        "¡GANA %s! ¡El equipo %s puede celebrar!",
        "¡ESPECTACULAR TRIUNFO DE %s! ¡Qué piloto, señoras y señores!",
        "¡%s SE LLEVA LA VICTORIA EN %s! ¡Memorable!",
        "¡%s GANA LA CARRERA! ¡Actuación perfecta!",
        "¡CHECKERED FLAG PARA %s! ¡Victoria merecida!",
        "¡%s TRIUNFA! ¡Exhibición de principio a fin!",
        "¡GANA %s! ¡Su mejor carrera de la temporada!",
        "¡VICTORIA APLASTANTE DE %s! ¡Nadie pudo con él!"
    };

    private static final String[] PODIO = {
        "%s completa el podio en posición %d. ¡Excelente resultado!",
        "¡%s termina %d! Puntos importantes para el campeonato.",
        "Podio para %s terminando en posición %d. ¡Bien hecho!",
        "%s sube al podio en posición %d. ¡Gran carrera!",
        "¡%s en el podio! Termina %d. ¡Felicitaciones!"
    };

    // Nuevos comentarios para cambio de clima
    private static final String[] CAMBIO_CLIMA = {
        "¡ATENCIÓN! El clima está cambiando. Esto va a ser caótico.",
        "¡Las primeras gotas caen sobre la pista! Los equipos deben reaccionar rápido.",
        "¡El clima mejora! Algunos pilotos podrían arriesgar con slicks.",
        "¡Cambio de condiciones! Las estrategias se van por la ventana.",
        "¡El clima es impredecible hoy! Nadie sabe qué pasará.",
        "¡Lluvia en algunos sectores! La pista está dividida.",
        "¡Se aclara el cielo! Momento de decisiones críticas en los boxes."
    };

    /**
     * Genera comentario de inicio de carrera
     */
    public static String comentarInicio(Circuito circuito, Clima clima) {
        String intro = String.format(INICIO_CARRERA[random.nextInt(INICIO_CARRERA.length)],
                circuito.getNombre(), clima.toString().toLowerCase());
        String climaComentario = comentarClima(clima);
        return intro + "\n" + climaComentario;
    }

    /**
     * Genera comentario sobre el clima
     */
    public static String comentarClima(Clima clima) {
        switch (clima) {
            case SECO:
                return CLIMA_SECO[random.nextInt(CLIMA_SECO.length)];
            case LLUVIOSO:
                return CLIMA_LLUVIA[random.nextInt(CLIMA_LLUVIA.length)];
            case EXTREMO:
                return CLIMA_EXTREMO[random.nextInt(CLIMA_EXTREMO.length)];
            default:
                return "";
        }
    }

    /**
     * Genera comentario de accidente
     */
    public static String comentarAccidente(String piloto, String equipo, boolean grave) {
        if (grave) {
            return String.format(ACCIDENTE_GRAVE[random.nextInt(ACCIDENTE_GRAVE.length)], piloto, equipo);
        } else {
            return String.format(ACCIDENTE_LEVE[random.nextInt(ACCIDENTE_LEVE.length)], piloto);
        }
    }

    /**
     * Genera comentario de fallo mecánico
     */
    public static String comentarFalloMecanico(String piloto, String equipo) {
        return String.format(FALLO_MECANICO[random.nextInt(FALLO_MECANICO.length)], piloto, equipo);
    }

    /**
     * Genera comentario de adelantamiento
     */
    public static String comentarAdelantamiento(String adelanta, String adelantado, int posicion) {
        return String.format(ADELANTAMIENTO[random.nextInt(ADELANTAMIENTO.length)],
                adelanta, adelantado, posicion);
    }

    /**
     * Genera comentario del líder
     */
    public static String comentarLider(String piloto) {
        return String.format(LIDER[random.nextInt(LIDER.length)], piloto);
    }

    /**
     * Genera comentario sobre neumáticos
     */
    public static String comentarNeumaticos(TipoNeumatico tipo) {
        switch (tipo) {
            case BLANDO:
                return NEUMATICOS_BLANDOS[random.nextInt(NEUMATICOS_BLANDOS.length)];
            case DURO:
                return NEUMATICOS_DUROS[random.nextInt(NEUMATICOS_DUROS.length)];
            default:
                return "";
        }
    }

    /**
     * Genera comentario de victoria
     */
    public static String comentarVictoria(String piloto, String equipo, String circuito) {
        return String.format(VICTORIA[random.nextInt(VICTORIA.length)], piloto, equipo, circuito);
    }

    /**
     * Genera comentario de podio
     */
    public static String comentarPodio(String piloto, int posicion) {
        return String.format(PODIO[random.nextInt(PODIO.length)], piloto, posicion);
    }

    /**
     * Genera comentario de cambio de clima
     */
    public static String comentarCambioClima(Clima climaAnterior, Clima climaNuevo) {
        String comentario = CAMBIO_CLIMA[random.nextInt(CAMBIO_CLIMA.length)];
        String detalle = "";

        if (climaAnterior == Clima.SECO && climaNuevo == Clima.LLUVIOSO) {
            detalle = " ¡La lluvia comienza a caer! Los equipos preparan los neumáticos de lluvia.";
        } else if (climaAnterior == Clima.LLUVIOSO && climaNuevo == Clima.EXTREMO) {
            detalle = " ¡Empeora drásticamente! Esto se pone peligroso.";
        } else if (climaAnterior == Clima.LLUVIOSO && climaNuevo == Clima.SECO) {
            detalle = " ¡La pista se seca! Momento crucial para cambiar a slicks.";
        } else if (climaAnterior == Clima.EXTREMO && climaNuevo == Clima.LLUVIOSO) {
            detalle = " ¡Mejora la visibilidad! Los pilotos pueden atacar de nuevo.";
        }

        return comentario + detalle;
    }

    /**
     * Genera resumen estadístico de la carrera
     */
    public static String generarResumenCarrera(GestorTemporada.ResultadoCarrera resultado) {
        StringBuilder resumen = new StringBuilder();
        resumen.append("\n═══════════════════════════════════════════════════════════\n");
        resumen.append("              RESUMEN DE CARRERA - ").append(resultado.getCircuito().getNombre()).append("\n");
        resumen.append("═══════════════════════════════════════════════════════════\n\n");

        resumen.append("🏆 GANADOR: ").append(resultado.getGanador()).append("\n");
        resumen.append("🌤️  CLIMA: ").append(resultado.getClima()).append("\n");
        resumen.append("✅ FINALIZADOS: ").append(resultado.getTotalFinalizados()).append(" pilotos\n");
        resumen.append("❌ DNF: ").append(resultado.getTotalDNF()).append(" pilotos\n");
        resumen.append("💥 ACCIDENTES: ").append(resultado.getTotalAccidentes()).append("\n");
        resumen.append("🔧 FALLOS MECÁNICOS: ").append(resultado.getTotalFallosMecanicos()).append("\n");
        resumen.append("⚠️  PENALIZACIONES: ").append(resultado.getTotalPenalizaciones()).append("\n");

        resumen.append("\n═══════════════════════════════════════════════════════════\n");

        return resumen.toString();
    }
}
