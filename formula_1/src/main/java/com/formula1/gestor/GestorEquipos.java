package com.formula1.gestor;

import com.formula1.modelo.Equipo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestor CRUD para escuderias (equipos) de Formula 1.
 * A diferencia de otros gestores, este persiste datos a disco (data/equipos.json)
 * usando FileReader/FileWriter, permitiendo guardar cambios entre ejecuciones.
 */
public class GestorEquipos {

    /** Almacen en memoria de equipos, indexado por nombre */
    private Map<String, Equipo> equipos;
    /** Ruta al archivo JSON de persistencia en disco */
    private final String RUTA_ARCHIVO = "data/equipos.json";
    /** Instancia de Gson configurada con pretty printing y naming policy */
    private Gson gson;

    /**
     * Constructor que inicializa Gson y carga los equipos desde el archivo en disco.
     */
    public GestorEquipos() {
        this.equipos = new HashMap<>();
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        cargarDatos();
    }

    /**
     * Carga los equipos desde el archivo JSON en disco.
     * Si el archivo no existe o falla, el mapa queda vacio.
     */
    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type listType = new TypeToken<ArrayList<Equipo>>(){}.getType();
            List<Equipo> lista = gson.fromJson(reader, listType);
            if (lista != null) {
                for (Equipo e : lista) equipos.put(e.getNombre(), e);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo equipos: " + e.getMessage());
        }
    }

    /** Persiste la lista actual de equipos al archivo JSON en disco. */
    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(new ArrayList<>(equipos.values()), writer);
        } catch (IOException e) {
            System.err.println("Error guardando equipos: " + e.getMessage());
        }
    }

    /**
     * Agrega un equipo si no existe otro con el mismo nombre. Guarda automaticamente.
     * @param e equipo a agregar
     * @return true si se agrego, false si el nombre ya existia
     */
    public boolean agregarEquipo(Equipo e) {
        if (equipos.containsKey(e.getNombre())) return false;
        equipos.put(e.getNombre(), e);
        guardarDatos();
        return true;
    }

    /**
     * Busca un equipo por nombre.
     * @param nombre nombre del equipo
     * @return el equipo o null si no existe
     */
    public Equipo buscarEquipo(String nombre) {
        return equipos.get(nombre);
    }

    /** @return lista con todos los equipos registrados */
    public List<Equipo> listarEquipos() {
        return new ArrayList<>(equipos.values());
    }

    /**
     * Actualiza un equipo existente. Guarda automaticamente.
     * @param e equipo con datos actualizados
     * @return true si se actualizo, false si no existia
     */
    public boolean actualizarEquipo(Equipo e) {
        if (!equipos.containsKey(e.getNombre())) return false;
        equipos.put(e.getNombre(), e);
        guardarDatos();
        return true;
    }

    /**
     * Elimina un equipo por nombre. Guarda automaticamente.
     * @param nombre nombre del equipo a eliminar
     * @return true si existia y fue eliminado
     */
    public boolean eliminarEquipo(String nombre) {
        if (equipos.remove(nombre) != null) {
            guardarDatos();
            return true;
        }
        return false;
    }
}
