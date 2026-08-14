package com.formula1.gestor;

import com.formula1.modelo.Circuito;
import java.util.HashMap;
import java.util.Map;

public class GestorCircuitos {
    private Map<String, Circuito> circuitos;

    public GestorCircuitos() {
        this.circuitos = new HashMap<>();
    }

    // Agregar
    public boolean agregarCircuito(Circuito circuito) {
        if (circuitos.containsKey(circuito.getNombre())) {
            return false; // ya existe, no lo sobreescribe
        }
        circuitos.put(circuito.getNombre(), circuito);
        return true;
    }

    // Buscar por nombre exacto (clave)
    public Circuito buscarPorNombre(String nombre) {
        return circuitos.get(nombre);
    }

    // Editar (reemplaza el circuito completo)
    public boolean editarCircuito(String nombre, Circuito nuevosDatos) {
        if (!circuitos.containsKey(nombre)) {
            return false;
        }
        circuitos.put(nombre, nuevosDatos);
        return true;
    }

    // Eliminar
    public boolean eliminarCircuito(String nombre) {
        return circuitos.remove(nombre) != null;
    }

    // Listar todos
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
