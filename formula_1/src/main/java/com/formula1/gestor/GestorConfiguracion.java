package com.formula1.gestor;

import com.formula1.modelo.ConfiguracionVehiculo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GestorConfiguracion {
    private Map<String, ConfiguracionVehiculo> configuraciones;
    private final String RUTA_ARCHIVO = "src/main/resources/data/configuracion.json";
    private Gson gson;

    public GestorConfiguracion() {
        this.configuraciones = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type mapType = new TypeToken<HashMap<String, ConfiguracionVehiculo>>(){}.getType();
            Map<String, ConfiguracionVehiculo> leido = gson.fromJson(reader, mapType);
            if (leido != null) this.configuraciones = leido;
        } catch (IOException e) {
            this.configuraciones = new HashMap<>();
        }
    }

    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(configuraciones, writer);
        } catch (IOException e) { System.err.println("Error guardando configuraciones: " + e.getMessage()); }
    }

    public void configurarVehiculo(ConfiguracionVehiculo config) {
        configuraciones.put(config.getModeloVehiculo(), config);
        guardarDatos(); 
    }

    public ConfiguracionVehiculo obtenerConfiguracion(String modeloVehiculo) {
        return configuraciones.get(modeloVehiculo);
    }
}