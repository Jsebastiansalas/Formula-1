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

/**
 * Gestor de configuraciones tecnicas de vehiculos.
 * Persiste las configuraciones en disco (data/configuracion.json)
 * permitiendo guardar ajustes de aerodinamica, neumaticos y estrategia
 * entre ejecuciones de la aplicacion.
 */
public class GestorConfiguracion {

    /** Mapa de configuraciones indexado por nombre de modelo de vehiculo */
    private Map<String, ConfiguracionVehiculo> configuraciones;
    /** Ruta al archivo JSON de persistencia */
    private final String RUTA_ARCHIVO = "data/configuracion.json";
    /** Instancia de Gson con pretty printing para legibilidad del JSON */
    private Gson gson;

    /**
     * Constructor que inicializa Gson y carga configuraciones existentes desde disco.
     */
    public GestorConfiguracion() {
        this.configuraciones = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        cargarDatos();
    }

    /**
     * Carga configuraciones desde el archivo JSON.
     * Si el archivo no existe, el mapa permanece vacio (primera ejecucion).
     */
    private void cargarDatos() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type mapType = new TypeToken<HashMap<String, ConfiguracionVehiculo>>(){}.getType();
            Map<String, ConfiguracionVehiculo> leido = gson.fromJson(reader, mapType);
            if (leido != null) this.configuraciones = leido;
        } catch (IOException e) {
            this.configuraciones = new HashMap<>();
        }
    }

    /** Persiste todas las configuraciones al archivo JSON en disco. */
    public void guardarDatos() {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(configuraciones, writer);
        } catch (IOException e) {
            System.err.println("Error guardando configuraciones: " + e.getMessage());
        }
    }

    /**
     * Guarda o actualiza la configuracion de un vehiculo. Persiste automaticamente.
     * @param config configuracion a guardar (se indexa por modeloVehiculo)
     */
    public void configurarVehiculo(ConfiguracionVehiculo config) {
        configuraciones.put(config.getModeloVehiculo(), config);
        guardarDatos();
    }

    /**
     * Obtiene la configuracion guardada para un vehiculo especifico.
     * @param modeloVehiculo nombre del modelo
     * @return la configuracion o null si no tiene una guardada
     */
    public ConfiguracionVehiculo obtenerConfiguracion(String modeloVehiculo) {
        return configuraciones.get(modeloVehiculo);
    }
}
