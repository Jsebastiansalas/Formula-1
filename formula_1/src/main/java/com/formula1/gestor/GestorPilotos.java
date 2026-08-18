package com.formula1.gestor;

import com.formula1.modelo.Piloto;
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

public class GestorPilotos {
    private Map<Integer, Piloto> pilotos;

    public GestorPilotos() {
        this.pilotos = new HashMap<>();
        cargarDesdeJSON("data/pilotos.json");
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
            Type tipoLista = new TypeToken<List<Piloto>>() {}.getType();
            List<Piloto> lista = gson.fromJson(reader, tipoLista);

            if (lista != null) {
                for (Piloto p : lista) {
                    pilotos.put(p.getId(), p);
                }
                System.out.println(lista.size() + " pilotos cargados correctamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar pilotos: " + e.getMessage());
        }
    }

    public boolean agregarPiloto(Piloto piloto) {
        if (pilotos.containsKey(piloto.getId())) {
            return false;
        }
        pilotos.put(piloto.getId(), piloto);
        return true;
    }

    public Piloto buscarPiloto(int id) {
        return pilotos.get(id);
    }

    public List<Piloto> listarPilotos() {
        return new ArrayList<>(pilotos.values());
    }

    public boolean actualizarPiloto(Piloto piloto) {
        if (!pilotos.containsKey(piloto.getId())) {
            return false;
        }
        pilotos.put(piloto.getId(), piloto);
        return true;
    }

    public boolean eliminarPiloto(int id) {
        return pilotos.remove(id) != null;
    }
}
