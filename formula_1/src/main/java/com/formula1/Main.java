package com.formula1;

import java.util.List;

import com.formula1.almacenamiento.HistorialResultados;
import com.formula1.gestor.GestorCircuitos;
import com.formula1.modelo.Circuito;
import com.formula1.simulacion.Clima;
import com.formula1.simulacion.SimuladorClasificacion;
import com.formula1.simulacion.SistemaPuntos;
import com.formula1.simulacion.TablaCampeonato;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║      SIMULADOR DE FÓRMULA 1 - TEMPORADA 2026                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        // Cargar circuitos
        GestorCircuitos gestorCircuitos = new GestorCircuitos();
        gestorCircuitos.cargarDesdeJSON("data/circuitos.json");

        // Mostrar sistema de puntos
        SistemaPuntos.mostrarTablaPuntos();
        System.out.println();

        // Seleccionar un circuito de prueba
        Circuito monza = gestorCircuitos.buscarPorNombre("Circuito de Monza");
        if (monza == null) {
            System.out.println("Error: No se encontró el circuito de Monza");
            return;
        }

        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              SIMULACIÓN: " + monza.getNombre());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Generar clima aleatorio
        Clima clima = Clima.generarAleatorio();
        System.out.println("Clima de la sesión: " + clima);
        System.out.println();

        // Simular carrera
        SimuladorClasificacion simulador = new SimuladorClasificacion();
        List<SimuladorClasificacion.ResultadoVuelta> clasificacion = simulador.simularConDatosPrueba(monza, clima);

        // Mostrar resultados completos
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    RESULTADOS DE LA CARRERA                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        for (SimuladorClasificacion.ResultadoVuelta resultado : clasificacion) {
            System.out.println(resultado);
        }

        // Guardar en historial
        HistorialResultados historial = new HistorialResultados();
        historial.guardarSesion(monza, clima, clasificacion);

        // Crear tabla de campeonato y registrar carrera
        TablaCampeonato campeonato = new TablaCampeonato();
        campeonato.registrarCarrera(clasificacion);
        campeonato.mostrarClasificacion();

        // Mostrar campeón actual
        TablaCampeonato.PilotoCampeonato campeon = campeonato.obtenerCampeon();
        if (campeon != null) {
            System.out.println("🏆 Líder del campeonato: " + campeon.getNombre() +
                    " (" + campeon.getPuntosTotal() + " puntos)");
        }

        System.out.println("\n" + "═".repeat(65));
        System.out.println("Simulación completada. Datos guardados en historial/resultados.json");
    }
}
