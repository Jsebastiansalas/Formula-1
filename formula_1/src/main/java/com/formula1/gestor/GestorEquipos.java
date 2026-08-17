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

public class GestorEquipos {
    private Map<String, Equipo> equipos;
    private final String RUTA_ARCHIVO = "src/main/resources/data/equipos.json";
    private Gson gson;

    public GestorEquipos() {
        this.equipos = new HashMap<>();
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type listType = new TypeToken<ArrayList<Equipo>>(){}.getType();
            List<Equipo> lista = gson.fromJson(reader, listType);
            if (lista != null) {
                for (Equipo e : lista) equipos.put(e.getNombre(), e);
            }
        } catch (IOException e) { System.err.println("Error leyendo equipos: " + e.getMessage()); }
    }

    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(new ArrayList<>(equipos.values()), writer);
        } catch (IOException e) { System.err.println("Error guardando equipos: " + e.getMessage()); }
    }

    public boolean agregarEquipo(Equipo e) {
        if (equipos.containsKey(e.getNombre())) return false;
        equipos.put(e.getNombre(), e);
        guardarDatos();
        return true;
    }

    public Equipo buscarEquipo(String nombre) { return equipos.get(nombre); }
    public List<Equipo> listarEquipos() { return new ArrayList<>(equipos.values()); }

    public boolean actualizarEquipo(Equipo e) {
        if (!equipos.containsKey(e.getNombre())) return false;
        equipos.put(e.getNombre(), e);
        guardarDatos();
        return true;
    }

    public boolean eliminarEquipo(String nombre) {
        if (equipos.remove(nombre) != null) {
            guardarDatos();
            return true;
        }
        return false;
    }
}