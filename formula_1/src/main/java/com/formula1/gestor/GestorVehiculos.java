package com.formula1.gestor;

import com.formula1.modelo.Vehiculo;
import com.formula1.modelo.Piloto;
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

public class GestorVehiculos {
    private Map<String, Vehiculo> vehiculos;
    private final String RUTA_ARCHIVO = "src/main/resources/data/vehiculos.json";
    private Gson gson;

    public GestorVehiculos() {
        this.vehiculos = new HashMap<>();
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type listType = new TypeToken<ArrayList<Vehiculo>>(){}.getType();
            List<Vehiculo> lista = gson.fromJson(reader, listType);
            if (lista != null) {
                for (Vehiculo v : lista) vehiculos.put(v.getModelo(), v);
            }
        } catch (IOException e) { System.err.println("Error leyendo vehiculos: " + e.getMessage()); }
    }

    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(new ArrayList<>(vehiculos.values()), writer);
        } catch (IOException e) { System.err.println("Error guardando vehiculos: " + e.getMessage()); }
    }

    public boolean agregarVehiculo(Vehiculo v) {
        if (vehiculos.containsKey(v.getModelo())) return false;
        vehiculos.put(v.getModelo(), v);
        guardarDatos();
        return true;
    }

    public Vehiculo buscarVehiculo(String modelo) { return vehiculos.get(modelo); }
    public List<Vehiculo> listarVehiculos() { return new ArrayList<>(vehiculos.values()); }

    public boolean actualizarVehiculo(Vehiculo v) {
        if (!vehiculos.containsKey(v.getModelo())) return false;
        vehiculos.put(v.getModelo(), v);
        guardarDatos();
        return true;
    }

    public boolean asignarPilotoAVehiculo(String modeloVehiculo, Piloto piloto, Equipo equipo) {
        Vehiculo v = buscarVehiculo(modeloVehiculo);
        if (v != null && piloto.getEquipo().equals(equipo.getNombre())) {
            if (!v.getPilotos().contains(piloto.getId())) {
                v.getPilotos().add(piloto.getId());
                guardarDatos();
                return true;
            }
        }
        return false;
    }

    public List<Vehiculo> compararVehiculos(List<String> modelosAComparar) {
        List<Vehiculo> comparacion = new ArrayList<>();
        for (String modelo : modelosAComparar) {
            Vehiculo v = buscarVehiculo(modelo);
            if (v != null) comparacion.add(v);
        }
        return comparacion;
    }
}