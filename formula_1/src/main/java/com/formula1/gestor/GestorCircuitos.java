package com.formula1.gestor;

import com.formula1.modelo.Circuito;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorCircuitos {
    private Map<String, Circuito> circuitos;

    public GestorCircuitos() {
        this.circuitos = new HashMap<>();
    }

    // Carga los circuitos desde un JSON en resources/ (ej: "data/circuitos.json")
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
            Type tipoLista = new TypeToken<List<Circuito>>() {}.getType();
            List<Circuito> lista = gson.fromJson(reader, tipoLista);

            for (Circuito c : lista) {
                agregarCircuito(c);
            }
            System.out.println(lista.size() + " circuitos cargados correctamente.");

        } catch (Exception e) {
            System.out.println("Error al cargar circuitos: " + e.getMessage());
        }
    }

    public boolean agregarCircuito(Circuito circuito) {
        if (circuitos.containsKey(circuito.getNombre())) {
            return false;
        }
        circuitos.put(circuito.getNombre(), circuito);
        return true;
    }

    public Circuito buscarPorNombre(String nombre) {
        return circuitos.get(nombre);
    }

    public List<Circuito> buscarPorPais(String pais) {
        List<Circuito> resultado = new java.util.ArrayList<>();
        for (Circuito c : circuitos.values()) {
            if (c.getPais().equalsIgnoreCase(pais)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public boolean editarCircuito(String nombre, Circuito nuevosDatos) {
        if (!circuitos.containsKey(nombre)) {
            return false;
        }
        circuitos.put(nombre, nuevosDatos);
        return true;
    }

    public boolean eliminarCircuito(String nombre) {
        return circuitos.remove(nombre) != null;
    }

    public void listarCircuitos() {
        if (circuitos.isEmpty()) {
            System.out.println("No hay circuitos registrados.");
            return;
        }
        for (Circuito c : circuitos.values()) {
            System.out.println(c);
        }
    }
}
