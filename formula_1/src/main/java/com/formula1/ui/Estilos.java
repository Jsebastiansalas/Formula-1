package com.formula1.ui;

import javafx.scene.Scene;

public class Estilos {
    private static final String RUTA_CSS = "/styles/f1-theme.css";

    public static void aplicar(Scene scene) {
        scene.getStylesheets().add(Estilos.class.getResource(RUTA_CSS).toExternalForm());
    }
}