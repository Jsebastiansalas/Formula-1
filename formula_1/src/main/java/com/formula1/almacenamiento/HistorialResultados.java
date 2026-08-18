package com.formula1.almacenamiento;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.formula1.modelo.Circuito;
import com.formula1.simulacion.Clima;
import com.formula1.simulacion.SimuladorClasificacion.ResultadoVuelta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Gestiona la persistencia del historial de resultados de carreras.
 * Guarda y carga sesiones desde un archivo JSON (historial/resultados.json).
 * Al instanciarse, carga automaticamente el historial de ejecuciones anteriores.
 * Permite guardar nuevas sesiones, listar el historial y comparar tiempos de pole.
 */
public class HistorialResultados {
    /** Ruta al archivo de persistencia del historial */
    private static final String RUTA_ARCHIVO = "historial/resultados.json";
    /** Lista de todas las sesiones guardadas */
    private List<SesionResultado> sesiones;

    public HistorialResultados() {
        this.sesiones = new ArrayList<>();
        cargarDesdeArchivo(); // recupera sesiones de ejecuciones anteriores al arrancar
    }

    /**
     * Guarda una nueva sesion de carrera y persiste todo el historial a disco.
     * @param circuito circuito donde se corrio
     * @param clima condiciones climaticas
     * @param clasificacion resultados de la carrera
     */
    public void guardarSesion(Circuito circuito, Clima clima, List<ResultadoVuelta> clasificacion) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SesionResultado sesion = new SesionResultado(circuito.getNombre(), clima.toString(), fecha, clasificacion);
        sesiones.add(sesion);
        guardarEnArchivo();
    }

    private void guardarEnArchivo() {
        File carpeta = new File("historial");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        try (Writer writer = new FileWriter(RUTA_ARCHIVO)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(sesiones, writer);
        } catch (IOException e) {
            System.out.println("Error al guardar historial: " + e.getMessage());
        }
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return; // primera vez que corre el programa, no hay nada que cargar
        }
        try (Reader reader = new FileReader(archivo)) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<SesionResultado>>() {}.getType();
            List<SesionResultado> cargadas = gson.fromJson(reader, tipoLista);
            if (cargadas != null) {
                sesiones = cargadas;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar historial: " + e.getMessage());
        }
    }

    /** Lista todo el historial de sesiones en consola */
    public void listarHistorial() {
        if (sesiones.isEmpty()) {
            System.out.println("No hay sesiones guardadas todavía.");
            return;
        }
        for (SesionResultado s : sesiones) {
            System.out.println(s);
        }
    }

    /** Compara los mejores tiempos (pole) de todas las sesiones guardadas */
    public void compararMejoresTiempos() {
        if (sesiones.isEmpty()) {
            System.out.println("No hay sesiones para comparar.");
            return;
        }
        for (SesionResultado s : sesiones) {
            if (!s.getClasificacion().isEmpty()) {
                ResultadoVuelta pole = s.getClasificacion().get(0);
                System.out.printf("%s (%s): pole %s%n", s.getCircuito(), s.getFecha(), pole);
            }
        }
    }

    public List<SesionResultado> getSesiones() { return sesiones; }
}
