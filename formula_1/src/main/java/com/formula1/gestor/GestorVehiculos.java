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

/**
 * Gestor CRUD para vehiculos (monoplazas) de Formula 1.
 * Carga automaticamente desde data/vehiculos.json al instanciarse.
 * Almacena en HashMap indexado por nombre de modelo.
 * Ademas permite asignar pilotos a vehiculos y comparar especificaciones.
 */
public class GestorVehiculos {

    /** Almacen en memoria de vehiculos, indexado por nombre de modelo */
    private Map<String, Vehiculo> vehiculos;

    /**
     * Constructor que inicializa el mapa y carga vehiculos desde JSON.
     */
    public GestorVehiculos() {
        this.vehiculos = new HashMap<>();
        cargarDesdeJSON("data/vehiculos.json");
    }

    /**
     * Carga vehiculos desde un archivo JSON en el classpath usando Gson.
     * @param rutaResource ruta relativa al classpath (ej: "data/vehiculos.json")
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

    /**
     * Agrega un vehiculo si no existe otro con el mismo modelo.
     * @param vehiculo vehiculo a registrar
     * @return true si se agrego, false si el modelo ya existia
     */
    public boolean agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculos.containsKey(vehiculo.getModelo())) {
            return false;
        }
        vehiculos.put(vehiculo.getModelo(), vehiculo);
        return true;
    }

    /**
     * Busca un vehiculo por nombre de modelo.
     * @param modelo nombre del modelo
     * @return el vehiculo encontrado o null
     */
    public Vehiculo buscarVehiculo(String modelo) {
        return vehiculos.get(modelo);
    }

    /** @return lista con todos los vehiculos registrados */
    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(vehiculos.values());
    }

    /**
     * Actualiza un vehiculo existente por su modelo.
     * @param vehiculo vehiculo con datos actualizados
     * @return true si se actualizo, false si el modelo no existia
     */
    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        if (!vehiculos.containsKey(vehiculo.getModelo())) {
            return false;
        }
        vehiculos.put(vehiculo.getModelo(), vehiculo);
        return true;
    }

    /**
     * Elimina un vehiculo por nombre de modelo.
     * @param modelo nombre del modelo a eliminar
     * @return true si existia y fue eliminado
     */
    public boolean eliminarVehiculo(String modelo) {
        return vehiculos.remove(modelo) != null;
    }

    /**
     * Asigna un piloto a un vehiculo, validando que pertenezcan al mismo equipo.
     * Evita duplicados (un piloto no puede estar asignado dos veces).
     * @param modeloVehiculo nombre del modelo del vehiculo
     * @param piloto piloto a asignar
     * @param equipo equipo al que debe pertenecer el piloto
     * @return true si se asigno correctamente, false si no cumple validaciones
     */
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

    /**
     * Obtiene una lista de vehiculos para comparacion lado a lado.
     * @param modelosAComparar lista de nombres de modelo a comparar
     * @return lista de vehiculos encontrados (ignora modelos inexistentes)
     */
    public List<Vehiculo> compararVehiculos(List<String> modelosAComparar) {
        List<Vehiculo> comparacion = new ArrayList<>();
        for (String modelo : modelosAComparar) {
            Vehiculo v = buscarVehiculo(modelo);
            if (v != null) comparacion.add(v);
        }
        return comparacion;
    }
}
