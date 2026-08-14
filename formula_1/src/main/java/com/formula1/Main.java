package com.formula1;

import com.formula1.gestor.GestorCircuitos;

public class Main {
    public static void main(String[] args) {
        GestorCircuitos gestor = new GestorCircuitos();
    gestor.cargarDesdeJSON("data/circuitos.json");
    gestor.listarCircuitos();
    }
    
}
