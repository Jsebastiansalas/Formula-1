package com.formula1.gestor;

import com.formula1.modelo.Piloto;
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

public class GestorPilotos {
    private Map<Integer, Piloto> pilotos;
    private final String RUTA_ARCHIVO = "src/main/resources/data/pilotos.json";
    private Gson gson;

    public GestorPilotos() {
        this.pilotos = new HashMap<>();
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type listType = new TypeToken<ArrayList<Piloto>>(){}.getType();
            List<Piloto> lista = gson.fromJson(reader, listType);
            if (lista != null) {
                for (Piloto p : lista) pilotos.put(p.getId(), p);
            }
        } catch (IOException e) { System.err.println("Error leyendo pilotos: " + e.getMessage()); }
    }

    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(new ArrayList<>(pilotos.values()), writer);
        } catch (IOException e) { System.err.println("Error guardando pilotos: " + e.getMessage()); }
    }

    public boolean agregarPiloto(Piloto p) {
        if (pilotos.containsKey(p.getId())) return false;
        pilotos.put(p.getId(), p);
        guardarDatos();
        return true;
    }

    public Piloto buscarPiloto(int id) { return pilotos.get(id); }
    public List<Piloto> listarPilotos() { return new ArrayList<>(pilotos.values()); }

    public boolean actualizarPiloto(Piloto p) {
        if (!pilotos.containsKey(p.getId())) return false;
        pilotos.put(p.getId(), p);
        guardarDatos();
        return true;
    }

    public boolean eliminarPiloto(int id) {
        if (pilotos.remove(id) != null) {
            guardarDatos();
            return true;
        }
        return false;
    }
}