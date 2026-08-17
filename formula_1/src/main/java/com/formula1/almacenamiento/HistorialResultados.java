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

public class HistorialResultados {
    private static final String RUTA_ARCHIVO = "historial/resultados.json";
    private List<SesionResultado> sesiones;

    public HistorialResultados() {
        this.sesiones = new ArrayList<>();
        cargarDesdeArchivo(); // recupera sesiones de ejecuciones anteriores al arrancar
    }

    // Guarda una nueva sesión y persiste todo el historial a disco (RF24)
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

    // RF25: ver historial completo de sesiones/configuraciones previas
    public void listarHistorial() {
        if (sesiones.isEmpty()) {
            System.out.println("No hay sesiones guardadas todavía.");
            return;
        }
        for (SesionResultado s : sesiones) {
            System.out.println(s);
        }
    }

    // RF26: comparar tiempos entre sesiones -> mejor tiempo de pole por cada una
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
