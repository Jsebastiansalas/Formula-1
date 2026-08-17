package com.formula1.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.formula1.modelo.Circuito;
import com.formula1.simulacion.GestorTemporada;

public class PantallaCalendario {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private Scene scene;

    public PantallaCalendario(AppFX app, GestorTemporada gestorTemporada) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Header
        Label titulo = new Label("📅 CALENDARIO F1 TEMPORADA 2026");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        Button btnVolver = new Button("⬅ Volver");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox header = new HBox(20, btnVolver, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        // Scroll para el calendario
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");

        VBox listaCarreras = new VBox(10);
        listaCarreras.setPadding(new Insets(20));

        int carreraActual = gestorTemporada.getCarreraActual();
        var calendario = gestorTemporada.getCalendario();

        for (int i = 0; i < calendario.size(); i++) {
            Circuito circuito = calendario.get(i);
            HBox fila = crearFilaCarrera(i + 1, circuito, i, carreraActual);
            listaCarreras.getChildren().add(fila);
        }

        scroll.setContent(listaCarreras);

        root.setTop(header);
        root.setCenter(scroll);

        return new Scene(root, 1000, 700);
    }

    private HBox crearFilaCarrera(int numero, Circuito circuito, int indice, int carreraActual) {
        String estado;
        String colorFondo;

        if (indice < carreraActual) {
            estado = "✓";
            colorFondo = "#27ae60"; // Verde - completada
        } else if (indice == carreraActual) {
            estado = "▶";
            colorFondo = "#e10600"; // Rojo - siguiente
        } else {
            estado = "";
            colorFondo = "#34495e"; // Gris - pendiente
        }

        Label lblEstado = new Label(estado);
        lblEstado.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        lblEstado.setMinWidth(30);

        Label lblNumero = new Label("R" + numero);
        lblNumero.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        lblNumero.setMinWidth(50);

        Label lblNombre = new Label(circuito.getNombre());
        lblNombre.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        lblNombre.setMinWidth(350);

        Label lblPais = new Label("📍 " + circuito.getPais());
        lblPais.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");
        lblPais.setMinWidth(200);

        Label lblDistancia = new Label(String.format("%.2f km", circuito.getLongitudKm()));
        lblDistancia.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");
        lblDistancia.setMinWidth(80);

        HBox fila = new HBox(15, lblEstado, lblNumero, lblNombre, lblPais, lblDistancia);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(15));
        fila.setStyle("-fx-background-color: " + colorFondo + "; -fx-background-radius: 8;");

        return fila;
    }

    public Scene getScene() {
        return scene;
    }
}
