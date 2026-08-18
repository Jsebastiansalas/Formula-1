package com.formula1.gestor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.formula1.modelo.Circuito;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Gestor CRUD para circuitos de Formula 1.
 * Carga los circuitos desde un archivo JSON en el classpath (resources/data/circuitos.json)
 * y los almacena en un HashMap indexado por nombre del circuito.
 * Proporciona operaciones de busqueda, edicion, eliminacion y listado.
 */
public class GestorCircuitos {

    /** Almacen en memoria de circuitos, indexado por nombre */
    private Map<String, Circuito> circuitos;

    /** Constructor que inicializa el mapa vacio. Se debe llamar cargarDesdeJSON() aparte. */
    public GestorCircuitos() {
        this.circuitos = new HashMap<>();
    }

    /**
     * Carga circuitos desde un archivo JSON ubicado en el classpath.
     * Usa Gson con politica LOWER_CASE_WITH_UNDERSCORES para mapear campos.
     * @param rutaResource ruta relativa al classpath (ej: "data/circuitos.json")
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

    /**
     * Agrega un circuito al sistema si no existe uno con el mismo nombre.
     * @param circuito circuito a agregar
     * @return true si se agrego exitosamente, false si ya existia
     */
    public boolean agregarCircuito(Circuito circuito) {
        if (circuitos.containsKey(circuito.getNombre())) {
            return false;
        }
        circuitos.put(circuito.getNombre(), circuito);
        return true;
    }

    /**
     * Busca un circuito por su nombre exacto.
     * @param nombre nombre del circuito
     * @return el circuito encontrado o null si no existe
     */
    public Circuito buscarPorNombre(String nombre) {
        return circuitos.get(nombre);
    }

    /**
     * Busca todos los circuitos ubicados en un pais (busqueda case-insensitive).
     * @param pais nombre del pais a buscar
     * @return lista de circuitos en ese pais (puede estar vacia)
     */
    public List<Circuito> buscarPorPais(String pais) {
        List<Circuito> resultado = new ArrayList<>();
        for (Circuito c : circuitos.values()) {
            if (c.getPais().equalsIgnoreCase(pais)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    /**
     * Obtiene todos los circuitos registrados como lista.
     * Se usa para poblar las tablas en la interfaz grafica.
     * @return copia de la coleccion de circuitos
     */
    public List<Circuito> obtenerTodosCircuitos() {
        return new ArrayList<>(circuitos.values());
    }

    /**
     * Reemplaza un circuito existente por nuevos datos.
     * @param nombre nombre del circuito a reemplazar
     * @param nuevosDatos nuevo objeto Circuito con los datos actualizados
     * @return true si se actualizo, false si no existia
     */
    public boolean editarCircuito(String nombre, Circuito nuevosDatos) {
        if (!circuitos.containsKey(nombre)) {
            return false;
        }
        circuitos.put(nombre, nuevosDatos);
        return true;
    }

    /**
     * Actualiza un circuito que fue modificado in-place via setters.
     * Reindexa el HashMap por si el nombre (clave) cambio.
     * @param circuito circuito modificado a reindexar
     * @return siempre true
     */
    public boolean actualizarCircuito(Circuito circuito) {
        circuitos.values().removeIf(c -> c == circuito);
        circuitos.put(circuito.getNombre(), circuito);
        return true;
    }

    /**
     * Elimina un circuito del sistema por su nombre.
     * @param nombre nombre del circuito a eliminar
     * @return true si existia y fue eliminado, false si no existia
     */
    public boolean eliminarCircuito(String nombre) {
        return circuitos.remove(nombre) != null;
    }

    /** Imprime todos los circuitos en consola (uso para depuracion). */
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