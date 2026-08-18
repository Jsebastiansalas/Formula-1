package com.formula1.gestor;

import com.formula1.modelo.Vehiculo;
import com.formula1.modelo.Piloto;
import com.formula1.modelo.Equipo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorVehiculos {
    private Map<String, Vehiculo> vehiculos;

    public GestorVehiculos() {
        this.vehiculos = new HashMap<>();
        cargarDesdeJSON("data/vehiculos.json");
    }

    public void cargarDesdeJSON(String rutaResource) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(rutaResource)) {
            if (is == null) {
                System.out.println("No se encontró el archivo: " + rutaResource);
                return;
            }
            Reader reader = new InputStreamReader(is);
            Type tipoLista = new TypeToken<List<Vehiculo>>() {}.getType();
            List<Vehiculo> lista = gson.fromJson(reader, tipoLista);

            if (lista != null) {
                for (Vehiculo v : lista) {
                    vehiculos.put(v.getModelo(), v);
                }
                System.out.println(lista.size() + " vehículos cargados correctamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar vehículos: " + e.getMessage());
        }
    }

    public boolean agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculos.containsKey(vehiculo.getModelo())) {
            return false;
        }
        vehiculos.put(vehiculo.getModelo(), vehiculo);
        return true;
    }

    public Vehiculo buscarVehiculo(String modelo) {
        return vehiculos.get(modelo);
    }

    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(vehiculos.values());
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        if (!vehiculos.containsKey(vehiculo.getModelo())) {
            return false;
        }
        vehiculos.put(vehiculo.getModelo(), vehiculo);
        return true;
    }

    public boolean eliminarVehiculo(String modelo) {
        return vehiculos.remove(modelo) != null;
    }

    public boolean asignarPilotoAVehiculo(String modeloVehiculo, Piloto piloto, Equipo equipo) {
        Vehiculo v = buscarVehiculo(modeloVehiculo);
        if (v != null && piloto.getEquipo().equals(equipo.getNombre())) {
            if (!v.getPilotos().contains(piloto.getId())) {
                v.getPilotos().add(piloto.getId());
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
