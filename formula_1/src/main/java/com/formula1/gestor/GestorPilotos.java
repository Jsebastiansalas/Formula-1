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

/**
 * Gestor CRUD para pilotos de Formula 1.
 * Carga automaticamente los pilotos desde el archivo JSON del classpath
 * (data/pilotos.json) al instanciarse. Almacena en HashMap indexado por ID.
 */
public class GestorPilotos {

    /** Almacen en memoria de pilotos, indexado por su ID unico */
    private Map<Integer, Piloto> pilotos;

    /**
     * Constructor que inicializa el mapa y carga los pilotos desde JSON.
     */
    public GestorPilotos() {
        this.pilotos = new HashMap<>();
        cargarDesdeJSON("data/pilotos.json");
    }

    /**
     * Carga pilotos desde un archivo JSON en el classpath usando Gson.
     * @param rutaResource ruta relativa al classpath (ej: "data/pilotos.json")
     */
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

    /**
     * Agrega un piloto si no existe otro con el mismo ID.
     * @param piloto piloto a registrar
     * @return true si se agrego, false si el ID ya existia
     */
    public boolean agregarPiloto(Piloto piloto) {
        if (pilotos.containsKey(piloto.getId())) {
            return false;
        }
        pilotos.put(piloto.getId(), piloto);
        return true;
    }

    /**
     * Busca un piloto por su ID unico.
     * @param id identificador del piloto
     * @return el piloto encontrado o null
     */
    public Piloto buscarPiloto(int id) {
        return pilotos.get(id);
    }

    /**
     * Retorna todos los pilotos registrados como lista.
     * @return lista con todos los pilotos
     */
    public List<Piloto> listarPilotos() {
        return new ArrayList<>(pilotos.values());
    }

    /**
     * Actualiza los datos de un piloto existente.
     * @param piloto piloto con datos actualizados (debe mantener el mismo ID)
     * @return true si se actualizo, false si el ID no existia
     */
    public boolean actualizarPiloto(Piloto piloto) {
        if (!pilotos.containsKey(piloto.getId())) {
            return false;
        }
        pilotos.put(piloto.getId(), piloto);
        return true;
    }

    /**
     * Elimina un piloto del sistema por su ID.
     * @param id identificador del piloto a eliminar
     * @return true si existia y fue eliminado
     */
    public boolean eliminarPiloto(int id) {
        return pilotos.remove(id) != null;
    }
}
