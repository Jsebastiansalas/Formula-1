package com.formula1.ui;

import javafx.scene.Scene;

/**
 * Clase utilitaria para aplicar el tema CSS global de la aplicacion.
 * Carga el archivo f1-theme.css (tema oscuro con colores F1) y lo aplica
 * a cualquier Scene. Debe llamarse en cada pantalla para mantener
 * consistencia visual en toda la aplicacion.
 */
public class Estilos {
    /** Ruta al archivo CSS del tema F1 en el classpath */
    private static final String RUTA_CSS = "/styles/f1-theme.css";

    /**
     * Aplica el tema CSS de Formula 1 a la escena dada.
     * @param scene escena de JavaFX a estilizar
     */
    public static void aplicar(Scene scene) {
        scene.getStylesheets().add(Estilos.class.getResource(RUTA_CSS).toExternalForm());
    }
}