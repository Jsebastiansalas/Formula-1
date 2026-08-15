package com.formula1;

import java.util.List;

import com.formula1.gestor.GestorCircuitos;
import com.formula1.modelo.Circuito;
import com.formula1.simulacion.Clima;
import com.formula1.simulacion.SimuladorClasificacion;

public class Main {
    public static void main(String[] args) {
    GestorCircuitos gestorCircuitos = new GestorCircuitos();
gestorCircuitos.cargarDesdeJSON("data/circuitos.json");

Circuito monza = gestorCircuitos.buscarPorNombre("Circuito de Monza");
Clima clima = Clima.generarAleatorio();
System.out.println("Clima de la sesión: " + clima);

SimuladorClasificacion simulador = new SimuladorClasificacion();
List<SimuladorClasificacion.ResultadoVuelta> clasificacion = simulador.simularConDatosPrueba(monza, clima);

System.out.println("--- Clasificación ---");
for (int i = 0; i < clasificacion.size(); i++) {
    System.out.println((i + 1) + ". " + clasificacion.get(i));
}
    }
    
}
